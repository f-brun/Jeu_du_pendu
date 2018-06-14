package fr.jeux.pendu;


public class Niveau {
	private static int nbNiveauxCrees = 0 ;
	public int numero ;		//Numéro interne du niveau (en partant de zéro)
	public String denomination ;
	public boolean minuteur ;
	public boolean minuteurFaitPerdre ;
	public float dureeMinuteur ;	//Durée du minuteur en secondes
	public float[][] gains ;	//Tableau contenant les gains pour chaque pourcentage du temps restant sur le minuteur	
	public int nbErreursMax ;
	public int[] imagesPendaison ;	//Liste des numéros des images pour la pendaison (quel numéro d'image correspond à l'étape x de la pendaison)

	
	public Niveau(String denomination, boolean minuteur, boolean minuteurFaitPerdre, float dureeMinuteur, float[][] gains, int nbErreursMax,int[] imagesPendaison) {
		this.denomination = denomination ;
		this.minuteur = minuteur ;
		this.minuteurFaitPerdre = minuteurFaitPerdre ;
		this.dureeMinuteur = dureeMinuteur ;
		this.gains = gains ;
		this.nbErreursMax = nbErreursMax ;
		this.imagesPendaison = imagesPendaison ;
		this.numero = nbNiveauxCrees++ ;	//Numérote le niveau et incrémente le nombre de niveaux crées
	}
	
	public Niveau() {
		numero = 0 ;
		denomination = "defaut" ;
		minuteur = false ;
		dureeMinuteur = 60 ;
		gains[0][0] = 0f;
		gains[1][0] = 10f ;
		nbErreursMax = Pendu.imagePendu.length ;
		for (int i = 0 ; i < Pendu.imagePendu.length ; i++) imagesPendaison[i] = i ;
		this.numero = nbNiveauxCrees++ ;	//Numérote le niveau et incrémente le nombre de niveaux crées
	}
	
	public Niveau(String nomNiveau) {
		this() ;		//Constructeur par défaut
		denomination = nomNiveau ;	//On définit juste le nom du niveau
	}
	
    public int calculeGain(float pourcentage) {
    	int i ;
    	for (i = 0 ; i < gains[0].length ; i++) {	//On parcours le tableau des pourcentage pour savoir quand on a un pourcentage de temps restant supérieur
    		if (pourcentage > gains[0][i]) break ;
    	}
    	//Dans le pire des cas on a parcouru tout le tableau sans succès et on a i qui est égal à la taille du tableau des pourcentages, ce qui correspond à la dernière case du tableau des gains
    	return (int) gains[1][i] ;
    }
}
