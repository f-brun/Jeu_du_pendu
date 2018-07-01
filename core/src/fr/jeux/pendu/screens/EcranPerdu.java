package fr.jeux.pendu.screens;

import static com.badlogic.gdx.utils.Timer.schedule;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import fr.jeux.pendu.GestionClavier;
import fr.jeux.pendu.Highscore;
import fr.jeux.pendu.Pendu;

public class EcranPerdu implements Screen {
    public static final float[][] TAILLES_POLICE_ADAPTEES = {{600,  400,  300,  200,  100,    0},
			 												{   3, 2.5f,   2f, 1.5f,   1f, 0.5f}};

    public static final int LARGEUR_DIALOG_PREFEREE = 600 ;
    public static final int HAUTEUR_DIALOG_PREFEREE = 350 ;
    public static final float[][] TAILLES_POLICE_ADAPTEES_DIALOGUE = {{500,  450,  400,  300,  200,  100,    0},
																	 {   2, 1.8f, 1.5f,   1f, 0.8f, 0.5f, 0.4f}};

    public static final String[] POSITION = {"premier", "deuxieme", "troisieme", "quatrieme", "cinquieme", "sixieme", "septieme", "huitieme", "neuvieme", "dixieme",
    								"onzieme", "douzieme", "treizieme", "quatorzieme", "quinzieme", "seizieme", "dix-septieme", "dix-huitieme", "dix-neuvieme", "vingtieme"} ;

    static final float DUREE_AFFICHAGE_PERDU = 1 ;	//Temps d'attente avant de pouvoir passer à la suite
    
    private static Stage stage = null ;
    private static Table table = null ;  //Table contenant les labels

    private static Label lAffichageScore = null ;
    private static Texture img = null ; 
    private static Image imagePerdu = null ;
    private static Label lTexteATrouver = null ;
    
    public static Dialog dialogHighscore ;	//Fenetre de dialogue pour informer qu'un highscore vient d'etre realise
	public static TextField	saisieNom ;	//Textfield pour la saisie du nom du joueur
	Label		lHighscore ;
	Cell<Label>	celluleTexteHighscore ;

    
    Cell<Label>	celluleTexteATrouver ;

    Pendu jeu ;	//référence aux données du jeu
    
    public EcranPerdu(Pendu jeuEnCours) {

    	jeu = jeuEnCours ;	//reprend la référence au jeu pour toutes les méthodes de la classe
 
    	if (Pendu.getEcranPerdu() == null) Pendu.setEcranPerdu(this); 	//Ecrit la référence à l'écran que l'on vient de créer
    	
    	if (stage == null) creeUI() ; //Si c'est le premier appel, on crée l'affichage
    }
    
    private void creeUI() {

       	stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);		//Met le focus sur notre écran
        
        if (Pendu.getDebugState()) {
            System.out.println("Perdu !");
        }

        //Crée l'affichage du score final
       	lAffichageScore = new Label("Score final : "+ Integer.toString(Pendu.getScore()),Pendu.getSkin()) ;
        

        //Crée le label contenant l'image
       	img = new Texture(Pendu.IMAGE_PERDU);
       	imagePerdu = new Image(img);

        //Crée le label contenant le mot à trouver
        lTexteATrouver = new Label("",Pendu.skin) ;
        lTexteATrouver.setWrap(true);
        lTexteATrouver.setAlignment(Align.center);
        
        table = new Table() ;

        //Définit la disposition de la table
        table.pad(3) ;
        if (Pendu.getDebugState()) table.setDebug(true); // This is optional, but enables debug lines for tables.
        table.setFillParent(true);  //La table occupe tout l'écran
        table.add(lAffichageScore) ;
        table.row();
        table.add(imagePerdu);
        table.row() ;    //Place le texte en dessous de l'image
        celluleTexteATrouver = table.add(lTexteATrouver).align(Align.center).width(Pendu.getLargeurEcran()) ;
        table.setVisible(true) ;

        stage.addActor(table) ;
        
