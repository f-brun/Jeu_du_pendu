package fr.jeux.pendu.screens;

import static com.badlogic.gdx.utils.Timer.schedule;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import fr.jeux.pendu.GestionClavier;
import fr.jeux.pendu.Highscore;
import fr.jeux.pendu.Pendu;
/**
 * Ecran qui s'affiche lorsque l'on a perdu. Affiche la bonne r�ponse et d�termine si le score atteint est dans les highscores.
 * Si c'est le cas, on affiche un dialogue pour recueillir le nom du joueur et on bascule sur l'�cran des highscores. Sinon on revient � l'�cran d'accueil 
 * @author Florent Brun
 *
 */
public class EcranPerdu implements Screen {
    public static final float[][] TAILLES_POLICE_ADAPTEES = {{600,  400,  300,  200,  100,    0},
			 												{   3, 2.5f,   2f, 1.5f,   1f, 0.5f}};

    public static final int LARGEUR_DIALOG_PREFEREE = 600 ;
    public static final int HAUTEUR_DIALOG_PREFEREE = 350 ;
    public static final float POSITION_Y_DIALOG_HIGHSCORE = 0.9f ;	//Position verticale du dialog des highscores en % de l'�cran (afficher en haut pour laisser la place d'afficher le clavier virtuel sur mobile)
    public static final float POSITION_Y_DIALOG_FELICITATIONS = 0.5f ;	//Position verticale du dialog de f�licitation
    public static final float[][] TAILLES_POLICE_ADAPTEES_DIALOGUE = {{500,  450,  400,  300,  200,  100,    0},
																	 {   2, 1.8f, 1.5f,   1f, 0.8f, 0.5f, 0.4f}};
    public static final float PROPORTION_IMAGE_PERDU = 0.7f ;

    public static final String[] POSITION = {"premier", "deuxieme", "troisieme", "quatrieme", "cinquieme", "sixieme", "septieme", "huitieme", "neuvieme", "dixieme",
    								"onzieme", "douzieme", "treizieme", "quatorzieme", "quinzieme", "seizieme", "dix-septieme", "dix-huitieme", "dix-neuvieme", "vingtieme"} ;

    static final float DUREE_AFFICHAGE_PERDU = 1 ;	//Temps d'attente avant de pouvoir passer � la suite
    static final float DUREE_AFFICHAGE_FELICITATIONS = 2f ;	//Temps d'affichage de l'image de f�licitation
    
    private static Stage stage = null ;
    private static Table table = null ;  //Table contenant les labels

    private static Label    lAffichageScore = null ;
    private static Texture  texturePerdu = null ;    //Texture de l'image perdu !
    private static Texture  textureFelicitations = null ;   //Texture de l'image de f�licitation
    private static Image    imagePerdu = null ;
    private static Image    imageFelicitations = null ;
    private static Label    lTexteATrouver = null ;
    
    public static Dialog dialogHighscore ;	//Fen�tre de dialogue pour informer qu'un highscore vient d'etre realise
    public static Dialog dialogFelicitations ;	//Fen�tre de dialogue pour afficher l'image de f�licitation
	public static TextField	saisieNom ;	//Textfield pour la saisie du nom du joueur
	Label		lHighscore ;
	Cell<Label>	celluleTexteHighscore ;

    
    Cell<Label>	celluleTexteATrouver ;

    Pendu jeu ;	//r�f�rence aux donn�es du jeu
    
    public EcranPerdu(Pendu jeuEnCours) {

    	jeu = jeuEnCours ;	//reprend la r�f�rence au jeu pour toutes les m�thodes de la classe
 
    	if (Pendu.getEcranPerdu() == null) Pendu.setEcranPerdu(this); 	//Ecrit la r�f�rence � l'�cran que l'on vient de cr�er
    	
    	if (stage == null) creeUI() ; //Si c'est le premier appel, on cr�e l'affichage
    }
    
