package fr.jeux.pendu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import fr.jeux.pendu.Pendu;
import fr.jeux.pendu.GestionnaireDeClics;

import static com.badlogic.gdx.utils.Timer.schedule;
import static fr.jeux.pendu.GestionMots.SepareParDesEspaces;

import java.util.Iterator;

@SuppressWarnings("static-access")
/**
 *
 * @author Florent
 */
public class EcranJeu implements Screen {

    static Pendu jeu ;	//r�f�rence aux donn�es du jeu
	
	Stage	stage ;	//Conteneur g�n�ral de l'�cran
    Table	table;   //table principale sur laquelle s'affichent les �l�ments graphiques
    Texture img;   //Texture de l'image du pendu en cours ou de l'image de fin
//    Image	imageFin; //El�ment d'UI contenant l'image de fin
//    Label	lPerdu ;  //Label affichant la r�ponse quand on a perdu
//    Label	lAffichageScore ; //Label pour afficher le score actuel

    Table	tLettres;    //Element d'UI contenant les lettres
    Cell	celluleLettres ;	//Cellule contenant les lettres � cliquer (sert pour redimensionner la cellule)
    Cell	celluleMotDevine ;	//Cellule contenant le texte du mot devin� jusqu'� l� (sert pour redimensionner)
    
//    protected Boolean retourMenu;  //Indique que la partie est finie et qu'il faut revenir au menu

    public EcranJeu(Pendu jeuEnCours, int niveau) {
    	jeu = jeuEnCours ;
    	
    	jeu.setEcranJeu(this); 	//Enregistre la r�f�rence de cet �cran
    	
        creeUI(jeu); //Cr�ation de la fen�tre de jeu
    }

    void init(Pendu jeu) {
        jeu.nbErreurs = 0;
        jeu.nbErreursMax = (jeu.NB_IMAGES - 1) / jeu.getNiveau();
        jeu.motADeviner = jeu.dictionnaire.choisitMotAuHasard();
//Cr�e le mot devin� en rempla�ant toutes les lettres par des sous-tirets
        jeu.motDevine = "";
        for (int i = 0; i < jeu.motADeviner.length(); i++) {
            if ((jeu.motADeviner.charAt(i) < 'A') || (jeu.motADeviner.charAt(i) > 'Z')) {   //Si c'est un caract�re non-alphab�tique, on le recopie (il est d�j� donn�)
                jeu.motDevine += jeu.motADeviner.charAt(i);
            } else {
                jeu.motDevine += "_";   //Si c'est un caract�re alphab�tique, on remplace par un sous-tiret
            }
        }
    }

    private void creeUI(Pendu jeu) {
        char lettre;
        GestionnaireDeClics gestionnaireDeClics;   //Fonction qui va g�rer les clics sur les boutons de lettres
        int i;

        stage = new Stage(new ScreenViewport());
        table = new Table();
        table.setFillParent(true);  //La table occupe tout l'�cran
        
        stage.addActor(table);

        if (jeu.getDebugState()) {
            table.setDebug(true); // This is optional, but enables debug lines for tables.
        }
        jeu.lMotDevine = new Label("", jeu.styleMots);
        jeu.lMotDevine.setWrap(true);					//Permet de mettre le texte sur plusieurs lignes si n�cessaire
        jeu.lMotDevine.setAlignment(Align.center);		//Aligne horizontalement
        jeu.affichagePendu = new Image(jeu.getImagesPendu()[0]);

        table.pad(3);
        celluleMotDevine = table.add(jeu.lMotDevine).align(Align.center).width(jeu.getLargeurEcran()) ;	//Le mot est centr� sur l'�cran
        table.row();    //Indique que l'�l�ment suivant sera sur une ligne suppl�mentaire
        table.add(jeu.affichagePendu);
        table.row();    //Indique que l'�l�ment suivant sera sur une ligne suppl�mentaire

        //D�finit la table qui contient les boutons des lettres � proposer
        tLettres = new Table();
        celluleLettres = table.add(tLettres).width(jeu.getLargeurEcran());

        i = 0;
        gestionnaireDeClics = new GestionnaireDeClics(jeu);
        for (lettre = 'A'; lettre <= 'Z'; lettre++) {
            TextButton bouton = new TextButton(Character.toString(lettre), jeu.getSkin());
            if (i % jeu.getNbLettresParLigne() == 0) {
                tLettres.row(); //On passe � la ligne suivante
            }
            i++;
            bouton.addListener(gestionnaireDeClics);
            tLettres.add(bouton).minWidth(jeu.LARGEUR_MIN_BOUTONS_LETTRES).maxWidth(jeu.LARGEUR_MAX_BOUTONS_LETTRES)
            					.minHeight(jeu.HAUTEUR_MIN_BOUTONS_LETTRES).maxHeight(jeu.HAUTEUR_MAX_BOUTONS_LETTRES);
        }
    }

