package fr.jeux.pendu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import fr.jeux.pendu.Pendu;

public class EcranPerdu implements Screen {
    private static Stage stage = null ;
    private static Table table = null ;  //Table contenant les labels

    private static Label lAffichageScore = null ;
    private static Texture img = null ; 
    private static Image imagePerdu = null ;
    private static Label lTexteATrouver = null ;
    
    Cell	celluleTexteATrouver ;

    Pendu jeu ;	//référence aux données du jeu
    
    public EcranPerdu(Pendu jeuEnCours) {

    	jeu = jeuEnCours ;	//reprend la référence au jeu pour toutes les méthodes de la classe
 
    	if (jeu.getEcranPerdu() == null) jeu.setEcranPerdu(this); 	//Ecrit la référence à l'écran que l'on vient de créer
    	
    	if (stage == null) creeUI() ; //Si c'est le premier appel, on crée l'affichage
    }
    
    private void creeUI() {

       	stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);		//Met le focus sur notre écran
        
        if (jeu.getDebugState()) {
            System.out.println("Perdu !");
        }

        //Crée l'affichage du score final
       	lAffichageScore = new Label("Score final : "+ Integer.toString(jeu.getScore()),jeu.getSkin()) ;
        lAffichageScore.setFontScale(3);
        

        //Crée le label contenant l'image
       	img = new Texture(jeu.IMAGE_PERDU);
       	imagePerdu = new Image(img);

        //Crée le label contenant le mot à trouver
        lTexteATrouver = new Label("",jeu.skin) ;
        lTexteATrouver.setFontScale(3);
        lTexteATrouver.setWrap(true);
        lTexteATrouver.setAlignment(Align.center);
        
        table = new Table() ;

        //Définit la disposition de la table
        table.pad(3) ;
        if (jeu.getDebugState()) table.setDebug(true); // This is optional, but enables debug lines for tables.
        table.setFillParent(true);  //La table occupe tout l'écran
        table.add(lAffichageScore) ;
        table.row();
        table.add(imagePerdu);
        table.row() ;    //Place le texte en dessous de l'image
        celluleTexteATrouver = table.add(lTexteATrouver).align(Align.center).width(jeu.getLargeurEcran()) ;
        table.setVisible(true) ;

        stage.addActor(table) ;
        
        imagePerdu.addListener(new ClickListener() {
//            Pendu jeu ;
            public void clicked(InputEvent event, float x, float y) {
/*                if (jeu.getDebugState()) {
                    System.out.println("Retour au menu");
                }*/
                Pendu.ecranJeu.RetourMenu() ;
            }
        });
    	
    }
    
    private void actualiseUI() {
        lAffichageScore.setText("Score final : "+ Integer.toString(jeu.getScore()));
        lTexteATrouver.setText("Il fallait trouver :\n"+jeu.motADeviner);

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
    	
    	if (table != null) {
    		celluleTexteATrouver.width(width) ;
    	}
    	
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.app.log("EcranPerdu","show");
        Gdx.input.setInputProcessor(stage);
        
    	if (stage == null) creeUI() ; //Si c'est le premier appel, on crée l'affichage
    	actualiseUI() ;
        jeu.setScore(0) ;     //On remet le score à zéro après l'avoir affiché
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
