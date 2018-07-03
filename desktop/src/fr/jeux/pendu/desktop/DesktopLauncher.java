package fr.jeux.pendu.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;  
import fr.jeux.pendu.Pendu;

public class DesktopLauncher {

    public static final int LARGEUR_CIBLE = 800 ;   //Largeur de la fenêtre
    public static final int HAUTEUR_CIBLE = 600 ;   //Hauteur de la fenêtre
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = LARGEUR_CIBLE ;
		config.height = HAUTEUR_CIBLE ;
		new LwjglApplication(new Pendu(), config);
	}
}
