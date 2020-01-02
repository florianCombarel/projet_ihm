package projet_ihm_ivy;

import java.util.Timer;
import java.util.concurrent.CountDownLatch;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyMessageListener;

public class IvyMsgListenerName implements IvyMessageListener{
	private Ivy controllerIvy;
	private final CountDownLatch doneSignal;
	private String name;
	
	public IvyMsgListenerName(CountDownLatch doneSignal, Ivy controllerIvy) {
		this.doneSignal = doneSignal;
		this.controllerIvy = controllerIvy;
		this.name = "";
	}
	
	@Override
	public void receive(IvyClient client, String[] args) {
		try {
			CountDownLatch testPointSignal = new CountDownLatch(1);
			ResultTestPoint resultTestPoint = new ResultTestPoint(testPointSignal);
			int id = this.controllerIvy.bindMsg("^Palette:ResultatTesterPoint x=(.*) y=(.*) nom=(.*)", resultTestPoint, true);
			this.controllerIvy.sendMsg("Palette:TesterPoint x="+args[0]+" y="+args[1]);
			testPointSignal.await();
			this.controllerIvy.unBindMsg(id);
			this.name = resultTestPoint.getName();
		}catch(Exception e) {
			e.printStackTrace();
		}
		this.doneSignal.countDown();
	}
	
	public String getName() {
		return this.name;
	}
}
