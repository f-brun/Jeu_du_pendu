package fr.jeux.pendu.screens;

import com.badlogic.gdx.Gdx;
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

import fr.jeux.pendu.Highscore;
import fr.jeux.pendu.Pendu;
import fr.jeux.pendu.Score;

public class EcranHighscores implements Screen {
    public static final float[][] TAILLES_POLICE_ADAPTEES = {{600,  400,  300,  200,  100,    0},
			 												{2.5f,   2f, 1.5f,   1f, 0.7f, 0.5f}};
    public static final String[] POSITION = {"premier", "deuxieme", "troisieme", "quatrieme", "cinquieme", "sixieme", "septieme", "huitieme", "neuvieme", "dixieme",
    								"onzieme", "douzieme", "treizieme", "quatorzieme", "quinzieme", "seizieme", "dix-septieme", "dix-huitieme", "dix-neuvieme", "vingtieme"} ;

    static final int[] COL_HIGHSCORES = {0, Score.NOM_JOUEUR, Score.SCORE, Score.NB_MOTS_DEVINES, Score.TEMPS } ;
    static final String[] NOMS_COL = {"Rang", "Nom", "Score", "Mots", "Temps"} ;
    static final int[][] TAILLES_COLONNES = { {40,80}, {200,800}, {70,160}, {70,100}, {90,180} } ;	//Largeurs min et max des colonnes
    static final int ESPACEMENT_COLONNES = 10 ;		//Espacement entre les colonnes
     	
   	static final int[] ALIGNEMENTS_COL_HIGHSCORES = {Align.right, Align.left, Align.center, Align.center, Align.center} ;
    static final float[] LARGEUR_COLONNES_HIGHSCORES = {10f,40f,15f,15f,15f} ;	//Largeurs des colonnes de highscore en %
    static final float LARGEUR_MAX_HIGHSCORES = 0.9f ;		//Largeur maxi des highscores en % de la fen�tre
    static final float COEF_HIGHSCORES = 0.85f ;
    static final float COEF_SCORE_JOUEUR = 1.2f ;
    
    private static Stage stage = null ;
    private static Table table = null ;  //Table contenant l'UI
    
    private static ScrollPane scrollpane = null ;
    
    private static Label	lTitre = null ;	//Titre de l'�cran
    
    private static Table tScores = null ;	//Table contenant la liste des scores
    private static Label[] lColonnes = null ; 	//Labels des t�tes de colonne
    private static Label[][] lScores = null ;	//Labels contenant les informations sur les highscores
    private static Cell<Label>[][]	clScores	= null ;
    private static Cell<ScrollPane> celluleTable = null ;
    
    
    private static TextButton boutonRetour = null ;

    
    Cell<Label>	celluleTexteATrouver ;

    Pendu jeu ;	//r�f�rence aux donn�es du jeu
    
    public EcranHighscores(Pendu jeuEnCours) {

    	jeu = jeuEnCours ;	//reprend la r�f�rence au jeu pour toutes les m�thodes de la classe
 
    	if (Pendu.getEcranHighscores() == null) Pendu.setEcranHighscores(this); 	//Ecrit la r�f�rence � l'�cran que l'on vient de cr�er
    	
    	if (stage == null) creeUI() ; //Si c'est le premier appel, on cr�e l'affichage
    }
    
    @SuppressWarnings("unchecked")
	private void creeUI() {
    	Score[] highScore = Pendu.highscores.getHighscoreActuel().getMeilleursScores() ;

    	
       	stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);		//Met le focus sur notre �cran
        

        table = new Table() ;
        //D�finit la disposition de la table
        if (Pendu.getDebugState()) table.setDebug(true); // This is optional, but enables debug lines for tables.
        table.setFillParent(true);  //La table occupe tout l'�cran
        stage.addActor(table) ;
        
