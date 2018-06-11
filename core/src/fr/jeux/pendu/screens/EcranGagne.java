package fr.jeux.pendu.screens;

import static fr.jeux.pendu.GestionMots.SepareParDesEspaces;

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
    private static Stage stage = null ;
    private static Table table = null ;  //Table contenant les labels

    private static Label lAffichageScore = null ;
    private static Texture img = null ; 
    private static Image imageGagne = null ;

    Pendu jeu ;	//référence aux données du jeu
    
    public EcranGagne(Pendu jeuEnCours) {

    	jeu = jeuEnCours ;	//reprend la référence au jeu pour toutes les méthodes de la classe
 
    	if (jeu.getEcranGagne() == null) jeu.setEcranGagne(this); 	//Ecrit la référence à l'écran que l'on vient de créer
    	
    	if (stage == null) creeUI() ; //Si c'est le premier appel, on crée l'affichage
    	actualiseUI() ;
    }
    private void creeUI() {

       	stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);		//Met le focus sur notre écran
        
        if (jeu.getDebugState()) {
            System.out.println("Gagné !");
        }

        //Crée l'affichage du score
       	lAffichageScore = new Label("Score : "+ Integer.toString(jeu.getScore()),jeu.getSkin()) ;
        lAffichageScore.setFontScale(3);
        

        //Crée le label contenant l'image
       	img = new Texture(jeu.IMAGE_GAGNE);
       	imageGagne = new Image(img);

        table = new Table() ;

        //Définit la disposition de la table
        table.pad(3) ;
        table.setFillParent(true);  //La table occupe tout l'écran
        if (jeu.getDebugState()) table.setDebug(true); // This is optional, but enables debug lines for tables.
        table.add(lAffichageScore);
        table.row();
        table.add(imageGagne);
        table.row() ;
        table.setVisible(true) ;
  
        imageGagne.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (jeu.getDebugState()) {
                    System.out.println("Retour au menu");
                }
                jeu.ecranJeu.RetourMenu() ;
            }
        });
        
        table.setVisible(true) ;

        stage.addActor(table) ;
        
    }
    
    private void actualiseUI() {
        lAffichageScore.setText("Score : "+ Integer.toString(jeu.getScore()));

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
    	if (jeu.getDebugState()) Gdx.app.log("Redimmensionnement vers ",width+" x "+height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.app.log("EcranGagné","show");
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

    
    @Override
    public void dispose() {
        img.dispose();
        stage.dispose();
    }
}
