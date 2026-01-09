package dirogue.example.code_squelette;

/**
 * L'Exterieur est une Piece spéciale. Pour qu'un Labyrinthe ait une entrée, un
 * corridor vers l'Exterieur doit être ajouté via la méthode
 * ajouteEntrée(Exterieur out, Piece e).
 * 
 * <p/>
 * Cette classe utilise le patron de conception «Singleton»: à tout moment, il
 * ne peut y avoir qu'une seule instance de cette classe. Son constructeur est privé,
 * et donc le seul moyen de prendre un objet de type Exterieur est via la
 * méthode statique Exterieur.getExterieur(). Voir: @see <a href=
 * "https://fr.wikipedia.org/wiki/Singleton_(patron_de_conception)">Singleton
 * (patron de conception)</a>
 * 
 * <p/>
 * NB: NE PAS CHANGER CE FICHIER
 * 
 * @author Michalis Famelis
 *
 */

public class Exterieur extends Piece {

	private static final int idExterieur = 0;

	private static Exterieur unSeulExterieur;

	private static RencontreType ceQuiSePasseALExterieur;

	/**
	 * Le constructeur est caché exprès !
	 */
	private Exterieur() {
		super(idExterieur, ceQuiSePasseALExterieur);
	}

	/**
	 * Cette méthode est la seul façon d'accéder à l'unique instance de la classe.
	 * 
	 * @return - l'instance unique de la classe Exterieur.
	 */
	public static Exterieur getExterieur() {
		if (Exterieur.unSeulExterieur == null) {
			ceQuiSePasseALExterieur = RencontreType.RIEN;
			unSeulExterieur = new Exterieur();
		}
		return unSeulExterieur;

	}

	/**
	 * Retourne ce qui se passe à l'exterieur du labyrinthe: C'est à dire
	 * RencontreType.RIEN d'intéressant.
	 * 
	 * @return - le RencontreType de l'Exterieur
	 */
	public static RencontreType getRencontreExterieur() {
		return ceQuiSePasseALExterieur;
	}

}
