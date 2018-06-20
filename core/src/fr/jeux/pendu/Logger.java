package fr.jeux.pendu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.badlogic.gdx.Gdx;

public class Logger {

	private static final String FICHIER_LOG = "log.txt" ;
	private BufferedWriter	writer ;
	
	public Logger() {
		writer = new BufferedWriter(Gdx.files.local(FICHIER_LOG).writer(true)) ;	//Ouvre le fichier en écriture en mode append
		
	}
	
	public void ecritLog(String joueur,int score, int niveau, long temps, String dico) {
		try {
			writer.newLine();
			writer.write(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.FRANCE).format(new Date()));
			writer.write(";Niveau "+niveau+";"+joueur+";"+score+";"+temps+";"+dico) ;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void fermeture() {
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
