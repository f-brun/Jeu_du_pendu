package fr.jeux.pendu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import static com.badlogic.gdx.utils.Timer.schedule;
import static fr.jeux.pendu.GestionMots.SepareParDesEspaces;
import static fr.jeux.pendu.GestionMots.cfg;

/**
 *
 * @author Florent
 */
public class Partie {

    Table table;   //table principale sur laquelle s'affichent les �l�ments graphiques
    Texture img;   //Texture de l'image du pendu en cours ou de l'image de fin
    Image imageFin; //El�ment d'UI contenant l'image de fin
    Label lPerdu ;  //Label affichant la r�ponse quand on a perdu
    Label lAffichageScore ; //Label pour afficher le score actuel

    Config cfg;
    Table tLettres;    //Element d'UI contenant 
//    protected Boolean retourMenu;  //Indique que la partie est finie et qu'il faut revenir au menu

    Partie(Config config, int niveau) {
        cfg = config;
        Init(niveau);
        cfg.tMenu.setVisible(false);  //Rend le menu invisible
        DessineUI(); //Cr�ation de la fen�tre de jeu
        if (cfg.DEBUG) {
            System.out.println("D�but de partie");
        }
    }

    void Init(int niveau) {
        cfg.nbErreurs = 0;
        cfg.nbErreursMax = (cfg.NB_IMAGES - 1) / niveau;
        int i; //index
        cfg.motADeviner = cfg.listeMots.get((int) (Math.random() * cfg.nombreMotsDico));
//Cr�e le mot devin� en rempla�ant toutes les lettres par des sous-tirets
        cfg.motDevine = "";
        for (i = 0; i < cfg.motADeviner.length(); i++) {
            if ((cfg.motADeviner.charAt(i) < 'A') || (cfg.motADeviner.charAt(i) > 'Z')) {   //Si c'est un caract�re non-alphab�tique, on le recopie (il est d�j� donn�)
                cfg.motDevine += cfg.motADeviner.charAt(i);
            } else {
                cfg.motDevine += "_";   //Si c'est un caract�re alphab�tique, on remplace par un sous-tiret
            }
        }
    }

    void DessineUI() {
        char lettre;
        GestionnaireDeClics gestionnaireDeClics;   //Fonction qui va g�rer les clics sur les boutons de lettres
        int i;

        cfg.tPartie = new Table();
        cfg.tPartie.setFillParent(true);  //La table occupe tout l'�cran
        cfg.stage.addActor(cfg.tPartie);

        if (cfg.DEBUG) {
            cfg.tPartie.setDebug(true); // This is optional, but enables debug lines for tables.
        }
        cfg.lMotDevine = new Label(cfg.motDevine, cfg.skin);
        cfg.affichagePendu = new Image(cfg.imagePendu[0]);

        cfg.tPartie.pad(3);
        cfg.tPartie.add(cfg.lMotDevine);
        cfg.tPartie.row();    //Indique que l'�l�ment suivant sera sur une ligne suppl�mentaire
        cfg.tPartie.add(cfg.affichagePendu);
        cfg.tPartie.row();    //Indique que l'�l�ment suivant sera sur une ligne suppl�mentaire

        tLettres = new Table();
        cfg.tPartie.add(tLettres);

        i = 0;
        gestionnaireDeClics = new GestionnaireDeClics();
        for (lettre = 'A'; lettre <= 'Z'; lettre++) {
            TextButton bouton = new TextButton(Character.toString(lettre), cfg.skin);
            if (i % 5 == 0) {
                tLettres.row(); //On passe � la ligne suivante
            }
            i++;
            bouton.addListener(gestionnaireDeClics);
            tLettres.add(bouton);
        }
    }

    void gagne() {

        Timer.Task affichageGagne; //Contiendra le code � ex�cuter pour afficher l'image de fin apr�s un d�lai

        if (cfg.DEBUG) {
            System.out.println("Gagn� !");
        }
        cfg.lMotDevine.setText(SepareParDesEspaces(cfg.motDevine));

        cfg.score++ ;
        
        affichageGagne = new Timer.Task() {
            Config cfg ;
            @Override
            public void run() {
                cfg.tPartie.reset();       //Efface l'�cran de la partie
                
                lAffichageScore = new Label("Score : " + Integer.toString(cfg.score), cfg.skin);
                if (img != null) img.dispose();
                img = new Texture(cfg.IMAGE_GAGNE);
                imageFin = new Image(img);
                
                if (cfg.tFin == null) {
                    cfg.tFin = new Table() ;
                }
                else {
                    cfg.tFin.reset();  //Efface le contenu de la table
                }
                
                cfg.stage.addActor(cfg.tFin);
                cfg.tFin.setFillParent(true);  //La table occupe tout l'�cran
                cfg.tFin.pad(3) ;
                if (cfg.DEBUG) cfg.tFin.setDebug(true); // This is optional, but enables debug lines for tables.
                               
                cfg.tFin.add(lAffichageScore);
                cfg.tFin.row();
                cfg.tFin.add(imageFin);
                cfg.tFin.row() ;
                cfg.tFin.setVisible(true) ;
          
                imageFin.addListener(new ClickListener() {
                    Config cfg ;
                    public void clicked(InputEvent event, float x, float y) {
                        if (cfg.DEBUG) {
                            System.out.println("Retour au menu");
                        }
                        cfg.partie.RetourMenu() ;
                    }
                });
            }
        };
        schedule(affichageGagne,1f) ;
    }

    public void RetourMenu() {
        imageFin.remove();
        if (img != null) img.dispose();
        cfg.tFin.setVisible(false) ;
        cfg.tMenu.setVisible(true);  //r�-affiche le menu
    }
    
    void perdu() {
        String textePerdu ; //Texte � afficher pour donner la r�ponse
        if (cfg.DEBUG) {
            System.out.println("Perdu !");
        }
        cfg.tPartie.reset();       //Efface l'�cran de la partie

        lAffichageScore =new Label("Score final : "+ Integer.toString(cfg.score),cfg.skin) ;
        cfg.score = 0 ;     //On remet le score � z�ro apr�s l'avoir affich�
        
        if (img != null) img.dispose();
        img = new Texture(cfg.IMAGE_PERDU);
        imageFin = new Image(img);

        textePerdu = "Il fallait trouver : "+cfg.motADeviner ;
        if (lPerdu == null) {
            lPerdu = new Label(textePerdu,cfg.skin) ;
        }
        else {
            lPerdu.setText(textePerdu);
        }
        
        if (cfg.tFin == null) {
            cfg.tFin = new Table() ;
        }
        else {
            cfg.tFin.reset();  //Efface le contenu de la table
        }
        cfg.stage.addActor(cfg.tFin) ;
        cfg.tFin.pad(3) ;
        if (cfg.DEBUG) cfg.tFin.setDebug(true); // This is optional, but enables debug lines for tables.
        cfg.tFin.setFillParent(true);  //La table occupe tout l'�cran
        cfg.tFin.add(lAffichageScore) ;
        cfg.tFin.row();
        cfg.tFin.add(imageFin);
        cfg.tFin.row() ;    //Place le texte en dessous de l'image
        cfg.tFin.add(lPerdu) ;
        cfg.tFin.setVisible(true) ;
               
        imageFin.addListener(new ClickListener() {
            Config cfg ;
            public void clicked(InputEvent event, float x, float y) {
                if (cfg.DEBUG) {
                    System.out.println("Retour au menu");
                }
                cfg.partie.RetourMenu() ;
            }
        });
    }
}
