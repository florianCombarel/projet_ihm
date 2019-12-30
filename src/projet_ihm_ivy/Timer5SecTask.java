package projet_ihm_ivy;

import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

public class Timer5SecTask extends TimerTask {
	
	private final CountDownLatch doneSignal;
	
	public Timer5SecTask(CountDownLatch doneSignal) {
		this.doneSignal = doneSignal;
	}
	
	@Override
	public void run() {
	  this.doneSignal.countDown();
	}
}
