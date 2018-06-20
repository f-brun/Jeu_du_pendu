package fr.jeux.pendu.screens;

import static com.badlogic.gdx.utils.Timer.schedule;

import com.badlogic.gdx.Gdx;
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

import fr.jeux.pendu.Highscore;
import fr.jeux.pendu.Pendu;
import fr.jeux.pendu.Score;

public class EcranPerdu implements Screen {
    public static final float[][] TAILLES_POLICE_ADAPTEES = {{600,  400,  300,  200,  100,    0},
			 												{   3, 2.5f,   2f, 1.5f,   1f, 0.5f}};

    public static final float[][] TAILLES_POLICE_ADAPTEES_DIALOGUE = {{800,  600,  400,  300,  200,  100,    0},
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
    
    public static int position ; 	//Position dans la liste des highscores à l'issu du jeu
    public static Dialog dialogHighscore ;	//Fenetre de dialogue pour informer qu'un highscore vient d'etre realise
	public static TextField	saisieNom ;	//Textfield pour la saisie du nom du joueur
	Label		lHighscore ;
	Cell<Label>	celluleTexteHighscore ;

    
    Cell<Label>	celluleTexteATrouver ;

    Pendu jeu ;	//référence aux données du jeu
    
    public EcranPerdu(Pendu jeuEnCours) {

    	jeu = jeuEnCours ;	//reprend la référence au jeu pour toutes les méthodes de la classe
 
    	if (Pendu.getEcranPerdu() == null) jeu.setEcranPerdu(this); 	//Ecrit la référence à l'écran que l'on vient de créer
    	
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
            	if (EcranPerdu.position == -1) 	Pendu.ecranPerdu.retourMenu() ;
            	else Pendu.ecranPerdu.highscoreObtenu() ;
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
   		if (lHighscore != null)	lHighscore.setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getLargeurEcran(),TAILLES_POLICE_ADAPTEES_DIALOGUE));	//Adapte la taille de la police à la largeur de l'affichage
   		if (dialogHighscore != null) {
   			dialogHighscore.setSize(Pendu.getLargeurEcran()*0.8f,Pendu.getHauteurEcran()*0.7f) ;
   			dialogHighscore.setPosition(Pendu.getLargeurEcran()*0.1f,Pendu.getHauteurEcran()*0.15f);
   		}
   		
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
    	if (Pendu.getDebugState()) Gdx.app.log("INFO","EcranPerdu - show");
    	
    	imagePerdu.setTouchable(Touchable.disabled) ;
        Gdx.input.setInputProcessor(stage);
        
    	if (stage == null) creeUI() ; //Si c'est le premier appel, on crée l'affichage
    	actualiseUI() ;

        long temps = Pendu.chrono.stop() ;
        
        //On écrit les infos pour le deboggage
        if (Pendu.getDebugState()) Gdx.app.log("INFO","Duree de la partie : " + temps) ;
        Pendu.logger.ecritLog("Florent", Pendu.score.score, Pendu.getNiveau().numero, temps, Pendu.dictionnaire.getLangue());
        
        Pendu.score.temps = temps ;		        //On met à jour le temps écoulé
        position = Pendu.highscore.proposeScore(Pendu.score) ;	//Puis on determine la position dans les highscores
        
        if (Pendu.getDebugState()) Gdx.app.log("INFO","Score : "+Pendu.score.score) ;
    	if (Pendu.getDebugState() && position >= 0) Gdx.app.log("INFO"," ("+POSITION[position]+" dans les highscores)");
        
        Timer.Task attenteFinie; //Contiendra le code a exécuter pour afficher l'image de fin après un délai
        
        attenteFinie = new Timer.Task(){
        	@Override
        	public void run() {
            	imagePerdu.setTouchable(Touchable.enabled) ;
            	if (EcranPerdu.position >= 0) {
            		afficheDialogueHighscore() ;
            	}
        	}
        } ;
        schedule(attenteFinie,DUREE_AFFICHAGE_PERDU) ;
    }
    
    public void afficheDialogueHighscore() {
    	TextButton	boutonOK ;
    	
    	boutonOK = new TextButton("OK",Pendu.getSkin()) ;
    	lHighscore = new Label("Vous avez realise une super partie et vous etes "+POSITION[position]+
    		" dans les highscores. Vous pouvez changez votre nom ci-dessous pour enregistrer votre record.", Pendu.getSkin()) ;
    	lHighscore.setWrap(true);
    	lHighscore.setAlignment(Align.center); 
   		lHighscore.setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getLargeurEcran(),TAILLES_POLICE_ADAPTEES_DIALOGUE));	//Adapte la taille de la police à la hauteur de l'affichage
   		lHighscore.setFillParent(true);
   		
   		saisieNom = new TextField(Pendu.score.joueur, Pendu.getSkin()) ;
   		saisieNom.setMaxLength(Highscore.LONGUEUR_MAX_NOM_JOUEUR);

    	dialogHighscore = new Dialog("Felicitations !", Pendu.getSkin())
        {
            protected void result(Object object)
            {
            	Pendu.score.joueur = saisieNom.getText() ;
            	Pendu.highscore.getMeilleursScores()[EcranPerdu.position].joueur = Pendu.score.joueur ;	//On change le nom du joueur
            	Pendu.highscore.ecritScores(); 	//et on sauvegarde les scores
            	Pendu.ecranPerdu.highscoreObtenu() ;	//puis on bascule sur l'écran des highscores
            };
        };
        dialogHighscore.button(boutonOK) ;	//On rajoute le bouton OK


        dialogHighscore.text(lHighscore) ;	//Le texte d'information
        
        celluleTexteHighscore = dialogHighscore.getContentTable().getCell(lHighscore).align(Align.left).expand().width(Pendu.getLargeurEcran()*0.75f) ;
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
        dialogHighscore.setSize(Pendu.getLargeurEcran()*0.8f,Pendu.getHauteurEcran()*0.7f);
		dialogHighscore.setPosition(Pendu.getLargeurEcran()*0.1f,Pendu.getHauteurEcran()*0.15f);

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
    	jeu.setEcranPerdu(null) ;	//Supprime la référence à l'écran pour l'obliger à être re-crée la prochaine fois
    	stage.dispose();
    }
}
