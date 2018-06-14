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

    Pendu jeu ;	//r�f�rence aux donn�es du jeu
    
    public EcranGagne(Pendu jeuEnCours) {

    	jeu = jeuEnCours ;	//reprend la r�f�rence au jeu pour toutes les m�thodes de la classe
 
    	if (Pendu.getEcranGagne() == null) Pendu.setEcranGagne(this); 	//Ecrit la r�f�rence � l'�cran que l'on vient de cr�er
    	
    	if (stage == null) creeUI() ; //Si c'est le premier appel, on cr�e l'affichage
    	actualiseUI() ;
    }
    private void creeUI() {

       	stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);		//Met le focus sur notre �cran
        
        if (Pendu.getDebugState()) {
            System.out.println("Gagn� !");
        }

        //Cr�e l'affichage du score
       	lAffichageScore = new Label("Score : "+ Integer.toString(Pendu.getScore()),Pendu.getSkin()) ;
        

        //Cr�e le label contenant l'image
       	img = new Texture(Pendu.IMAGE_GAGNE);
       	imageGagne = new Image(img);

        table = new Table() ;

        //D�finit la disposition de la table
        table.pad(3) ;
        table.setFillParent(true);  //La table occupe tout l'�cran
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
                else jeu.setScreen(Pendu.getEcranJeu());	//Bascule sur l'�cran de jeu pour avoir un nouveau mot
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
   		lAffichageScore.setFontScale(jeu.getTaillePoliceTitreAdaptee(Pendu.getHauteurEcran(),TAILLES_POLICE_ADAPTEES));	//Adapte la taille de la police � la hauteur de l'affichage
    	stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
    	if (Pendu.getDebugState()) Gdx.app.log("EcranGagn�","show");
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
    	jeu.setEcranGagne(null) ;	//Supprime la r�f�rence � l'�cran pour l'obliger � �tre re-cr�e la prochaine fois
        img.dispose();
        stage.dispose();
     }
}