        imagePerdu.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (Pendu.logger != null) Pendu.logger.ecritLog(Pendu.score.niveau, Pendu.score.joueur, Pendu.score.score, Pendu.score.nbMotsDevines, Pendu.score.temps, Pendu.dictionnaires.getDictionnaireActuel().getLangue());	//On inscrit le score dans le log            		Pendu.ecranPerdu.retourMenu() ;
                Pendu.ecranPerdu.retourMenu() ;
            }
        });
    	
    }

   
    public void retourMenu() {
    	if (Pendu.getEcranAccueil() != null) {
    		jeu.setScreen(Pendu.getEcranAccueil());	//Bascule sur l'écran d'accueil
    	}
    	else {
    		jeu.setScreen(new EcranAccueil(jeu));	//Bascule sur l'écran d'accueil
    	}
    }

    public void highscoreObtenu() {
    	if (Pendu.getEcranHighscores() != null) {
    		jeu.setScreen(Pendu.getEcranHighscores());	//Bascule sur l'écran d'accueil
    	}
    	else {
    		jeu.setScreen(new EcranHighscores(jeu));	//Bascule sur l'écran d'accueil
    	}
    }
    
    private void actualiseUI() {
        lAffichageScore.setText("Score final : "+ Integer.toString(Pendu.getScore()));
        lTexteATrouver.setText("Il fallait trouver :\n"+Pendu.motADeviner);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void resize(int width, int height) {
    	Pendu.setHauteurEcran(height) ;
    	Pendu.setLargeurEcran(width) ;
    	if (Pendu.getDebugState()) Gdx.app.log("INFO","Redimmensionnement vers "+width+" x "+height);
    	
   		celluleTexteATrouver.width(width) ;
   		lAffichageScore.setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getHauteurEcran(),TAILLES_POLICE_ADAPTEES));	//Adapte la taille de la police à la hauteur de l'affichage
   		lTexteATrouver.setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getHauteurEcran(),TAILLES_POLICE_ADAPTEES));	//Adapte la taille de la police à la hauteur de l'affichage
   		if (dialogHighscore != null) dimensionneDialogHighscore() ;
   		
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        InputMultiplexer im ;

    	imagePerdu.setTouchable(Touchable.disabled) ;
    	if (Pendu.getDebugState()) Gdx.app.log("INFO","EcranPerdu - show");
    	if (stage == null) creeUI() ; //Si c'est le premier appel, on crée l'affichage
    	actualiseUI() ;

        im = new InputMultiplexer() ;
        im.addProcessor(stage);
        im.addProcessor(new GestionClavier(new GestionClavier.EcouteClavier(){
            public void toucheGAUCHE() { } ;
            public void toucheDROITE() { } ;
            public void toucheESCAPE() { Pendu.getEcranPerdu().retourMenu() ; } ;
        }));
        Gdx.input.setInputProcessor(im) ;
    	
        //On écrit les infos pour le deboggage
        if (Pendu.getDebugState()) Gdx.app.log("INFO","Duree de la partie : " + Pendu.score.temps) ;
        Pendu.position = Pendu.highscores.getHighscoreActuel().insereScore(Pendu.score) ;	//Puis on determine la position dans les highscores
        
        if (Pendu.getDebugState()) Gdx.app.log("INFO","Score : "+Pendu.score.score) ;
    	if (Pendu.getDebugState() && Pendu.position >= 0) Gdx.app.log("INFO"," ("+POSITION[Pendu.position]+" dans les highscores)");
        
        Timer.Task attenteFinie; //Contiendra le code a exécuter pour afficher l'image de fin après un délai
        
        attenteFinie = new Timer.Task(){
        	@Override
        	public void run() {
            	if (Pendu.position >= 0) {			//Si on a un record
            		afficheDialogueHighscore() ;	//On affiche le dialogue pour récupérer le nom
            	}
            	else imagePerdu.setTouchable(Touchable.enabled) ;	//Sinon on autorise la sortie de l'écran par l'image
        	}
        } ;
        schedule(attenteFinie,DUREE_AFFICHAGE_PERDU) ;
    }
    
    public void afficheDialogueHighscore() {
    	TextButton	boutonOK ;
    	
    	boutonOK = new TextButton("OK",Pendu.getSkin()) ;
    	lHighscore = new Label("Vous avez realise une super partie et vous etes "+POSITION[Pendu.position]+
    		" dans les highscores. Vous pouvez changez votre nom ci-dessous pour enregistrer votre record.", Pendu.getSkin()) ;
    	lHighscore.setWrap(true);
    	lHighscore.setAlignment(Align.center); 
   		
   		saisieNom = new TextField(Pendu.score.joueur, Pendu.getSkin()) ;
   		saisieNom.setMaxLength(Highscore.LONGUEUR_MAX_NOM_JOUEUR);

    	dialogHighscore = new Dialog("Felicitations !", Pendu.getSkin())
        {
            protected void result(Object object)
            {
            	Pendu.score.joueur = saisieNom.getText() ;	//On change le nom du joueur
            	Pendu.highscores.getHighscoreActuel().setHighscore(Pendu.position, Pendu.score) ;	//Inscrit le score dans la table des highscores
            	Pendu.highscores.getHighscoreActuel().ecritScores(); 	//et on sauvegarde les scores
                if (Pendu.logger != null) Pendu.logger.ecritLog(Pendu.score.niveau, Pendu.score.joueur, Pendu.score.score, Pendu.score.nbMotsDevines, Pendu.score.temps, Pendu.dictionnaires.getDictionnaireActuel().getLangue());	//On inscrit le score dans le log
            	Pendu.ecranPerdu.highscoreObtenu() ;	//puis on bascule sur l'écran des highscores
            }
        };
        dialogHighscore.button(boutonOK) ;	//On rajoute le bouton OK
        dialogHighscore.text(lHighscore) ;	//Le texte d'information
        
        celluleTexteHighscore = dialogHighscore.getContentTable().getCell(lHighscore) ;
        dialogHighscore.getContentTable().row() ;
        dialogHighscore.getContentTable().add(saisieNom) ;	//Et la zone de saisie du nom du joueur
        dialogHighscore.setMovable(true);
        dialogHighscore.setResizable(true);
        dialogHighscore.setResizeBorder(8);

        if (Pendu.getDebugState()) {
        	dialogHighscore.setDebug(true) ; // This is optional, but enables debug lines for tables.
        	dialogHighscore.getContentTable().setDebug(true) ;
        }

        dialogHighscore.show(stage) ;	//Affiche la fenetre de dialogue
        dimensionneDialogHighscore() ;
    }

    private void dimensionneDialogHighscore() {
        int largDialog = LARGEUR_DIALOG_PREFEREE ;
        int hautDialog = HAUTEUR_DIALOG_PREFEREE ;
        if ( Pendu.getLargeurEcran() < largDialog) largDialog = (int)( Pendu.getLargeurEcran()*0.9f) ;
        if ( Pendu.getHauteurEcran() < hautDialog) hautDialog = (int)( Pendu.getHauteurEcran()*0.9f) ;
   		lHighscore.setFontScale(jeu.getTaillePoliceTitreAdaptee(Math.min(largDialog, hautDialog*2f),TAILLES_POLICE_ADAPTEES_DIALOGUE));	//Adapte la taille de la police à la hauteur de l'affichage
   		celluleTexteHighscore.width(largDialog-8) ;
		dialogHighscore.setSize(largDialog,hautDialog) ;
		dialogHighscore.setPosition((Pendu.getLargeurEcran()-dialogHighscore.getWidth())/2,(Pendu.getHauteurEcran()-dialogHighscore.getHeight())/2);
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
        if (Pendu.DEBUG) Gdx.app.log("INFO","Suppression des references de l'ecran perdu") ;
    	Pendu.setEcranPerdu(null) ;	//Supprime la référence à l'écran pour l'obliger à être re-crée la prochaine fois
        if (stage != null) stage.dispose();
    }
}
