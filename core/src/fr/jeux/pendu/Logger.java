package fr.jeux.pendu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.badlogic.gdx.Gdx;
/**
 * Classe de gestion d'un fichier de log qui écrit simplement les information qu'on lui transmet à la fin du fichier de log.
 * @author Florent Brun
 *
 */
public class Logger {

	private static final String NOM_FICHIER_LOG = "log.txt" ;	//Nom par défaut du fichier de log
	
	private BufferedWriter	writer ;
	
	public Logger() {
		initLogger(NOM_FICHIER_LOG) ;
	}

	public Logger(String nomFichierLog) {
		initLogger(nomFichierLog) ;
	}

	private void initLogger(String nomFichierLog) {
		writer = new BufferedWriter(Gdx.files.local(nomFichierLog).writer(true)) ;	//Ouvre le fichier en écriture en mode append
	}
	
	/**
	 * Ecrit  la chaîne d'information en argument dans le fichier de log en la faisant précéder d'un horodatage
	 * @param information Chaine à écrire dans le log
	 */
	public void ecritLog(String information) {
		try {
			writer.newLine();
			writer.write(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.FRANCE).format(new Date()));	//Fabrique l'hotodatage
			writer.write(information) ;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void fermeture() {
		if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
}
