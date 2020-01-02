package projet_ihm_ivy;

import java.util.Timer;
import java.util.concurrent.CountDownLatch;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;

public class MainControllerIvy {

	private static Ivy controllerIvy;
	private static boolean inAction;
	
	public static void main(String[] args){
		controllerIvy = new Ivy("ControllerIvy", "ControllerIvy launch", null);
		inAction = false;
		try {
			controllerIvy.start("127.255.255.255:2010");
			
			// Choix de creation d'un rectangle
			controllerIvy.bindMsg("^OneDollar Reco=Rectangle", new IvyMessageListener() {
				@Override
				public void receive(IvyClient client, String[] args) {
					if(!inAction) {
						inAction = true;
						int xRect = 0;
						int yRect = 0;
						String color = "black";
						
						// Detection de la voix pour la couleur ou la position
						try {
							Timer timer5Sec = new Timer();
							CountDownLatch signal = new CountDownLatch(1);
							timer5Sec.schedule(new Timer5SecTask(signal), 5000);
							IvyMsgListenerController controllerListener = new IvyMsgListenerController(signal,controllerIvy,timer5Sec);
							int id = controllerIvy.bindMsg("^sra5 Text=(.*) Confidence=(.*)", controllerListener);
							signal.await();
							controllerIvy.unBindMsg(id);
							if(controllerListener.getX() != 0 || !controllerListener.getColor().equals("noir")) {
								Timer timer5Sec2 = new Timer();
								CountDownLatch signal2 = new CountDownLatch(1);
								timer5Sec2.schedule(new Timer5SecTask(signal2), 5000);
								controllerListener.setDoneSignal(signal2);
								controllerListener.setTimer5Sec(timer5Sec2);
								id = controllerIvy.bindMsg("^sra5 Text=(.*) Confidence=(.*)", controllerListener);
								signal2.await();
								controllerIvy.unBindMsg(id);
							}
							xRect = controllerListener.getX();
							yRect = controllerListener.getY();
							color = controllerListener.getColor();
							controllerIvy.sendMsg("Palette:CreerRectangle x="+xRect+" y="+yRect+" longueur=50 hauteur=20 couleurFond="+color+" couleurContour="+color);
							System.out.println(xRect+" / "+yRect+" / "+color);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					inAction = false;
				}
			});
			
			// Choix de creation d'une ellipse
			controllerIvy.bindMsg("^OneDollar Reco=Ellipse", new IvyMessageListener() {
				@Override
				public void receive(IvyClient client, String[] args) {
					if(!inAction) {
						inAction = true;
						int xRect = 0;
						int yRect = 0;
						String color = "black";
						
						// Detection de la voix pour la couleur ou la position
						try {
							Timer timer5Sec = new Timer();
							CountDownLatch signal = new CountDownLatch(1);
							timer5Sec.schedule(new Timer5SecTask(signal), 5000);
							IvyMsgListenerController controllerListener = new IvyMsgListenerController(signal,controllerIvy,timer5Sec);
							int id = controllerIvy.bindMsg("^sra5 Text=(.*) Confidence=(.*)", controllerListener);
							signal.await();
							controllerIvy.unBindMsg(id);
							if(controllerListener.getX() != 0 || !controllerListener.getColor().equals("black")) {
								Timer timer5Sec2 = new Timer();
								CountDownLatch signal2 = new CountDownLatch(1);
								timer5Sec2.schedule(new Timer5SecTask(signal2), 5000);
								controllerListener.setDoneSignal(signal2);
								controllerListener.setTimer5Sec(timer5Sec2);
								id = controllerIvy.bindMsg("^sra5 Text=(.*) Confidence=(.*)", controllerListener);
								signal2.await();
								controllerIvy.unBindMsg(id);
							}
							xRect = controllerListener.getX();
							yRect = controllerListener.getY();
							color = controllerListener.getColor();
							controllerIvy.sendMsg("Palette:CreerEllipse x="+xRect+" y="+yRect+" longueur=20 hauteur=50 couleurFond="+color+" couleurContour="+color);
							System.out.println(xRect+" / "+yRect+" / "+color);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					inAction = false;
				}
			});
			
			// Choix de suppression d'un objet
			controllerIvy.bindMsg("^OneDollar Reco=Supprimer", new IvyMessageListener() {
				@Override
				public void receive(IvyClient client, String[] args) {
					try {
						controllerIvy.sendMsg("Palette:DemanderInfo nom=R5");
					} catch (IvyException e) {
						e.printStackTrace();
					}
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
