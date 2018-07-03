package fr.jeux.pendu;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;

public class GestionClavier extends InputAdapter {
	public interface EcouteClavier {
		void toucheGAUCHE() ;
		void toucheDROITE() ;
		void toucheESCAPE() ;
	}
	private EcouteClavier ecouteClavier;


	public GestionClavier(EcouteClavier ecouteClavier) {
		super();
		this.ecouteClavier = ecouteClavier;
	}

	@Override
	public boolean keyDown(int keycode) {
        if(keycode == Keys.LEFT) ecouteClavier.toucheGAUCHE();
        if(keycode == Keys.RIGHT) ecouteClavier.toucheDROITE();
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK) ecouteClavier.toucheESCAPE(); //La touche Back (sous Android) et la touche Esc on le même effet
        return true;
	}
}
