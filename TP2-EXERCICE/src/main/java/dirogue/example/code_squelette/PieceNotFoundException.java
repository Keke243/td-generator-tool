package dirogue.example.code_squelette;

/**
 * Exception indiquant qu'une pièce est introuvable dans le labyrinthe.
 * Cette exception est levée lorsque la recherche d'une pièce spécifique échoue.
 * <p/>
 * 
 * NB: NE PAS CHANGER CE FICHIER
 * 
 * @author Michalis Famelis
 *
 */
public class PieceNotFoundException extends Exception {

    /**
     * Constructeur par défaut pour l'exception PieceNotFoundException.
     * Le message par défaut indique que la pièce est introuvable.
     */
    public PieceNotFoundException() {
        super("Piece introuvable.");
    }

    /**
     * Constructeur avec un message personnalisé pour l'exception PieceNotFoundException.
     *
     * @param message Message détaillé concernant la pièce introuvable.
     */
    public PieceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructeur avec un message et une cause pour l'exception PieceNotFoundException.
     *
     * @param message Message détaillé concernant la pièce introuvable.
     * @param cause   Cause de l'exception.
     */
    public PieceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}