        lTitre = new Label("Meilleurs scores\n"+Pendu.highscores.getHighscoreActuel().getNiveau().getDenomination(), Pendu.getSkin()) ;
        lTitre.setAlignment(Align.center);
        table.add(lTitre).width(Pendu.getLargeurEcran()) ;
        table.row();
 
        tScores = new Table() ;
        if (Pendu.getDebugState()) tScores.setDebug(true); // This is optional, but enables debug lines for tables.

    	lColonnes = new Label[NOMS_COL.length] ;	//Tableau contenant les labels des titres des colonnes

    	//Cr�ation des t�tes de colonnes
    	for (int i = 0 ; i < NOMS_COL.length ; i++) {
    		tScores.columnDefaults(i).minWidth(TAILLES_COLONNES[i][0]).maxWidth(TAILLES_COLONNES[i][1]).space(ESPACEMENT_COLONNES) ;	//Sp�ficifations des colonnes
    		lColonnes[i] = new Label(NOMS_COL[i],Pendu.getSkin()) ;
    		lColonnes[i].setAlignment(ALIGNEMENTS_COL_HIGHSCORES[i]);
    		tScores.add(lColonnes[i]).align(ALIGNEMENTS_COL_HIGHSCORES[i]) ;
    	}

    	//Cr�ation du contenu des colonnes
    	lScores = new Label[Highscore.NB_HIGHSCORES_PAR_CATEGORIE][NOMS_COL.length] ;
    	clScores = new Cell[Highscore.NB_HIGHSCORES_PAR_CATEGORIE][NOMS_COL.length] ;
    	
    	for (int i = 0 ; i < highScore.length ; i++) {
     		tScores.row() ;
    		for (int j=0 ; j < COL_HIGHSCORES.length ; j++) {
        		if (COL_HIGHSCORES[j] == 0) {			//Si c'est le rang
        			lScores[i][j] = new Label(Integer.toString(i+1), Pendu.getSkin()) ;	//On inscrit l'index +1 pour avoir le numero de rang
        		}
        		else lScores[i][j] = new Label(highScore[i].getStringItemScore(COL_HIGHSCORES[j]), Pendu.getSkin()) ;
        		lScores[i][j].setAlignment(ALIGNEMENTS_COL_HIGHSCORES[j]);
        		clScores[i][j] = tScores.add(lScores[i][j]) ;
    		}
    	}
    	
    	scrollpane = new ScrollPane(tScores, Pendu.getSkin()) ;
    	
