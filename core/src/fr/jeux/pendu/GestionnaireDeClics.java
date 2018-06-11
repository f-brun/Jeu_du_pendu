package fr.jeux.pendu;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import static fr.jeux.pendu.GestionMots.CompleteMotADeviner;
import static fr.jeux.pendu.GestionMots.SepareParDesEspaces;


/**
 *
 * @author Florent
 */
public class GestionnaireDeClics extends ChangeListener {
    Pendu jeu ;	//Référence aux données du jeu

    public GestionnaireDeClics(Pendu jeuEnCours) {
    	super() ;
    	jeu = jeuEnCours ;
    }
    
    @Override
    public void changed(ChangeEvent evenement,Actor acteur) {
        String lettreProposee;
        TextButton bouton;
        if (acteur instanceof TextButton) {
            bouton = (TextButton) acteur;
            lettreProposee = bouton.getText().toString();

            jeu.lettresProposes += lettreProposee;
//            bouton.setDisabled(true);

            if (CompleteMotADeviner(lettreProposee)) {
                jeu.lMotDevine.setText(SepareParDesEspaces(jeu.motDevine));
                if (jeu.motDevine.equals(jeu.motADeviner)) {    //Vérifie si on a gagné
//                    jeu.lMotDevine.setText(SepareParDesEspaces(jeu.motDevine));
                    jeu.ecranJeu.gagne(jeu);
                }	
            } else {
                jeu.nbErreurs++;
                if (jeu.DEBUG) System.out.println("La lettre " + lettreProposee + " n'est pas dans le mot");
                if (jeu.DEBUG) System.out.println(jeu.nbErreurs + " erreur(s) jusqu'ici");
                jeu.affichagePendu.setDrawable(new SpriteDrawable(new Sprite(jeu.getImagesPendu()[jeu.nbErreurs])));
                if (jeu.nbErreurs >= jeu.nbErreursMax) {	//On a perdu
                    jeu.ecranJeu.perdu(jeu);
                }
            }
            acteur.setVisible(false) ;	//Fait disparaître la lettre
        }
    }
}
