package fr.jeux.pendu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import fr.jeux.pendu.DetectionSwipe;
import fr.jeux.pendu.GestionClavier;
import fr.jeux.pendu.GestionClavier.EcouteClavier;
import fr.jeux.pendu.Highscore;
import fr.jeux.pendu.Pendu;
import fr.jeux.pendu.Score;

public class EcranHighscores implements Screen {
    public static final float[][] TAILLES_POLICE_ADAPTEES = {{600,  400,  300,  200,  100,    0},
			 												{2.5f,   2f, 1.5f,   1f, 0.7f, 0.5f}};
    public static final String[] POSITION = {"premier", "deuxieme", "troisieme", "quatrieme", "cinquieme", "sixieme", "septieme", "huitieme", "neuvieme", "dixieme",
    								"onzieme", "douzieme", "treizieme", "quatorzieme", "quinzieme", "seizieme", "dix-septieme", "dix-huitieme", "dix-neuvieme", "vingtieme"} ;

    static final int[] COL_HIGHSCORES = {0, Score.NOM_JOUEUR, Score.SCORE, Score.NB_MOTS_DEVINES, Score.TEMPS_HMS } ;
    static final String[] NOMS_COL = {"No", "Nom", "Score", "Mots", "Temps"} ;
    static final int[][] TAILLES_COLONNES = { {13,80}, {80,800}, {25,160}, {20,100}, {35,180} } ;	//Largeurs min et max des colonnes
    static final int ESPACEMENT_COLONNES = 10 ;		//Espacement entre les colonnes
     	
   	static final int[] ALIGNEMENTS_COL_HIGHSCORES = {Align.right, Align.left, Align.center, Align.center, Align.center} ;
    static final float[] LARGEUR_COLONNES_HIGHSCORES = {7f,50f,11f,10f,15f} ;	//Largeurs des colonnes de highscore en %
    static final float LARGEUR_MAX_HIGHSCORES = 0.95f ;		//Largeur maxi des highscores en % de la fenêtre
    static final float COEF_HIGHSCORES = 0.85f ;
    static final float COEF_SCORE_JOUEUR = 1.2f ;

    //Ces variables servent de communication avec les autres ecrans pour savoir quelles informations afficher
    public static int	noNiveauAAfficher ;
    public static int	noDicoHighscore ;



    private static Stage stage = null ;
    private static Table table = null ;  //Table contenant l'UI
    
    private static ScrollPane scrollpane = null ;
    
    private static Label	lTitre = null ;	//Titre de l'écran
    
    private static Table tScores = null ;	//Table contenant la liste des scores
    private static Label[] lColonnes = null ; 	//Labels des têtes de colonne
    private static Label[][] lScores = null ;	//Labels contenant les informations sur les highscores
    private static Cell<Label>[][]	clScores	= null ;
    private static Cell<ScrollPane> celluleTable = null ;

    private static TextButton boutonRetour = null ;

	public static Score[] highscore ;	//Stocke les scores a afficher

    Cell<Label>	celluleTexteATrouver ;

	public DetectionSwipe detectionSwipe = null ;

	Pendu jeu ;	//référence aux données du jeu
    
    public EcranHighscores(Pendu jeuEnCours) {

    	jeu = jeuEnCours ;	//reprend la référence au jeu pour toutes les méthodes de la classe
 
    	if (Pendu.getEcranHighscores() == null) Pendu.setEcranHighscores(this); 	//Ecrit la référence à l'écran que l'on vient de créer
    	
    	if (stage == null) creeUI() ; //Si c'est le premier appel, on crée l'affichage

	}
    
    @SuppressWarnings("unchecked")
	private void creeUI() {
    	highscore = Pendu.highscores.getHighscoreActuel().getMeilleursScores() ;

    	
       	stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);		//Met le focus sur notre écran
        

        table = new Table() ;
        //Définit la disposition de la table
        if (Pendu.getDebugState()) table.setDebug(true); // This is optional, but enables debug lines for tables.
        table.setFillParent(true);  //La table occupe tout l'écran
        stage.addActor(table) ;
        
