package projet_ihm_ivy;

import java.util.concurrent.CountDownLatch;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyMessageListener;

public class ResultTestPoint implements IvyMessageListener{

	private Ivy controllerIvy;
	private String color;
	private CountDownLatch testPointSignal;
	private String name;
	
	public ResultTestPoint(Ivy controllerIvy, CountDownLatch testPointSignal) {
		this.color = "black";
		this.controllerIvy = controllerIvy;
		this.testPointSignal = testPointSignal;
	}
	
	public ResultTestPoint(CountDownLatch testPointSignal) {
		this.name = "";
		this.testPointSignal = testPointSignal;
		this.controllerIvy = null;
	}
	
	@Override
	public void receive(IvyClient client, String[] args) {
		if(this.controllerIvy != null) {
			try {
				CountDownLatch colorSignal = new CountDownLatch(1);
				Info info = new Info(colorSignal);
				int id = this.controllerIvy.bindMsg("^Palette:Info nom=(.*) x=(.*) y=(.*) longueur=(.*) hauteur=(.*) couleurFond=(.*) couleurContour=(.*)", info, true);
				this.controllerIvy.sendMsg("Palette:DemanderInfo nom="+args[2]);
				colorSignal.await();
				this.controllerIvy.unBindMsg(id);
				this.color = info.getColor();
			}catch(Exception e) {
				e.printStackTrace();
			}
		} else {
			this.name = args[2];
		}
		this.testPointSignal.countDown();
	}
	
	public String getColor() {
		return this.color;
	}
	
	public String getName() {
		return this.name;
	}
}