        if (Pendu.getDebugState()) scrollpane.setDebug(true); // This is optional, but enables debug lines for tables.
        celluleTable = table.add(scrollpane) ; //.align(Align.top).width(Pendu.getLargeurEcran()) ;

        
        boutonRetour = new TextButton("Retour", Pendu.getSkin());
        boutonRetour.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor acteur) {
            	if (Pendu.getEcranAccueil() == null ) Pendu.setEcranAccueil(new EcranAccueil(jeu));
            	else jeu.setScreen(Pendu.getEcranAccueil());	//Retourne sur l'ecran d'accueil
            }
        } ) ;
        table.row() ;
        table.add(boutonRetour).minHeight(Pendu.HAUTEUR_MIN_BOUTONS).maxHeight(Pendu.HAUTEUR_MAX_BOUTONS) ;
    	
    }

   
    public void RetourMenu() {
    	if (Pendu.getEcranAccueil() != null) {
    		jeu.setScreen(Pendu.getEcranAccueil());	//Bascule sur l'�cran d'accueil
    	}
    	else {
    		jeu.setScreen(new EcranAccueil(jeu));	//Bascule sur l'�cran d'accueil
    	}
    }
    
    private void actualiseUI() {
    	Score[] highScore = Pendu.highscores.getHighscoreActuel().getMeilleursScores() ;
    	
    	lTitre.setText("Meilleurs scores\n"+Pendu.highscores.getHighscoreActuel().getNiveau().getDenomination()) ;
    	
    	//On met � jour le contenu de la table des highscores
    	for (int i = 0 ; i < highScore.length ; i++) {
    		for (int j=0 ; j < COL_HIGHSCORES.length ; j++) {
        		if (COL_HIGHSCORES[j] == 0) {			//Si c'est le rang
        			lScores[i][j].setText(Integer.toString(i+1)) ;
        		}
        		else {
        			lScores[i][j].setText(highScore[i].getStringItemScore(COL_HIGHSCORES[j])) ;
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
    	
   		lTitre.setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getHauteurEcran(),TAILLES_POLICE_ADAPTEES));	//Adapte la taille de la police � la hauteur de l'affichage
   		celluleTable.width(Pendu.getLargeurEcran()*LARGEUR_MAX_HIGHSCORES) ;

   		//Redimensionne les t�te de colonnes
   		for (int j = 0 ; j < NOMS_COL.length ; j++) {
    		lColonnes[j].setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getLargeurEcran(),TAILLES_POLICE_ADAPTEES)*COEF_HIGHSCORES);	//Adapte la taille de la police � la largeur de l'affichage
    	}
   		
   		float positionHighscore = 0 ;	//Position du highscore dans la table
   		//Redimensionne le texte des scores en mettant la bonne couleur
   		for (int i = 0 ; i < Pendu.highscores.getHighscoreActuel().getMeilleursScores().length ; i++) {
     		for (int j=0 ; j < COL_HIGHSCORES.length ; j++) {
        		if (Pendu.position == i) {
        			positionHighscore = lScores[i][0].getY() ;	//R�cup�re la position verticale du highscore dans la table
        			lScores[i][j].setColor(Color.FIREBRICK);
        			lScores[i][j].setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getLargeurEcran(),TAILLES_POLICE_ADAPTEES)*COEF_SCORE_JOUEUR);	//Adapte la taille de la police � la hauteur de l'affichage
        		}
        		else {
        			lScores[i][j].setColor(Color.WHITE);
        			lScores[i][j].setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getLargeurEcran(),TAILLES_POLICE_ADAPTEES)*COEF_HIGHSCORES);	//Adapte la taille de la police � la hauteur de l'affichage
        		}
        		clScores[i][j].prefWidth(Pendu.getLargeurEcran()*LARGEUR_COLONNES_HIGHSCORES[j]/100) ;
    		}
    	}
   		if (Pendu.position != -1) scrollpane.scrollTo(0, positionHighscore, 10, 10); 	//On positionne le scorlling de mani�re � voir le highscore qu'on vient de r�aliser

   		tScores.setWidth(Pendu.getLargeurEcran());
   		
/*    	Gdx.app.log("INFO", "Scrollpane : x="+scrollpane.getScrollX()+" y="+scrollpane.getScrollY()+" larg ="+scrollpane.getWidth()+" haut="+scrollpane.getHeight());
    	Gdx.app.log("INFO", "Scrollpane : posX="+scrollpane.getX()+" posY="+scrollpane.getY()+" prefW="+scrollpane.getPrefWidth()+" PrefH="+scrollpane.getPrefHeight());
    	Gdx.app.log("INFO", "ScrollTo : y="+positionHighscore);
*/
    	
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
    	if (Pendu.getDebugState()) Gdx.app.log("INFO","EcranHighscores - show");
        Gdx.input.setInputProcessor(stage);
        
    	if (stage == null) creeUI() ; //Si c'est le premier appel, on cr�e l'affichage
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
     * Elimine les r�f�rences statiques aux objets car si l'application est relanc�e juste apr�s �tre quitt�e, toutes les r�f�rences statiques
     * demeurent alors que les objets (�cran, widgets... ) sont eux d�truits
     */
    @Override
    public void dispose() {
    	Pendu.setEcranHighscores(null) ;	//Supprime la r�f�rence � l'�cran pour l'obliger � �tre re-cr�e la prochaine fois
    	stage.dispose();
    }
}
