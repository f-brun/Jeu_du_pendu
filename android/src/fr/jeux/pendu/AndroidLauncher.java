package fr.jeux.pendu;

import android.content.res.Configuration;
import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import fr.jeux.pendu.Pendu;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		Gdx.input.setCatchBackKey(true);	//Capture la touche "back" pour éviter une sortie prématurée du programme et faire les retours arrière dans les écrans
		initialize(new Pendu(), config);
	}
    public void onConfigurationChanged (Configuration newConfig) {
    	super.onConfigurationChanged (newConfig) ;
    }
}
