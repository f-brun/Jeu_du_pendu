package fr.jeux.pendu;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import static fr.jeux.pendu.GestionMots.CompleteMotADeviner;
import static fr.jeux.pendu.GestionMots.SepareParDesEspaces;
/**
 *
 * @author Florent
 */
public class GestionnaireDeClics extends ChangeListener {
    Config cfg ;
    
    @Override
    public void changed(ChangeEvent evenement,Actor acteur) {
        String lettreProposee;
        TextButton bouton;
        if (acteur instanceof TextButton) {
            bouton = (TextButton) acteur;
            lettreProposee = bouton.getText().toString();

            cfg.lettresProposes += lettreProposee;
            bouton.setDisabled(true);

            if (CompleteMotADeviner(lettreProposee)) {
                cfg.lMotDevine.setText(SepareParDesEspaces(cfg.motDevine));
                if (cfg.motDevine.equals(cfg.motADeviner)) {    //Vérifie si on a gagné
                    cfg.lMotDevine.setText(SepareParDesEspaces(cfg.motDevine));
                    cfg.partie.gagne();
                }	
            } else {
                cfg.nbErreurs++;
                if (cfg.DEBUG) System.out.println("La lettre " + lettreProposee + " n'est pas dans le mot");
                if (cfg.DEBUG) System.out.println(cfg.nbErreurs + " erreur(s) jusqu'ici");
                cfg.affichagePendu.setDrawable(new SpriteDrawable(new Sprite(cfg.imagePendu[cfg.nbErreurs])));
                if (cfg.nbErreurs >= cfg.nbErreursMax) {	//On a perdu
                    cfg.partie.perdu();
                }
            }
            acteur.remove();
        }
    }
}
