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

            Pendu.lettresProposes += lettreProposee;
//            bouton.setDisabled(true);

            if (CompleteMotADeviner(lettreProposee)) {
                Pendu.lMotDevine.setText(SepareParDesEspaces(Pendu.motDevine));
                if (Pendu.motDevine.equals(Pendu.motADeviner)) {    //Vérifie si on a gagné
//                    jeu.lMotDevine.setText(SepareParDesEspaces(jeu.motDevine));
                    Pendu.ecranJeu.gagne(jeu);
                }	
            } else {
                Pendu.nbErreurs++;
                Pendu.lNbEssaisRestants.setText("Nombre d'essais\nrestants :\n"+(Pendu.getNiveau().getNiveauDeJeu().getNbErreursMax() - Pendu.getNbErreurs())); ;
                if (Pendu.DEBUG) System.out.println("La lettre " + lettreProposee + " n'est pas dans le mot");
                if (Pendu.DEBUG) System.out.println(Pendu.getNbErreurs() + " erreur(s) jusqu'ici");
                Pendu.affichagePendu.setDrawable(new SpriteDrawable(new Sprite(Pendu.getImagesPendu()[Pendu.getNiveau().getNiveauDeJeu().getImagesPendaison()[Pendu.getNbErreurs()]])));
                if (Pendu.getNbErreurs() >= Pendu.getNiveau().getNiveauDeJeu().getNbErreursMax()) {	//On a perdu
                    Pendu.ecranJeu.perdu(jeu);
                }
            }
            acteur.setVisible(false) ;	//Fait disparaître la lettre
        }
    }
}