        lTitre = new Label("Meilleurs scores\n"+Pendu.highscores.getHighscoreActuel().getNiveau().getDenomination(), Pendu.getSkin()) ;
        lTitre.setAlignment(Align.center);
        table.add(lTitre).width(Pendu.getLargeurEcran()) ;
        table.row();
 
        tScores = new Table() ;
        if (Pendu.getDebugState()) tScores.setDebug(true); // This is optional, but enables debug lines for tables.

    	lColonnes = new Label[NOMS_COL.length] ;	//Tableau contenant les labels des titres des colonnes

    	//Création des têtes de colonnes
    	for (int i = 0 ; i < NOMS_COL.length ; i++) {
    		tScores.columnDefaults(i).minWidth(TAILLES_COLONNES[i][0]).maxWidth(TAILLES_COLONNES[i][1]).space(ESPACEMENT_COLONNES) ;	//Spéficifations des colonnes
    		lColonnes[i] = new Label(NOMS_COL[i],Pendu.getSkin()) ;
    		lColonnes[i].setAlignment(ALIGNEMENTS_COL_HIGHSCORES[i]);
    		tScores.add(lColonnes[i]).align(ALIGNEMENTS_COL_HIGHSCORES[i]) ;
    	}

    	//Création du contenu des colonnes
    	lScores = new Label[Highscore.NB_HIGHSCORES_PAR_CATEGORIE][NOMS_COL.length] ;
    	clScores = new Cell[Highscore.NB_HIGHSCORES_PAR_CATEGORIE][NOMS_COL.length] ;
    	
    	for (int i = 0 ; i < highscore.length ; i++) {
     		tScores.row() ;
    		for (int j=0 ; j < COL_HIGHSCORES.length ; j++) {
        		if (COL_HIGHSCORES[j] == 0) {			//Si c'est le rang
        			lScores[i][j] = new Label(Integer.toString(i+1), Pendu.getSkin()) ;	//On inscrit l'index +1 pour avoir le numero de rang
        		}
        		else lScores[i][j] = new Label(highscore[i].getStringItemScore(COL_HIGHSCORES[j]), Pendu.getSkin()) ;
        		lScores[i][j].setAlignment(ALIGNEMENTS_COL_HIGHSCORES[j]);
        		clScores[i][j] = tScores.add(lScores[i][j]) ;
    		}
    	}
    	
    	scrollpane = new ScrollPane(tScores, Pendu.getSkin()) ;
    	
