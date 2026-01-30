package dirogue.example.rencontre;

import java.util.Random;

import dirogue.example.code_squelette.RencontreType;

/**
 * Classe abstraite représentant une rencontre dans le jeu.
 * Les sous-classes de Rencontre définissent différents types de rencontres,
 * tels que MONSTRE, TRESOR, BOSS, etc.
 */
public abstract class Rencontre {

    /**
     * Méthode statique pour obtenir une instance de Rencontre en fonction du type
     * spécifié.
     *
     * @param type Le type de rencontre à créer.
     * @return Une instance de la sous-classe de Rencontre correspondante au type
     *         spécifié.
     * @throws IllegalArgumentException Si le type de rencontre spécifié est
     *                                  invalide.
     */
    public static Rencontre getInstance(RencontreType type) {
        switch (type) {
            case RIEN:
                return new Rien();
            case MONSTRE:
                int r = new Random().nextInt(3);
                switch (r) {
                    case 0:
                        return new Gargouille();
                    case 1:
                        return new Gobelin();
                    case 2:
                        return new Orque();
                }
            case TRESOR:
                int r1 = new Random().nextInt(3);
                switch (r1) {
                    case 0:
                        return new SacDeButin();
                    case 1:
                        return new Potion();
                    case 2:
                        return new ArtefactMagique();
                }
            case BOSS:
                return new Boss();

            default:
                throw new IllegalArgumentException("Invalid RencontreType...!");
        }
    }

    /**
     * Redéfinition de la méthode toString pour retourner le nom simplifié de la
     * classe.
     *
     * @return Le nom simplifié de la classe.
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    /**
     * Méthode abstraite pour décrire la rencontre actuelle.
     *
     * @return Une description de la rencontre.
     */
    public abstract String rencontre();

    /**
     * Méthode abstraite pour obtenir le sprite associé à la rencontre.
     *
     * @return Le chemin vers le sprite/image représentant la rencontre.
     */
    public abstract String getSprite();

    /**
     * Méthode abstraite pour obtenir le type d'essence de la rencontre (MONSTRE,
     * TRESOR, BOSS, etc.).
     *
     * @return Le type d'essence de la rencontre.
     */
    public abstract RencontreType essence();
}
