package fr.jeux.pendu.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;  
import fr.jeux.pendu.Pendu;
import static fr.jeux.pendu.Config.* ;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = LARGEUR_CIBLE ;
		config.height = HAUTEUR_CIBLE ;
		new LwjglApplication(new Pendu(), config);
	}
}