    private void creeUI() {

       	stage = new Stage(new ScreenViewport());

        //Cr�e l'affichage du score final
       	lAffichageScore = new Label("Score final : "+ Integer.toString(Pendu.getScore()),Pendu.getSkin()) ;

        //Cr�e le label contenant l'image
       	texturePerdu = new Texture(Pendu.IMAGE_PERDU);
       	imagePerdu = new Image(texturePerdu);
       	imagePerdu.setScaling(Scaling.fit); //Pour faire en sorte de conserver l'ascpect ratio

        //Cr�e le label contenant le mot � trouver
        lTexteATrouver = new Label("",Pendu.skin) ;
        lTexteATrouver.setWrap(true);
        lTexteATrouver.setAlignment(Align.center);
        
        table = new Table() ;

        //D�finit la disposition de la table
        table.pad(Pendu.getHauteurEcran()/40) ;
        if (Pendu.getDebugState()) table.setDebug(true); // This is optional, but enables debug lines for tables.
        table.setFillParent(true);  //La table occupe tout l'�cran
        table.add(lAffichageScore) ;
        table.row();
        table.add(imagePerdu).maxHeight(Pendu.getHauteurEcran()*PROPORTION_IMAGE_PERDU);    //Ajoute l'image en limitant sa taille verticale pour avoir la place de voir les textes
        table.row() ;    //Place le texte en dessous de l'image
        celluleTexteATrouver = table.add(lTexteATrouver).align(Align.center).width(Pendu.getLargeurEcran()) ;
        table.setVisible(true) ;

        stage.addActor(table) ;
        
        imagePerdu.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (Pendu.logger != null) Pendu.logger.ecritLog("Niv " + Pendu.score.niveau + ";Joueur:" + Pendu.score.joueur + ";Score:" + Pendu.score.score + ";Mots devines:" + Pendu.score.nbMotsDevines
                		+ ";Temps:" + Pendu.score.temps + ";Dico:" + Pendu.dictionnaires.getDictionnaireActuel().getLangue());	//On inscrit le score dans le log            		Pendu.ecranPerdu.retourMenu() ;
                Pendu.ecranPerdu.retourMenu() ;
            }
        });
    	
    }

   
    public void retourMenu() {
    	if (Pendu.getEcranAccueil() != null) {
    		jeu.setScreen(Pendu.getEcranAccueil());	//Bascule sur l'�cran d'accueil
    	}
    	else {
    		jeu.setScreen(new EcranAccueil(jeu));	//Bascule sur l'�cran d'accueil
    	}
    }

    public void highscoreObtenu() {
    	if (Pendu.getEcranHighscores() != null) {
    		jeu.setScreen(Pendu.getEcranHighscores());	//Bascule sur l'�cran d'accueil
    	}
    	else {
    		jeu.setScreen(new EcranHighscores(jeu));	//Bascule sur l'�cran d'accueil
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
   		lAffichageScore.setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getHauteurEcran(),TAILLES_POLICE_ADAPTEES));	//Adapte la taille de la police � la hauteur de l'affichage
   		lTexteATrouver.setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getHauteurEcran(),TAILLES_POLICE_ADAPTEES));	//Adapte la taille de la police � la hauteur de l'affichage
   		if (dialogHighscore != null) dimensionneDialogHighscore() ;
        if (dialogFelicitations != null) dimmensionneDialogFelicitations() ;

        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        InputMultiplexer im ;

    	imagePerdu.setTouchable(Touchable.disabled) ;
    	if (Pendu.getDebugState()) Gdx.app.log("INFO","EcranPerdu - show");
    	if (stage == null) creeUI() ; //Si c'est le premier appel, on cr�e l'affichage
    	actualiseUI() ;

        im = new InputMultiplexer() ;
        im.addProcessor(stage);
        im.addProcessor(new GestionClavier(new GestionClavier.EcouteClavier(){
            public void toucheGAUCHE() { } ;
            public void toucheDROITE() { } ;
            public void toucheESCAPE() { Pendu.getEcranPerdu().retourMenu() ; } ;
        }));
        Gdx.input.setInputProcessor(im) ;
    	
        //On �crit les infos pour le deboggage
        if (Pendu.getDebugState()) Gdx.app.log("INFO","Duree de la partie : " + Pendu.score.temps) ;
        Pendu.position = Pendu.highscores.getHighscoreActuel().insereScore(Pendu.score) ;	//Puis on determine la position dans les highscores
        
        if (Pendu.getDebugState()) Gdx.app.log("INFO","Score : "+Pendu.score.score) ;
    	if (Pendu.getDebugState() && Pendu.position >= 0) Gdx.app.log("INFO"," ("+POSITION[Pendu.position]+" dans les highscores)");
        
        Timer.Task attenteFinie; //Contiendra le code a ex�cuter pour afficher l'image de fin apr�s un d�lai
        
        attenteFinie = new Timer.Task(){
        	@Override
        	public void run() {
            	if (Pendu.position >= 0) {			//Si on a un record
            		afficheFelicitations() ;
            		Timer.Task attenteFelicitations = new Timer.Task() {
            			@Override
            			public void run() {
                            dialogFelicitations.remove() ;	//Ferme le dialog pr�c�dent (avec l'image de f�licitation)
            				afficheDialogueHighscore() ;	//On affiche le dialogue pour r�cup�rer le nom
            			}
            		} ;
            		schedule(attenteFelicitations,DUREE_AFFICHAGE_FELICITATIONS) ;
            		
            	}
            	else imagePerdu.setTouchable(Touchable.enabled) ;	//Sinon on autorise la sortie de l'�cran par l'image
        	}
        } ;
        schedule(attenteFinie,DUREE_AFFICHAGE_PERDU) ;
    }
    
    public void afficheFelicitations() {
    	dialogFelicitations = new Dialog("Felicitations !", Pendu.getSkin()) ;
        textureFelicitations = new Texture(Pendu.IMAGE_HIGHSCORE) ;
        imageFelicitations = new Image(textureFelicitations) ;
        imageFelicitations.setScaling(Scaling.fit);
        dialogFelicitations.add(imageFelicitations);
        dialogFelicitations.show(stage) ;	//Affiche la fenetre de dialogue
        dimmensionneDialogFelicitations() ;
    }

    private void dimmensionneDialogFelicitations() {
        float ratio = imageFelicitations.getWidth()/imageFelicitations.getHeight() ;    //D�termine le ratio d'aspect de l'image
        if (Pendu.getHauteurEcran() < Pendu.getLargeurEcran()/ratio) {  //Si l'�cran est trop peu haut
            dialogFelicitations.setSize(Pendu.getHauteurEcran()*ratio,Pendu.getHauteurEcran());      //On dimensionne en fonction de la hauteur
        }
        else dialogFelicitations.setSize(Pendu.getLargeurEcran(),Pendu.getLargeurEcran()/ratio );   //Sinon on dimensionne en fonction de la largeur
        dialogFelicitations.setPosition((Pendu.getLargeurEcran()-dialogFelicitations.getWidth())/2,((Pendu.getHauteurEcran()-dialogFelicitations.getHeight())*POSITION_Y_DIALOG_FELICITATIONS));
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
            	Pendu.config.setValeurCle(Pendu.CLE_JOUEUR, saisieNom.getText() ) ;		//On enregistre dans la config persistante pour que �a reste pour les parties suivantes
            	Pendu.highscores.getHighscoreActuel().setHighscore(Pendu.position, Pendu.score) ;	//Inscrit le score dans la table des highscores
            	Pendu.highscores.getHighscoreActuel().ecritScores(); 	//et on sauvegarde les scores
                if (Pendu.logger != null) Pendu.logger.ecritLog("Niv " + Pendu.score.niveau + ";Joueur:" + Pendu.score.joueur + ";Score:" + Pendu.score.score + ";Mots devines:" + Pendu.score.nbMotsDevines
                		+ ";Temps:" + Pendu.score.temps + ";Dico:" + Pendu.dictionnaires.getDictionnaireActuel().getLangue());	//On inscrit le score dans le log
                Pendu.ecranPerdu.retourMenu() ;
            	Pendu.ecranPerdu.highscoreObtenu() ;	//puis on bascule sur l'�cran des highscores
            }
        };
        dialogHighscore.button(boutonOK) ;	//On rajoute le bouton OK
        dialogHighscore.text(lHighscore) ;	//Le texte d'information
        
        celluleTexteHighscore = dialogHighscore.getContentTable().getCell(lHighscore) ;
        dialogHighscore.getContentTable().row() ;
        dialogHighscore.getContentTable().add(saisieNom) ;	//Et la zone de saisie du nom du joueur
        dialogHighscore.setMovable(true);
        dialogHighscore.setResizable(false);
        
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
   		lHighscore.setFontScale(jeu.getTaillePoliceTitreAdaptee(Math.min(largDialog, hautDialog*2f),TAILLES_POLICE_ADAPTEES_DIALOGUE));	//Adapte la taille de la police � la hauteur de l'affichage
   		celluleTexteHighscore.width(largDialog-8) ;
		dialogHighscore.setSize(largDialog,hautDialog) ;
		dialogHighscore.setPosition((Pendu.getLargeurEcran()-dialogHighscore.getWidth())/2,((Pendu.getHauteurEcran()-dialogHighscore.getHeight())*POSITION_Y_DIALOG_HIGHSCORE));
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
     * Elimine les r�f�rences statiques aux objets car si l'application est relanc�e juste apr�s �tre quitt�e, toutes les r�f�rences statiques
     * demeurent alors que les objets (�cran, widgets... ) sont eux d�truits
     */
    @Override
    public void dispose() {
        if (Pendu.DEBUG) Gdx.app.log("INFO","Suppression des references de l'ecran perdu") ;
    	Pendu.setEcranPerdu(null) ;	//Supprime la r�f�rence � l'�cran pour l'obliger � �tre re-cr�e la prochaine fois
        if (texturePerdu != null) texturePerdu.dispose() ;
        if (textureFelicitations != null) textureFelicitations.dispose() ;
        if (stage != null) stage.dispose();
    }
}
