package projet_ihm_ivy;

import java.util.Timer;
import java.util.concurrent.CountDownLatch;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;

public class IvyMsgListenerControllerMovement implements IvyMessageListener {

	private Ivy controllerIvy;
	private CountDownLatch doneSignal;
	private String name;
	int x;
	int y;
	
	public IvyMsgListenerControllerMovement(CountDownLatch doneSignal, Ivy controllerIvy) {
		super();
		this.controllerIvy = controllerIvy;
		this.doneSignal = doneSignal;
		this.name = "";
		this.x = 0;
		this.y = 0;
	}
	
	@Override
	public void receive(IvyClient client, String[] args) {
		Double confidence = Double.parseDouble(args[1].replace(",", "."));
		if(confidence > 0.8) {
			String msg = args[0];
			if (msg.equals("cet objet") || msg.equals("ce rectangle") || msg.equals("cette ellipse")) {
				if(this.name.equals("") || this.name == null) {
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
			} else if (msg.equals("ici") || msg.equals("la") || msg.equals("a cette position")) {
				if(this.x == 0 && this.y == 0) {
					CountDownLatch xySignal = new CountDownLatch(1);
					IvyMsgListenerXY xyListener = new IvyMsgListenerXY(xySignal);
					try {
						int id = this.controllerIvy.bindMsg("^Palette:MousePressed x=(.*) y=(.*)", xyListener, true);
						xySignal.await();
						this.controllerIvy.unBindMsg(id);
						this.x = xyListener.getX();
						this.y = xyListener.getY();
					} catch (Exception e) {
						e.printStackTrace();
					}
					this.doneSignal.countDown();
				}
			}
		}
	}
	
	public void setDoneSignal(CountDownLatch doneSignal) {
		this.doneSignal = doneSignal;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
}
