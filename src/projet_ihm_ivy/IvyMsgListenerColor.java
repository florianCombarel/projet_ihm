package projet_ihm_ivy;

import java.util.Timer;
import java.util.concurrent.CountDownLatch;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyMessageListener;

public class IvyMsgListenerColor implements IvyMessageListener{
	private String color;
	private Ivy controllerIvy;
	private final CountDownLatch doneSignal;
	
	public IvyMsgListenerColor(CountDownLatch doneSignal, Ivy controllerIvy) {
		this.doneSignal = doneSignal;
		this.controllerIvy = controllerIvy;
		this.color = "black";
	}
	
	@Override
	public void receive(IvyClient client, String[] args) {
		try {
			CountDownLatch testPointSignal = new CountDownLatch(1);
			ResultTestPoint resultTestPoint = new ResultTestPoint(this.controllerIvy, testPointSignal);
			int id = this.controllerIvy.bindMsg("^Palette:ResultatTesterPoint x=(.*) y=(.*) nom=(.*)", resultTestPoint, true);
			this.controllerIvy.sendMsg("Palette:TesterPoint x="+args[0]+" y="+args[1]);
			testPointSignal.await();
			this.controllerIvy.unBindMsg(id);
			this.color = resultTestPoint.getColor();
		}catch(Exception e) {
			e.printStackTrace();
		}
		this.doneSignal.countDown();
	}
	
	public String getColor() {
		return this.color;
	}
}
