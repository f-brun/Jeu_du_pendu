package fr.jeux.pendu;

/**
*
* @author Florent
*/
public class GestionMots {
   static Pendu jeu ;
   public static boolean CompleteMotADeviner(String lettreProposee) {
       boolean lettrePresente;
       lettrePresente = false;
       int pos;	//index dans le mot à deviner
       int i = 0;
       do {
           pos = Pendu.motADeviner.indexOf(lettreProposee, i);	//index de la première occurence de la lettre proposée dans le mot à devinner
           if (pos != -1) {
               lettrePresente = true;
               Pendu.motDevine = Pendu.motDevine.substring(0, pos) + lettreProposee + Pendu.motDevine.substring(pos + 1);
               i = pos;
           }
           i++;
       } while (pos != -1);
       return lettrePresente;
   }

   public static String SepareParDesEspaces(String mot) {
       String motAAfficher;
       motAAfficher = "";
       for (int i = 0; i < mot.length(); i++) {
           motAAfficher += mot.charAt(i);
           if (i != mot.length()-1) {
               motAAfficher += " ";
           }
       }
       return motAAfficher;
   }

   public static void TestSiGagne() {
       if (jeu.motADeviner.equals(jeu.motDevine)) {
           if (jeu.getDebugState()) System.out.println("Gagné !");
       }
   }

}
