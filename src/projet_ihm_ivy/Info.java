package projet_ihm_ivy;

import java.util.concurrent.CountDownLatch;

import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyMessageListener;

public class Info implements IvyMessageListener{

	private String color;
	private CountDownLatch colorSignal;
	
	public Info(CountDownLatch colorSignal) {
		this.color = "black";
		this.colorSignal = colorSignal;
	}
	
	@Override
	public void receive(IvyClient client, String[] args) {
		this.color = args[5];
		System.out.println(this.color);
		this.colorSignal.countDown();
	}
	
	public String getColor() {
		return this.color;
	}	
}
