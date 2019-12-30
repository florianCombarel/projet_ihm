package projet_ihm_ivy;

import java.util.Timer;
import java.util.concurrent.CountDownLatch;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;

public class MainControllerIvy {

	private static Ivy controllerIvy;
	
	public static void main(String[] args){
		controllerIvy = new Ivy("ControllerIvy", "ControllerIvy launch", null);
		try {
			controllerIvy.start("127.255.255.255:2010");
			
			// Choix de creation d'un rectangle
			controllerIvy.bindMsg("^OneDollar Reco=Rectangle", new IvyMessageListener() {
				@Override
				public void receive(IvyClient client, String[] args) {
					int xRect = 0;
					int yRect = 0;
					
					// Detection de la voix pour la couleur ou la position
					try {
						Timer timer5Sec = new Timer();
						CountDownLatch positionSignal = new CountDownLatch(1);
						timer5Sec.schedule(new Timer5SecTask(positionSignal), 5000);
						IvyMsgListenerPosition positionListener = new IvyMsgListenerPosition(positionSignal,controllerIvy,timer5Sec);
						int idPos = controllerIvy.bindMsgOnce("^sra5 Text=(.*) Confidence=(.*)", positionListener);
						//positionSignal.await();
						controllerIvy.waitForMsg("^sra5 Text=(.*) Confidence=(.*)", 5000);
						controllerIvy.unBindMsg(idPos);
						xRect = positionListener.getX();
						yRect = positionListener.getY();
						System.out.println(xRect+" / "+yRect);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			// Choix de creation d'une ellipse
			controllerIvy.bindMsg("^OneDollar Reco=ellipse", new IvyMessageListener() {
				@Override
				public void receive(IvyClient client, String[] args) {
					
				}
			});
			
			// Choix de suppression d'un objet
			controllerIvy.bindMsg("^OneDollar Reco=Supprimer", new IvyMessageListener() {
				@Override
				public void receive(IvyClient client, String[] args) {
					
				}
			});
			
			// Choix de deplacement d'un objet
			controllerIvy.bindMsg("^OneDollar Reco=Modifier", new IvyMessageListener() {
				@Override
				public void receive(IvyClient client, String[] args) {
					
				}
			});
			
		} catch (IvyException e) {
			e.printStackTrace();
		}
	}
	
}