    private void actualiseUI() {
        jeu.lMotDevine.setText(SepareParDesEspaces(jeu.motDevine));									//R�-initialise le texte du mot � deviner
    	jeu.affichagePendu.setDrawable(new SpriteDrawable(new Sprite(jeu.getImagesPendu()[0])));	//Remet l'image du pendu sur la premi�re image
    	
    	//Rend tous les acteurs visibles : cela permet de faire en sorte que toutes les lettres choisies pr�c�demment r�-apparaissent
    	Iterator<Cell> i = tLettres.getCells().iterator() ;
    	while (i.hasNext()) {
    		Actor acteur = i.next().getActor();
    		acteur.setVisible(true) ;
		}
    }
    
    
    public void gagne(Pendu jeu) {

        if (jeu.getDebugState()) {
            System.out.println("Gagn� !");
        }
        jeu.lMotDevine.setText(SepareParDesEspaces(jeu.motDevine));
        
        jeu.score++ ;

        //Ici on est oblig� de passer par une t�che en parall�le, sinon l'actualisation de l'�cran ne se fait pas et on ne pourrais pas voir le mot compl�t�
        Timer.Task affichageGagne; //Contiendra le code � ex�cuter pour afficher l'image de fin apr�s un d�lai
        
        affichageGagne = new Timer.Task(){
        	@Override
        	public void run() {
        		BasculeVersEcranGagne() ;	//On a aucun acc�s au jeu, donc on va appeler une m�thode statique de cette classe qui elle aura acc�s aux donn�es du jeu
        	}
        } ;
        
        schedule(affichageGagne,jeu.DUREE_AFFICHAGE_GAGNE) ;
    }

    public static void BasculeVersEcranGagne() {
    	if (Pendu.getEcranGagne() == null) {
            jeu.setScreen(new EcranGagne(jeu));	//Bascule sur l'�cran de victoire
    	}
    	else jeu.setScreen(Pendu.getEcranGagne());
    }
    
    
    public void RetourMenu() {
    	if (jeu.getEcranAccueil() != null) {
    		jeu.setScreen(jeu.getEcranAccueil());	//Bascule sur l'�cran d'accueil
    	}
    	else {
    		jeu.setScreen(new EcranAccueil(jeu));	//Bascule sur l'�cran d'accueil
    	}
    }
    
    public void perdu(Pendu jeu) {
    	if (jeu.getEcranPerdu() == null) {
            jeu.setScreen(new EcranPerdu(jeu));	//Bascule sur l'�cran de game over
    	}
    	else jeu.setScreen(jeu.getEcranPerdu());
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    	jeu.setHauteurEcran(height) ;
    	jeu.setLargeurEcran(width) ;
    	if (jeu.getDebugState()) Gdx.app.log("Redimmensionnement vers ",width+" x "+height);
    	
    	if (table != null) {
    		celluleLettres.width(width);
    		celluleMotDevine.width(width) ;
    	}
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.app.log("EcranJeu","show");
        Gdx.input.setInputProcessor(stage);
        
        init(jeu);
        actualiseUI(); //actualisation de la fen�tre de jeu
        if (jeu.getDebugState()) {
            System.out.println("D�but de partie");
        }

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    
    @Override
    public void dispose() {
    }
    
}
