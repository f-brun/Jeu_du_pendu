package fr.jeux.pendu.screens;

import com.badlogic.gdx.Gdx;
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

import fr.jeux.pendu.Pendu;

public class EcranGagne implements Screen {
    public static final float[][] TAILLES_POLICE_ADAPTEES = {{600,  400,  300,  200,  100,    0},
    														{   3, 2.5f,   2f, 1.5f,   1f, 0.5f}};

	
	private static Stage stage = null ;
    private static Table table = null ;  //Table contenant les labels

    private static Label lAffichageScore = null ;
    private static Texture img = null ; 
    private static Image imageGagne = null ;

    Pendu jeu ;	//référence aux données du jeu
    
    public EcranGagne(Pendu jeuEnCours) {

    	jeu = jeuEnCours ;	//reprend la référence au jeu pour toutes les méthodes de la classe
 
    	if (Pendu.getEcranGagne() == null) Pendu.setEcranGagne(this); 	//Ecrit la référence à l'écran que l'on vient de créer
    	
    	if (stage == null) creeUI() ; //Si c'est le premier appel, on crée l'affichage
    	actualiseUI() ;
    }
    private void creeUI() {

       	stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);		//Met le focus sur notre écran
        
        if (Pendu.getDebugState()) {
            System.out.println("Gagné !");
        }

        //Crée l'affichage du score
       	lAffichageScore = new Label("Score : "+ Integer.toString(Pendu.getScore()),Pendu.getSkin()) ;
        

        //Crée le label contenant l'image
       	img = new Texture(Pendu.IMAGE_GAGNE);
       	imageGagne = new Image(img);

        table = new Table() ;

        //Définit la disposition de la table
        table.pad(3) ;
        table.setFillParent(true);  //La table occupe tout l'écran
        if (Pendu.getDebugState()) table.setDebug(true); // This is optional, but enables debug lines for tables.
        table.add(lAffichageScore);
        table.row();
        table.add(imageGagne);
        table.row() ;
        table.setVisible(true) ;
  
        imageGagne.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (Pendu.getDebugState()) {
                    System.out.println("Nouveau mot");
                }
                if (jeu.getEcranJeu() == null) {
                	jeu.setEcranJeu(new EcranJeu(jeu));
                }
                else jeu.setScreen(Pendu.getEcranJeu());	//Bascule sur l'écran de jeu pour avoir un nouveau mot
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
    	jeu.setHauteurEcran(height) ;
    	jeu.setLargeurEcran(width) ;
    	if (Pendu.getDebugState()) Gdx.app.log("Redimmensionnement vers ",width+" x "+height);
   		lAffichageScore.setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getHauteurEcran(),TAILLES_POLICE_ADAPTEES));	//Adapte la taille de la police à la hauteur de l'affichage
    	stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
    	if (Pendu.getDebugState()) Gdx.app.log("EcranGagné","show");
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
    	jeu.setEcranGagne(null) ;	//Supprime la référence à l'écran pour l'obliger à être re-crée la prochaine fois
        img.dispose();
        stage.dispose();
     }
}
