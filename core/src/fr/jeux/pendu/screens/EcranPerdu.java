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
import com.badlogic.gdx.utils.viewport.StretchViewport;

import fr.jeux.pendu.Pendu;

public class EcranPerdu implements Screen {
    private static Stage stage = null ;
    private static Table table = null ;  //Table contenant les labels

    private static Label lAffichageScore = null ;
    private static Texture img = null ; 
    private static Image imagePerdu = null ;
    private static Label lPerdu = null ;

    Pendu jeu ;	//r�f�rence aux donn�es du jeu
    
    public EcranPerdu(Pendu jeuEnCours) {

    	jeu = jeuEnCours ;	//reprend la r�f�rence au jeu pour toutes les m�thodes de la classe
 
    	if (jeu.getEcranPerdu() == null) jeu.setEcranPerdu(this); 	//Ecrit la r�f�rence � l'�cran que l'on vient de cr�er
    	
    	if (stage == null) creeUI() ; //Si c'est le premier appel, on cr�e l'affichage
//    	actualiseUI() ;
//        jeu.setScore(0) ;     //On remet le score � z�ro apr�s l'avoir affich�
    }
    
    private void creeUI() {

       	stage = new Stage(new StretchViewport(jeu.getLargeurEcran(),jeu.getHauteurEcran()));

        Gdx.input.setInputProcessor(stage);		//Met le focus sur notre �cran
        
        if (jeu.getDebugState()) {
            System.out.println("Perdu !");
        }

        //Cr�e l'affichage du score final
       	lAffichageScore = new Label("Score final : "+ Integer.toString(jeu.getScore()),jeu.getSkin()) ;
        lAffichageScore.setFontScale(3);
        

        //Cr�e le label contenant l'image
       	img = new Texture(jeu.IMAGE_PERDU);
       	imagePerdu = new Image(img);

        //Cr�e le label contenant le mot � trouver
        lPerdu = new Label("",jeu.skin) ;
        lPerdu.setFontScale(3);
        
        table = new Table() ;

        //D�finit la disposition de la table
        table.pad(3) ;
        if (jeu.getDebugState()) table.setDebug(true); // This is optional, but enables debug lines for tables.
        table.setFillParent(true);  //La table occupe tout l'�cran
        table.add(lAffichageScore) ;
        table.row();
        table.add(imagePerdu);
        table.row() ;    //Place le texte en dessous de l'image
        table.add(lPerdu) ;
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
        lPerdu.setText("Il fallait trouver : "+jeu.motADeviner);

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
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.app.log("EcranPerdu","show");
        Gdx.input.setInputProcessor(stage);
        
    	if (stage == null) creeUI() ; //Si c'est le premier appel, on cr�e l'affichage
    	actualiseUI() ;
        jeu.setScore(0) ;     //On remet le score � z�ro apr�s l'avoir affich�
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
