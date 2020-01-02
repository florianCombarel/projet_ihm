package projet_ihm_ivy;

import java.util.Timer;
import java.util.concurrent.CountDownLatch;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;

public class IvyMsgListenerControllerSuppression implements IvyMessageListener {

	private Ivy controllerIvy;
	private CountDownLatch doneSignal;
	private Timer timer5Sec;
	private String name;
	
	public IvyMsgListenerControllerSuppression(CountDownLatch doneSignal, Ivy controllerIvy, Timer timer5Sec) {
		super();
		this.controllerIvy = controllerIvy;
		this.doneSignal = doneSignal;
		this.timer5Sec = timer5Sec;
		this.name = "";
	}
	
	@Override
	public void receive(IvyClient client, String[] args) {
		Double confidence = Double.parseDouble(args[1].replace(",", "."));
		if(confidence > 0.8) {
			String msg = args[0];
			if (msg.equals("cet objet") || msg.equals("ce rectangle") || msg.equals("cette ellipse")) {
				this.timer5Sec.cancel();
				CountDownLatch nameSignal = new CountDownLatch(1);
				IvyMsgListenerName nameListener = new IvyMsgListenerName(nameSignal,controllerIvy);
				try {
					int id = this.controllerIvy.bindMsg("^Palette:MousePressed x=(.*) y=(.*)", nameListener, true);
					nameSignal.await();
					this.controllerIvy.unBindMsg(id);
					this.name = nameListener.getName();
				}catch(Exception e) {
					e.printStackTrace();
				}
				switch(this.name.charAt(0)){
				case 'E':
					if(!msg.equals("cette ellipse") && !msg.equals("cet objet")) {
						this.name = null;
					}
					break;
				case 'R':
					if(!msg.equals("ce rectangle") && !msg.equals("cet objet")) {
						this.name = null;
					}
					break;
				default:
					this.name = null;
					break;
				}
				this.doneSignal.countDown();
			}
		}
	}
	
	public void setDoneSignal(CountDownLatch doneSignal) {
		this.doneSignal = doneSignal;
	}
	
	public void setTimer5Sec(Timer timer5Sec) {
		this.timer5Sec = timer5Sec;
	}
	
	public String getName() {
		return this.name;
	}
}
