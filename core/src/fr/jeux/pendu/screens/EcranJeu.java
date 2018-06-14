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
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import fr.jeux.pendu.Pendu;
import fr.jeux.pendu.BarreMinuteur;
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
	
    public static final float[][] TAILLES_POLICE_ADAPTEES = {{600,  400,  300,  200,  100,   70,    0},
    														{   1, 0.8f, 0.7f, 0.5f, 0.3f, 0.2f, 0.1f}};
    public static final float[][] TAILLES_POLICE_INFOS_ADAPTEES = {{800,  600,  400,  300,  200,   100,    0},
    															  {1.5f, 1.2f, 1.0f, 0.8f, 0.5f, 0.2f, 0.1f}};

    static Pendu jeu ;	//référence aux données du jeu
	
	Stage	stage ;	//Conteneur général de l'écran
    Table	table;   //table principale sur laquelle s'affichent les éléments graphiques
    Texture img;   //Texture de l'image du pendu en cours ou de l'image de fin

    Table	tLettres;    //Element d'UI contenant les lettres
    Table	tInfos ;	//Table contenant les infos (nb lmots trouvés, image pendu, nb essais restants)
    Cell<Table>	celluleLettres ;	//Cellule contenant les lettres à cliquer (sert pour redimensionner la cellule)
    Cell<Table>	celluleInfos ;		//Cellule contenant les infos (nb lmots trouvés, image pendu, nb essais restants)
    Cell<Label>	celluleMotDevine ;	//Cellule contenant le texte du mot deviné jusqu'à là (sert pour redimensionner)
    Cell<BarreMinuteur>	celluleMinuteur ;	//Cellule contenant le minuteur

    public EcranJeu(Pendu jeuEnCours) {
    	jeu = jeuEnCours ;
    	
    	jeu.setEcranJeu(this); 	//Enregistre la référence de cet écran
    	
        creeUI(jeu); //Création de la fenêtre de jeu
    }

    void init(Pendu jeu) {
        jeu.nbErreurs = 0;
        jeu.motADeviner = jeu.dictionnaire.choisitMotAuHasard();
//Crée le mot deviné en remplaçant toutes les lettres par des sous-tirets
        jeu.motDevine = "";
        for (int i = 0; i < jeu.motADeviner.length(); i++) {
            if ((jeu.motADeviner.charAt(i) < 'A') || (jeu.motADeviner.charAt(i) > 'Z')) {   //Si c'est un caractère non-alphabétique, on le recopie (il est déjà donné)
                jeu.motDevine += jeu.motADeviner.charAt(i);
            } else {
                jeu.motDevine += "_";   //Si c'est un caractère alphabétique, on remplace par un sous-tiret
            }
        }
    }

    private void creeUI(Pendu jeu) {
        char lettre;
        GestionnaireDeClics gestionnaireDeClics;   //Fonction qui va gérer les clics sur les boutons de lettres
        int i;

        stage = new Stage(new ScreenViewport());
        table = new Table();
        table.setFillParent(true);  //La table occupe tout l'écran
        
        stage.addActor(table);

        if (jeu.getDebugState()) table.setDebug(true); // This is optional, but enables debug lines for tables.
        
        jeu.lMotDevine = new Label("", jeu.styleMots);
        jeu.lMotDevine.setWrap(true);					//Permet de mettre le texte sur plusieurs lignes si nécessaire
        jeu.lMotDevine.setAlignment(Align.center);		//Aligne horizontalement

        table.pad(3);
        celluleMotDevine = table.add(jeu.lMotDevine).align(Align.center).width(jeu.getLargeurEcran()) ;	//Le mot est centré sur l'écran

 
        tInfos = new Table() ;
        Pendu.lNbMotsDevinnes = new Label("Nombre de mots\ndevinnes :\n"+Pendu.nbMotsDevinnes,Pendu.getSkin()) ;
        Pendu.lNbMotsDevinnes.setAlignment(Align.center);
        jeu.affichagePendu = new Image(jeu.getImagesPendu()[0]) ;
        Pendu.lNbEssaisRestants = new Label("Nombre d'essais\nrestants :\n"+(Pendu.getNiveau().nbErreursMax - Pendu.getNbErreurs()), Pendu.getSkin()) ;
        Pendu.lNbEssaisRestants.setAlignment(Align.center);


        table.row();    //Indique que l'élément suivant sera sur une ligne supplémentaire
        tInfos.add(Pendu.lNbMotsDevinnes).maxWidth(jeu.getLargeurEcran()/4).space(8) ;
        tInfos.add(jeu.affichagePendu).maxWidth(jeu.getLargeurEcran()/2) ; //.align(Align.center) ;
        tInfos.add(Pendu.lNbEssaisRestants).maxWidth(jeu.getLargeurEcran()/4).space(8) ;
        celluleInfos = table.add(tInfos).width(jeu.getLargeurEcran()) ;	//.align(Align.center)
        
        table.row();    //Indique que l'élément suivant sera sur une ligne supplémentaire
        //Définit la table qui contient les boutons des lettres à proposer
        tLettres = new Table();
        celluleLettres = table.add(tLettres).width(jeu.getLargeurEcran());

        i = 0;
        gestionnaireDeClics = new GestionnaireDeClics(jeu);
        for (lettre = 'A'; lettre <= 'Z'; lettre++) {
            TextButton bouton = new TextButton(Character.toString(lettre), jeu.getSkin());
            if (i % jeu.getNbLettresParLigne() == 0) {
                tLettres.row(); //On passe à la ligne suivante
            }
            i++;
            bouton.addListener(gestionnaireDeClics);
            tLettres.add(bouton).minWidth(Pendu.LARGEUR_MIN_BOUTONS_LETTRES).maxWidth(Pendu.LARGEUR_MAX_BOUTONS_LETTRES)
            					.minHeight(Pendu.HAUTEUR_MIN_BOUTONS).maxHeight(Pendu.HAUTEUR_MAX_BOUTONS);
        }
        
        jeu.barreMinuteur = new BarreMinuteur(60f, 0.2f, 0.4f, 0.6f, false, jeu.getSkin()) ;	//Crée un minuteur horizontal
        table.row();
        celluleMinuteur = table.add(jeu.barreMinuteur).width(jeu.getLargeurEcran()-(jeu.getLargeurEcran()/100)) ;
       
    }

    private void actualiseUI() {
        jeu.lMotDevine.setText(SepareParDesEspaces(jeu.motDevine));									//Ré-initialise le texte du mot à deviner
    	jeu.affichagePendu.setDrawable(new SpriteDrawable(new Sprite(jeu.getImagesPendu()[0])));	//Remet l'image du pendu sur la première image
        Pendu.lNbEssaisRestants.setText("Nombre d'essais\nrestants :\n"+(Pendu.getNiveau().nbErreursMax - Pendu.getNbErreurs())) ;
    	
    	//Rend tous les acteurs visibles : cela permet de faire en sorte que toutes les lettres choisies précédemment ré-apparaissent
    	@SuppressWarnings("rawtypes")
		Iterator<Cell> i = tLettres.getCells().iterator() ;
    	while (i.hasNext()) {
    		Actor acteur = i.next().getActor();
    		acteur.setVisible(true) ;
		}

        if (jeu.getNiveau().minuteur) {
        	jeu.barreMinuteur.reset(jeu.niveau.dureeMinuteur, 0.2f, 0.4f, 0.6f);
        	jeu.barreMinuteur.setVisible(true); 
        	jeu.barreMinuteur.depart();
        }
        else {
        	jeu.barreMinuteur.setVisible(false);
        	jeu.barreMinuteur.reset();
        }
    }
    
    public void gagne(Pendu jeu) {

        if (jeu.getDebugState()) {
            System.out.println("Gagné !");
        }
        jeu.barreMinuteur.stop();
        jeu.lMotDevine.setText(SepareParDesEspaces(jeu.motDevine));

        jeu.score += jeu.getNiveau().calculeGain(jeu.barreMinuteur.getPercent()) ;	//On ajoute des points en fonction de la réussite du joueur
        jeu.nbMotsDevinnes++ ;
        
        Pendu.lNbMotsDevinnes.setText("Nombre de mots\ndevinnes :\n"+Pendu.nbMotsDevinnes);
        
        //Ici on est obligé de passer par une tâche en parallèle, sinon l'actualisation de l'écran ne se fait pas et on ne pourrait pas voir le mot complété
        Timer.Task affichageGagne; //Contiendra le code à exécuter pour afficher l'image de fin après un délai
        
        affichageGagne = new Timer.Task(){
        	@Override
        	public void run() {
        		basculeVersEcranGagne() ;	//On a aucun accès au jeu, donc on va appeler une méthode statique de cette classe qui elle aura accès aux données du jeu
        	}
        } ;
        
        schedule(affichageGagne,jeu.DUREE_AFFICHAGE_GAGNE) ;
    }

    public static void basculeVersEcranGagne() {
    	if (Pendu.getEcranGagne() == null) {
            jeu.setScreen(new EcranGagne(jeu));	//Bascule sur l'écran de victoire
    	}
    	else jeu.setScreen(Pendu.getEcranGagne());
    }
    
    public void perdu(Pendu jeu) {
    	jeu.barreMinuteur.stop();
    	if (jeu.getEcranPerdu() == null) {
            jeu.setScreen(new EcranPerdu(jeu));		//Bascule sur l'écran de game over
    	}
    	else jeu.setScreen(jeu.getEcranPerdu());
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        jeu.barreMinuteur.miseAJour() ;

        if ((jeu.getNiveau().minuteurFaitPerdre) && (jeu.barreMinuteur.getPercent() == 0) ) {	//Si le temps est écoulé et que cela fait perdre
            Timer.Task perduTempsEcoule ; //Contiendra le code à exécuter pour afficher l'image de fin après un délai
            
            perduTempsEcoule = new Timer.Task(){
            	@Override
            	public void run() {
            		basculeVersEcranPerdu() ;	//On a aucun accès au jeu, donc on va appeler une méthode statique de cette classe qui elle aura accès aux données du jeu
            	}
            } ;
            
            schedule(perduTempsEcoule,0.01f) ;	//On bascule sur perdu dans 10 ms, le temps de finir l'affichage
        }
        
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
    

    public static void basculeVersEcranPerdu() {
    	jeu.barreMinuteur.stop();
    	if (jeu.getEcranPerdu() == null) {
            jeu.setScreen(new EcranPerdu(jeu));		//Bascule sur l'écran de game over
    	}
    	else jeu.setScreen(jeu.getEcranPerdu());
    }

    
    @Override
    public void resize(int width, int height) {
    	jeu.setHauteurEcran(height) ;
    	
    	jeu.setLargeurEcran(width) ;
    	if (jeu.getDebugState()) Gdx.app.log("Redimmensionnement vers ",width+" x "+height);
    	
    	if (table != null) {
    		celluleLettres.width(width);
    		celluleInfos.width(width);
    		celluleMotDevine.width(width) ;
    		celluleMinuteur.width(width-(width/100)) ;
    	}
    	
    	jeu.lMotDevine.setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getHauteurEcran(),TAILLES_POLICE_ADAPTEES));	//Adapte la taille de la police à la hauteur de l'affichage

    	Pendu.lNbMotsDevinnes.setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getLargeurEcran(),TAILLES_POLICE_INFOS_ADAPTEES));	//Adapte la taille de la police à la largeur de l'affichage
    	Pendu.lNbEssaisRestants.setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getLargeurEcran(),TAILLES_POLICE_INFOS_ADAPTEES));	//Adapte la taille de la police à la largteur de l'affichage

        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
    	if (Pendu.getDebugState()) Gdx.app.log("INFO","EcranJeu - show") ;
        Gdx.input.setInputProcessor(stage);
        
        init(jeu);
        actualiseUI(); //actualisation de la fenêtre de jeu
        if (jeu.getDebugState()) Gdx.app.log("INFO","Début devine mot") ;

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

    /**
     * Elimine les références statiques aux objets car si l'application est relancée juste après être quittée, toutes les références statiques
     * demeurent alors que les objets (écran, widgets... ) sont eux détruits
     */
    @Override
    public void dispose() {
    	//Elimine dabord les références aux objets de l'écran
    	Pendu.lMotDevine = Pendu.lNbEssaisRestants = Pendu.lNbMotsDevinnes = null ;
    	//Puis la référence à l'écran lui-même
    	jeu.setEcranJeu(null) ;
    	img.dispose();
    	stage.dispose();
    }
    
}
