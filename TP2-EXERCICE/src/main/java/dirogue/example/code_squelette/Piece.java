package dirogue.example.code_squelette;

import java.io.Serializable;

import dirogue.example.rencontre.Rencontre;

/**
 * Répresente un lieu dans le labyrinthe: une grotte, une salle, une chambre, une
 * cave, etc.
 * 
 * NB: NE PAS CHANGER CE FICHIER
 * 
 * @author Michalis Famelis
 *
 */
public class Piece implements Serializable{

	private int id;

	private Rencontre rencontre;

	/**
	 * Crée un nouvel objet Piece avec un RencontreType donné.
	 * 
	 * Exemple:
	 * 
	 * <pre>
	 * Piece trésorerie = Piece(34, RencontreType.TRESOR);
	 * </pre>
	 * 
	 * @param i - numéro identificateur -- on suppose qu'il est unique et que
	 *          id==0 est reservé pour l'Exterieur
	 * @param r - une des possibles valeurs de l'enumération {@link RencontreType}
	 */
	public Piece(int i, RencontreType r) {
		this.id = i;
		this.rencontre = Rencontre.getInstance(r);
	}

	/**
	 * Retourne ce qui peut être rencontré à ce lieu.
	 * 
	 * @return - une des possibles valeurs de l'enumération {@link RencontreType}
	 */
	public Rencontre getRencontre() {
		return rencontre;
	}

	/**
	 * Une représentation très abstraite d'une Piece.
	 */
	public String toString() {
		return "<" + Integer.toString(this.id) + "-" + this.rencontre.toString() + ">";
	}

	/**
	 * Retourne l'ID du Piece.
	 * 
	 * @return l'id.
	 */
	public int getID() {
		return this.id;
	}

}
