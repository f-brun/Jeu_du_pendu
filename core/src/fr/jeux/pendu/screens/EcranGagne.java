package fr.jeux.pendu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import fr.jeux.pendu.GestionClavier;
import fr.jeux.pendu.Pendu;
/**
 * Ecran qui s'affiche lorsque l'on arrive à compléter le mot. Le chrono est suspendu le temps d'afficher le score et l'image d'encouragement.
 * @author Florent Brun
 *
 */
public class EcranGagne implements Screen {
    public static final float[][] TAILLES_POLICE_ADAPTEES = {{600,  400,  300,  200,  100,    0},
    														{   3, 2.5f,   2f, 1.5f,   1f, 0.5f}};

	
	private static Stage stage = null ;
    private static Table table = null ;  //Table contenant les labels

    private static Label lAffichageScore = null ;
    private static Texture img = null ; 
    private static Image imageGagne = null ;

    public static Pendu jeu ;	//reference aux donnees du jeu
    
    public EcranGagne(Pendu jeuEnCours) {

    	jeu = jeuEnCours ;	//reprend la reference au jeu pour toutes les methodes de la classe
 
    	if (Pendu.getEcranGagne() == null) Pendu.setEcranGagne(this); 	//Ecrit la reference a l'ecran que l'on vient de creer
    	
    	if (stage == null) creeUI() ; //Si c'est le premier appel, on cree l'affichage
    	actualiseUI() ;
    }
    private void creeUI() {

       	stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);		//Met le focus sur notre ecran
        
        if (Pendu.getDebugState()) {
            System.out.println("Gagne !");
        }

        //Cree l'affichage du score
       	lAffichageScore = new Label("Score : "+ Integer.toString(Pendu.getScore()),Pendu.getSkin()) ;
        

        //Cree le label contenant l'image
       	img = new Texture(Pendu.IMAGE_GAGNE);
       	imageGagne = new Image(img);

        table = new Table() ;

        //Definit la disposition de la table
        table.pad(3) ;
        table.setFillParent(true);  //La table occupe tout l'ecran
        if (Pendu.getDebugState()) table.setDebug(true); // This is optional, but enables debug lines for tables.
        table.add(lAffichageScore);
        table.row();
        table.add(imageGagne);
        table.row() ;
        table.setVisible(true) ;
  
        imageGagne.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (Pendu.getDebugState()) Gdx.app.log("INFO", "Nouveau mot");

                Pendu.chrono.reprise() ;		//On fait repartir le chrono pour le nouveau mot
                
                if (Pendu.getEcranJeu() == null) {
                	Pendu.setEcranJeu(new EcranJeu(EcranGagne.jeu));
                }
                else jeu.setScreen(Pendu.getEcranJeu());	//Bascule sur l'ecran de jeu pour avoir un nouveau mot
            }
        });
        
        table.setVisible(true) ;

        stage.addActor(table) ;
        
    }
    
    private void actualiseUI() {
        lAffichageScore.setText("Score : "+ Integer.toString(Pendu.getScore()));

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
   		lAffichageScore.setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getHauteurEcran(),TAILLES_POLICE_ADAPTEES));	//Adapte la taille de la police a la hauteur de l'affichage
    	stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        InputMultiplexer im ;

    	if (Pendu.getDebugState()) Gdx.app.log("INFO","EcranGagne - show");

        im = new InputMultiplexer() ;
        im.addProcessor(stage);
        im.addProcessor(new GestionClavier(new GestionClavier.EcouteClavier(){
            public void toucheGAUCHE() { } ;
            public void toucheDROITE() { } ;
            public void toucheESCAPE() { Pendu.getEcranPerdu().retourMenu() ; } ;
        }));
        Gdx.input.setInputProcessor(im) ;
        
    	if (stage == null) creeUI() ; //Si c'est le premier appel, on cree l'affichage
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
     * Elimine les references statiques aux objets car si l'application est relancee juste apres etre quittee, toutes les references statiques
     * demeurent alors que les objets (ecran, widgets... ) sont eux detruits
     */
    @Override
    public void dispose() {
        if (Pendu.DEBUG) Gdx.app.log("INFO","Suppression des references de l'ecran gagne") ;
    	Pendu.setEcranGagne(null) ;	//Supprime la reference a l'ecran pour l'obliger a etre re-cree la prochaine fois
        if (Pendu.DEBUG) Gdx.app.log("INFO","Destruction de la texture et du stage") ;
        if (img != null) img.dispose();
        if (stage != null) stage.dispose();
     }
}