        if (Pendu.getDebugState()) scrollpane.setDebug(true); // This is optional, but enables debug lines for tables.
        celluleTable = table.add(scrollpane) ; //.align(Align.top).width(Pendu.getLargeurEcran()) ;

        
        boutonRetour = new TextButton("Retour", Pendu.getSkin());
        boutonRetour.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor acteur) { Pendu.getEcranHighscores().retourAccueil(); }
        } ) ;
        table.row() ;
        table.add(boutonRetour).minHeight(Pendu.HAUTEUR_MIN_BOUTONS).maxHeight(Pendu.HAUTEUR_MAX_BOUTONS) ;
    	
    }

    public void retourAccueil() {
    	if (Pendu.getEcranAccueil() != null) {
    		jeu.setScreen(Pendu.getEcranAccueil());	//Bascule sur l'écran d'accueil
    	}
    	else {
    		jeu.setScreen(new EcranAccueil(jeu));	//Bascule sur l'écran d'accueil
    	}
    }
    
    public void actualiseUI() {
    	highscore = Pendu.highscores.getHighscore(noNiveauAAfficher,noDicoHighscore).getMeilleursScores() ;
    	
    	lTitre.setText("Meilleurs scores\n"+Pendu.niveaux[noNiveauAAfficher].getDenomination()) ;
    	
    	//On met à jour le contenu de la table des highscores
    	for (int i = 0 ; i < highscore.length ; i++) {
    		for (int j=0 ; j < COL_HIGHSCORES.length ; j++) {
        		if (COL_HIGHSCORES[j] == 0) {			//Si c'est le rang
        			lScores[i][j].setText(Integer.toString(i+1)) ;
        		}
        		else {
        			lScores[i][j].setText(highscore[i].getStringItemScore(COL_HIGHSCORES[j])) ;
        		}
    		}
    	}
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
    	
   		lTitre.setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getHauteurEcran(),TAILLES_POLICE_ADAPTEES));	//Adapte la taille de la police à la hauteur de l'affichage
   		celluleTable.width(Pendu.getLargeurEcran()*LARGEUR_MAX_HIGHSCORES) ;

   		//Redimensionne les tête de colonnes
   		for (int j = 0 ; j < NOMS_COL.length ; j++) {
    		lColonnes[j].setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getLargeurEcran(),TAILLES_POLICE_ADAPTEES)*COEF_HIGHSCORES);	//Adapte la taille de la police à la largeur de l'affichage
    	}

		dimmensionneTextesHighscores() ;

   		tScores.setWidth(Pendu.getLargeurEcran());
   		
        stage.getViewport().update(width, height, true);
    }

    public void dimmensionneTextesHighscores() {

		float positionHighscore = -1 ;	//Position du highscore dans la table
		//Redimensionne le texte des scores en mettant la bonne couleur
		for (int i = 0 ; i < highscore.length ; i++) {
			for (int j=0 ; j < COL_HIGHSCORES.length ; j++) {
				if ( (Pendu.position == i) && (Pendu.niveau.getNumero() == noNiveauAAfficher) ) {	//Si le highscore vient d'etre fait (meme niveau et position = -1), on l'affiche en rouge et gros
					positionHighscore = lScores[i][0].getY() ;	//Récupère la position verticale du highscore dans la table
					lScores[i][j].setColor(Color.FIREBRICK);
					lScores[i][j].setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getLargeurEcran(),TAILLES_POLICE_ADAPTEES)*COEF_SCORE_JOUEUR);	//Adapte la taille de la police à la hauteur de l'affichage
				}
				else {
					lScores[i][j].setColor(Color.WHITE);
					lScores[i][j].setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getLargeurEcran(),TAILLES_POLICE_ADAPTEES)*COEF_HIGHSCORES);	//Adapte la taille de la police à la hauteur de l'affichage
				}
				clScores[i][j].prefWidth(Pendu.getLargeurEcran()*LARGEUR_COLONNES_HIGHSCORES[j]/100) ;
			}
		}
		if (positionHighscore != -1) scrollpane.scrollTo(0, positionHighscore, 10, 10); 	//On positionne le scrolling de manière à voir le highscore qu'on vient de réaliser
	}


    @Override
    public void show() {
		//On commence par fixer le niveau et le dico pour lequel on veut afficher les highscores
		noNiveauAAfficher = Pendu.niveau.getNumero() ;
		noDicoHighscore = Pendu.dictionnaires.getDictionnaireActuel().getNumero() ;

		InputMultiplexer im ;

    	if (Pendu.getDebugState()) Gdx.app.log("INFO","EcranHighscores - show");

		detectionSwipe = new DetectionSwipe() ;

		im = new InputMultiplexer() ;
		im.addProcessor(detectionSwipe);
		im.addProcessor(stage);
		im.addProcessor(new GestionClavier(new EcouteClavier(){
			public void toucheGAUCHE() { DetectionSwipe.DirectionGestureListener.changeNiveau(-1) ; } ;
			public void toucheDROITE() { DetectionSwipe.DirectionGestureListener.changeNiveau(1) ; } ;
		    public void toucheESCAPE() { Pendu.getEcranHighscores().retourAccueil(); } ; 
		}));
		Gdx.input.setInputProcessor(im) ;

    	if (stage == null) creeUI() ; //Si c'est le premier appel, on crée l'affichage
    	actualiseUI() ;
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
    	Pendu.setEcranHighscores(null) ;	//Supprime la référence à l'écran pour l'obliger à être re-crée la prochaine fois
    	stage.dispose();
    }
}
