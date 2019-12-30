package projet_ihm_ivy;

import java.util.concurrent.CountDownLatch;

import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyMessageListener;

public class IvyMsgListenerXY implements IvyMessageListener{
	private int x;
	private int y;
	private final CountDownLatch doneSignal;
	
	public IvyMsgListenerXY(CountDownLatch doneSignal) {
		this.doneSignal = doneSignal;
	}
	
	@Override
	public void receive(IvyClient client, String[] args) {
		this.x = Integer.valueOf(args[0]).intValue();
		this.y = Integer.valueOf(args[1]).intValue();
		this.doneSignal.countDown();
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
}
