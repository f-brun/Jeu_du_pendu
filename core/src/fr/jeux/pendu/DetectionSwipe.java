package fr.jeux.pendu;

import com.badlogic.gdx.input.GestureDetector;

public class DetectionSwipe extends GestureDetector {

	public static final int VELOCITE_LIMITE = 1000 ;

	public DetectionSwipe() {
		super(new DirectionGestureListener());
	}

	public static class DirectionGestureListener extends GestureAdapter{

		@Override
		public boolean fling(float velocityX, float velocityY, int button) {
			if(Math.abs(velocityX)>Math.abs(velocityY)){
				if(velocityX>VELOCITE_LIMITE) {
				    changeNiveau(-1) ;
                }
				else if ((velocityX < (-VELOCITE_LIMITE))) {
					changeNiveau(1) ;
                }
				else return true ;	//Si c'est trop lent, on ne change rien
            }
			return true;
		}

		public static void changeNiveau(int direction) {
			int noNiveau = (Pendu.getEcranHighscores().noNiveauAAfficher+direction)%Pendu.niveaux.length ;
			if (noNiveau < 0 ) noNiveau = Pendu.niveaux.length - 1 ;
			Pendu.getEcranHighscores().noNiveauAAfficher = noNiveau ;	//Passe au niveau suivant ou reboucle
            Pendu.ecranHighscores.actualiseUI() ;   					//Et on actualise le contenu
			Pendu.ecranHighscores.dimmensionneTextesHighscores() ;		//En mettant la bonne couleur
		}
		
	}
}