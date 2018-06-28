package fr.jeux.pendu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;

public class DetectionSwipe extends GestureDetector {

	public static final int VELOCITE_LIMITE = 1000 ;

	public DetectionSwipe() {
		super(new DirectionGestureListener());
	}

	private static class DirectionGestureListener extends GestureAdapter{
		private String message ;

		@Override
		public boolean fling(float velocityX, float velocityY, int button) {
			if(Math.abs(velocityX)>Math.abs(velocityY)){
                int direction = 0 ;
				if(velocityX>VELOCITE_LIMITE) {
				    direction = -1 ;
                }
				else if ((velocityX < (-VELOCITE_LIMITE))) {
				    direction = +1 ;
                }
				else return true ;	//Si c'est trop lent, on ne change rien
				int noNiveau = (Pendu.getNiveau().getNumero()+direction)%Pendu.niveaux.length ;
				if (noNiveau < 0 ) noNiveau = Pendu.niveaux.length - 1 ;
                Pendu.niveau = Pendu.niveaux[noNiveau] ;  //Passe au niveau suivant ou reboucle
                Pendu.ecranHighscores.actualiseUI() ;   //Et on actualise le contenu
            }
			return true;
		}
/*		@Override
		public boolean pan(float x, float y, float deltaX, float deltaY) {
			message = "Pan performed, delta:" + Float.toString(deltaX) +
					"," + Float.toString(deltaY);
			Gdx.app.log("INFO", message);
			return true;
		}*/
	}
}