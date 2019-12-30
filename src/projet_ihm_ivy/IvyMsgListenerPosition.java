package projet_ihm_ivy;

import java.util.Timer;
import java.util.concurrent.CountDownLatch;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyMessageListener;

public class IvyMsgListenerPosition implements IvyMessageListener {

	private Ivy controllerIvy;
	private final CountDownLatch doneSignal;
	private Timer timer5Sec;
	private int x;
	private int y;
	
	public IvyMsgListenerPosition(CountDownLatch doneSignal, Ivy controllerIvy, Timer timer5Sec) {
		super();
		this.controllerIvy = controllerIvy;
		this.doneSignal = doneSignal;
		this.timer5Sec = timer5Sec;
		this.x = 0;
		this.y = 0;
	}
	
	@Override
	public void receive(IvyClient client, String[] args) {
		Double confidence = Double.parseDouble(args[1].replace(",", "."));
		if(confidence > 0.8) {
			String msg = args[0];
			if (msg.equals("ici") || msg.equals("la") || msg.equals("a cette position")) {
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
		}
		this.doneSignal.countDown();
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
}
