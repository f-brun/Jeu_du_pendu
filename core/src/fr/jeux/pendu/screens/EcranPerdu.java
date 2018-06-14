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
    public static final float[][] TAILLES_POLICE_ADAPTEES = {{600,  400,  300,  200,  100,    0},
			 												{   3, 2.5f,   2f, 1.5f,   1f, 0.5f}};

    private static Stage stage = null ;
    private static Table table = null ;  //Table contenant les labels

    private static Label lAffichageScore = null ;
    private static Texture img = null ; 
    private static Image imagePerdu = null ;
    private static Label lTexteATrouver = null ;
    
    Cell<Label>	celluleTexteATrouver ;

    Pendu jeu ;	//r�f�rence aux donn�es du jeu
    
    public EcranPerdu(Pendu jeuEnCours) {

    	jeu = jeuEnCours ;	//reprend la r�f�rence au jeu pour toutes les m�thodes de la classe
 
    	if (Pendu.getEcranPerdu() == null) jeu.setEcranPerdu(this); 	//Ecrit la r�f�rence � l'�cran que l'on vient de cr�er
    	
    	if (stage == null) creeUI() ; //Si c'est le premier appel, on cr�e l'affichage
    }
    
    private void creeUI() {

       	stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);		//Met le focus sur notre �cran
        
        if (Pendu.getDebugState()) {
            System.out.println("Perdu !");
        }

        //Cr�e l'affichage du score final
       	lAffichageScore = new Label("Score final : "+ Integer.toString(Pendu.getScore()),Pendu.getSkin()) ;
        

        //Cr�e le label contenant l'image
       	img = new Texture(Pendu.IMAGE_PERDU);
       	imagePerdu = new Image(img);

        //Cr�e le label contenant le mot � trouver
        lTexteATrouver = new Label("",Pendu.skin) ;
        lTexteATrouver.setWrap(true);
        lTexteATrouver.setAlignment(Align.center);
        
        table = new Table() ;

        //D�finit la disposition de la table
        table.pad(3) ;
        if (Pendu.getDebugState()) table.setDebug(true); // This is optional, but enables debug lines for tables.
        table.setFillParent(true);  //La table occupe tout l'�cran
        table.add(lAffichageScore) ;
        table.row();
        table.add(imagePerdu);
        table.row() ;    //Place le texte en dessous de l'image
        celluleTexteATrouver = table.add(lTexteATrouver).align(Align.center).width(Pendu.getLargeurEcran()) ;
        table.setVisible(true) ;

        stage.addActor(table) ;
        
        imagePerdu.addListener(new ClickListener() {
//            Pendu jeu ;
            public void clicked(InputEvent event, float x, float y) {
/*                if (jeu.getDebugState()) {
                    System.out.println("Retour au menu");
                }*/
                Pendu.ecranPerdu.RetourMenu() ;
            }
        });
    	
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
    	jeu.setHauteurEcran(height) ;
    	jeu.setLargeurEcran(width) ;
    	if (Pendu.getDebugState()) Gdx.app.log("Redimmensionnement vers ",width+" x "+height);
    	
   		celluleTexteATrouver.width(width) ;
   		lAffichageScore.setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getHauteurEcran(),TAILLES_POLICE_ADAPTEES));	//Adapte la taille de la police � la hauteur de l'affichage
   		lTexteATrouver.setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getHauteurEcran(),TAILLES_POLICE_ADAPTEES));	//Adapte la taille de la police � la hauteur de l'affichage
    	
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
    	if (Pendu.getDebugState()) Gdx.app.log("EcranPerdu","show");
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

    
    /**
     * Elimine les r�f�rences statiques aux objets car si l'application est relanc�e juste apr�s �tre quitt�e, toutes les r�f�rences statiques
     * demeurent alors que les objets (�cran, widgets... ) sont eux d�truits
     */
    @Override
    public void dispose() {
    	jeu.setEcranPerdu(null) ;	//Supprime la r�f�rence � l'�cran pour l'obliger � �tre re-cr�e la prochaine fois
    	stage.dispose();
    }
}
