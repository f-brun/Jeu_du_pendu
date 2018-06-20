package fr.jeux.pendu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
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

    static final int[] COL_HIGHSCORES = {1, 2, 3, 4 } ;
    static final String[] NOMS_COL = {"Rang", "Nom", "Score", "Temps"} ;
    
    private static Stage stage = null ;
    private static Table table = null ;  //Table contenant l'UI
    
    private static ScrollPane scrollpane = null ;
    
    private static Label	lTitre = null ;	//Titre de l'écran
    
    private static Table tScores = null ;	//Table contenant la liste des scores
    private static Label[] lColonnes = null ; 	//Labels des têtes de colonne
    private static Label[][] lScores = null ;	//Labels contenant les informations sur les highscores
    
    private static TextButton boutonRetour = null ;

    
    Cell<Label>	celluleTexteATrouver ;

    Pendu jeu ;	//référence aux données du jeu
    
    public EcranHighscores(Pendu jeuEnCours) {

    	jeu = jeuEnCours ;	//reprend la référence au jeu pour toutes les méthodes de la classe
 
    	if (Pendu.getEcranHighscores() == null) jeu.setEcranHighscores(this); 	//Ecrit la référence à l'écran que l'on vient de créer
    	
    	if (stage == null) creeUI() ; //Si c'est le premier appel, on crée l'affichage
    }
    
    private void creeUI() {
    	Score[] highScore = Pendu.highscore.getMeilleursScores() ;

    	
       	stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);		//Met le focus sur notre écran
        

        table = new Table() ;
        
        lTitre = new Label("Meilleurs scores", Pendu.getSkin()) ;
        table.add(lTitre) ;
        table.row();
        
        		
        //Définit la disposition de la table
        table.pad(3) ;
        if (Pendu.getDebugState()) table.setDebug(true); // This is optional, but enables debug lines for tables.
        table.setFillParent(true);  //La table occupe tout l'écran

        stage.addActor(table) ;
        

        tScores = new Table() ;
        if (Pendu.getDebugState()) tScores.setDebug(true); // This is optional, but enables debug lines for tables.
        
        scrollpane = new ScrollPane(tScores, Pendu.getSkin()) ;
        table.add(scrollpane) ;

       	tScores.columnDefaults(0).width(45).space(10);
    	tScores.columnDefaults(1).width(350).space(10);
    	tScores.columnDefaults(2).width(60).space(10);
    	tScores.columnDefaults(3).width(80).space(10);

    	lColonnes = new Label[NOMS_COL.length] ;
    	
    	for (int i = 0 ; i < NOMS_COL.length ; i++) {
    		lColonnes[i] = new Label(NOMS_COL[i],Pendu.getSkin()) ;
    		tScores.add(lColonnes[i]) ;
    	}
    	lColonnes[0].setAlignment(Align.center);
    	lColonnes[1].setAlignment(Align.left);
    	lColonnes[2].setAlignment(Align.center);
    	lColonnes[3].setAlignment(Align.center);
    	
    	
    	lScores = new Label[Highscore.NB_HIGHSCORES_PAR_CATEGORIE][NOMS_COL.length] ;
    	
    	for (int i = 0 ; i < highScore.length ; i++) {
    		tScores.row() ;
    		lScores[i][0] = new Label(Integer.toString(i+1), Pendu.getSkin()) ;
    		lScores[i][0].setAlignment(Align.right);
    		tScores.add(lScores[i][0]);
    		lScores[i][1] = new Label(highScore[i].joueur, Pendu.getSkin()) ;
    		lScores[i][1].setAlignment(Align.left);
    		tScores.add(lScores[i][1]) ;
    		lScores[i][2] = new Label(Integer.toString(highScore[i].score), Pendu.getSkin()) ;
    		lScores[i][2].setAlignment(Align.center);
    		tScores.add(lScores[i][2]) ;
    		lScores[i][3] = new Label(Long.toString(highScore[i].temps), Pendu.getSkin()) ;
    		lScores[i][3].setAlignment(Align.center);
    		tScores.add(lScores[i][3]) ;
     	}
        
        
        boutonRetour = new TextButton("Retour", Pendu.getSkin());
        boutonRetour.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor acteur) {
            	if (Pendu.getEcranAccueil() == null ) jeu.setEcranAccueil(new EcranAccueil(jeu));
            	else jeu.setScreen(Pendu.getEcranAccueil());	//Retourne sur l'ecran d'accueil
            }
        } ) ;
        table.row() ;
        table.add(boutonRetour).minHeight(Pendu.HAUTEUR_MIN_BOUTONS).maxHeight(Pendu.HAUTEUR_MAX_BOUTONS) ;
    	
    }

   
    public void RetourMenu() {
    	if (Pendu.getEcranAccueil() != null) {
    		jeu.setScreen(Pendu.getEcranAccueil());	//Bascule sur l'écran d'accueil
    	}
    	else {
    		jeu.setScreen(new EcranAccueil(jeu));	//Bascule sur l'écran d'accueil
    	}
    }
    
    private void actualiseUI() {
    	Score[] highScore = Pendu.highscore.getMeilleursScores() ;
    	
    	tScores.columnDefaults(0).width(40);
    	tScores.columnDefaults(1).width(300);
    	tScores.columnDefaults(2).width(80);
    	tScores.columnDefaults(3).width(120);

    	for (int i = 0 ; i < highScore.length ; i++) {
    		lScores[i][0].setText(Integer.toString(i+1)) ;
    		lScores[i][1].setText(highScore[i].joueur) ;
    		lScores[i][2].setText(Integer.toString(highScore[i].score)) ;
    		lScores[i][3].setText(Long.toString(highScore[i].temps)) ;
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
    	jeu.setHauteurEcran(height) ;
    	jeu.setLargeurEcran(width) ;
    	if (Pendu.getDebugState()) Gdx.app.log("INFO","Redimmensionnement vers "+width+" x "+height);
    	
//   		celluleTexteATrouver.width(width) ;
   		lTitre.setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getHauteurEcran(),TAILLES_POLICE_ADAPTEES));	//Adapte la taille de la police à la hauteur de l'affichage
    	
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
    	if (Pendu.getDebugState()) Gdx.app.log("INFO","EcranHighscores - show");
        Gdx.input.setInputProcessor(stage);
        
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
    	jeu.setEcranHighscores(null) ;	//Supprime la référence à l'écran pour l'obliger à être re-crée la prochaine fois
    	stage.dispose();
    }
}
