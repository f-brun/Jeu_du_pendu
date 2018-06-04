package fr.jeux.pendu;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;

import android.content.res.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Pendu extends ApplicationAdapter {

    public Config cfg;

    protected TextButton boutton;
    protected Label titre;
    protected Image imageTitre;

    SpriteBatch batch;
    Texture img;
    Partie partie;

    @Override
    public void create() {
        cfg = new Config();

        cfg.tMenu = new Table();
        cfg.tMenu.setFillParent(true);  //La table occupe tout l'écran
        cfg.stage.addActor(cfg.tMenu);

        if (cfg.DEBUG) cfg.tMenu.setDebug(true); // This is optional, but enables debug lines for tables.

        boutton = new TextButton("Jouer", cfg.skin);
        titre = new Label("Jeu du pendu\n", cfg.skin);
        titre.setFontScale(3);	//Augmente la taille de la police
        img = new Texture("images/pendu11.png");
        imageTitre = new Image(img);

        cfg.tMenu.pad(3);
        cfg.tMenu.add(titre);
        cfg.tMenu.row();    //Indique que l'élément suivant sera sur une ligne supplémentaire
        cfg.tMenu.add(boutton);
        cfg.tMenu.row();    //Indique que l'élément suivant sera sur une ligne supplémentaire
        cfg.tMenu.add(imageTitre);

                try {   //On attend 0,5 s pour que le mot complet ai le temps de s'afficher
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
        
    
        boutton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor acteur) {
                TextButton boutton;
                if (acteur instanceof TextButton) {
                    boutton = (TextButton) acteur;
                    cfg.partie = new Partie(cfg,1);
                }
            }
        });
      
        ChargeImagesPendu();
        cfg.listeMots = new ArrayList<String>();	//Initialise la liste pour les mots à deviner
        try {
            RempliListeMot(cfg.FICHIER_DICTIONNAIRE);
        } catch (FileNotFoundException e) {
            System.out.println("catch #1 :" + e.toString());
        }
/*        try {
            EcritListeMots() ;
        }
        catch (Exception e) {
        }*/
    }

    void ChargeImagesPendu() {
        int i;	//Compteur d'image indice de tableau
        for (i = 0; i < cfg.NB_IMAGES; i++) {
            if (i < 10) {
                cfg.imagePendu[i] = new Texture(Gdx.files.internal(cfg.CHEMIN_FICHIERS + cfg.PREFIXE_FICHIERS_IMAGES + "0" + i + ".png"));
            }
            if (i > 9) {
                cfg.imagePendu[i] = new Texture(Gdx.files.internal(cfg.CHEMIN_FICHIERS + cfg.PREFIXE_FICHIERS_IMAGES + i + ".png"));
            }
        }
    }

    void RempliListeMot(String fichier) throws FileNotFoundException {
        boolean finLecture ;
        String ligne;
        cfg.nombreMotsDico = 0;
        BufferedReader reader = null;
        FileHandle handle ;
        boolean test ;
        test = Gdx.files.internal(fichier).exists() ;
        handle = Gdx.files.internal(fichier) ;
        if (handle!=null) {
            ligne = handle.readString();
        }
        try {
            reader = new BufferedReader(Gdx.files.internal(fichier).reader(),cfg.TAILLE_BUFFER);
        } catch (Exception e) {
            reader = null ;
        }

        System.out.println("Debut lecture dico");
        Gdx.app.log("INFO", "Debut lecture dico");
        
        finLecture = false ;
        while (!finLecture) {
            ligne = null;
            try {
                ligne = reader.readLine();
                //                 ligne = Normalizer.normalize(ligne, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", ""); //Retire les accents
            } catch (Exception e) {
                finLecture = true ;
            }
            if (ligne == null) {
                finLecture = true ;
                if (cfg.DEBUG) {
                    System.out.println("Fin de fichier !");
                }
            } else {
                cfg.listeMots.add(ligne);
            }
        }
        cfg.nombreMotsDico = cfg.listeMots.size() ;
    }

    void EcritListeMots() throws IOException {
        FileWriter f = new FileWriter("D:/Dictionnaire.txt") ;
        try {
            for (int i = 0 ; i<cfg.listeMots.size();i++) {
                f.write(cfg.listeMots.get(i)+"\n");
            }
            f.flush();
            f.close();
            System.out.println("fichier dico cree sans pb");
        }
        catch (Exception e) {
            System.out.println("Erreur écriture fichier !");
            }
        
        
            
        }
         
       
    @Override
    public void render() {
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cfg.stage.act(Gdx.graphics.getDeltaTime());
        cfg.stage.draw();
    }

    public void resize(int width, int height) {
        cfg.stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
//        batch.dispose();
        img.dispose();
    }
}
