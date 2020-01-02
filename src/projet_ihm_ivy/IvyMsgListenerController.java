package projet_ihm_ivy;

import java.util.Timer;
import java.util.concurrent.CountDownLatch;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;

public class IvyMsgListenerController implements IvyMessageListener {

	private Ivy controllerIvy;
	private CountDownLatch doneSignal;
	private Timer timer5Sec;
	private int x;
	private int y;
	private String color;
	
	public IvyMsgListenerController(CountDownLatch doneSignal, Ivy controllerIvy, Timer timer5Sec) {
		super();
		this.controllerIvy = controllerIvy;
		this.doneSignal = doneSignal;
		this.timer5Sec = timer5Sec;
		this.x = 0;
		this.y = 0;
		this.color = "black";
	}
	
	@Override
	public void receive(IvyClient client, String[] args) {
		Double confidence = Double.parseDouble(args[1].replace(",", "."));
		if(confidence > 0.8) {
			String msg = args[0];
			if (msg.equals("ici") || msg.equals("la") || msg.equals("a cette position")) {
				if(this.x == 0 && this.y == 0) {
					this.timer5Sec.cancel();
					CountDownLatch xySignal = new CountDownLatch(1);
					IvyMsgListenerXY xyListener = new IvyMsgListenerXY(xySignal);
					try {
						this.controllerIvy.bindMsgOnce("^Palette:MousePressed x=(.*) y=(.*)", xyListener);
						xySignal.await();
						this.x = xyListener.getX();
						this.y = xyListener.getY();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (msg.equals("rouge") || msg.equals("bleu") || msg.equals("jaune") || msg.equals("vert")) {
				if(this.color == "black") {
					this.timer5Sec.cancel();
					switch(msg) {
					case "rouge":
						this.color = "red";
						break;
					case "bleu":
						this.color = "blue";
						break;
					case "jaune":
						this.color = "yellow";
						break;
					case "vert":
						this.color = "green";
						break;
					}
					this.doneSignal.countDown();
				}
			} else if (msg.equals("de cette couleur")) {
				if(this.color.equals("black")) {
					this.timer5Sec.cancel();
					CountDownLatch colorSignal = new CountDownLatch(1);
					IvyMsgListenerColor colorListener = new IvyMsgListenerColor(colorSignal,controllerIvy);
					try {
						int id = this.controllerIvy.bindMsg("^Palette:MousePressed x=(.*) y=(.*)", colorListener);
						colorSignal.await();
						this.controllerIvy.unBindMsg(id);
						this.color = colorListener.getColor();
					}catch(Exception e) {
						e.printStackTrace();
					}
					this.doneSignal.countDown();
				}
			}
		}
		this.doneSignal.countDown();
	}
	
	public void setDoneSignal(CountDownLatch doneSignal) {
		this.doneSignal = doneSignal;
	}
	
	public void setTimer5Sec(Timer timer5Sec) {
		this.timer5Sec = timer5Sec;
	}
	
	public String getColor() {
		return this.color;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
}
