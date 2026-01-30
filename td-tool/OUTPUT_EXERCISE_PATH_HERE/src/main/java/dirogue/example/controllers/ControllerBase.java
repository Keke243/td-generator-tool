package dirogue.example.controllers;

import dirogue.example.view.ViewBase;

/**
 * Classe abstraite représentant un contrôleur de base pour une vue dans
 * l'application.
 * Les classes qui héritent de ControllerBase sont responsables de la gestion
 * des actions
 * et des interactions liées à une vue spécifique.
 */
public abstract class ControllerBase {

    /**
     * Vue associée au contrôleur.
     */
    protected ViewBase view;

    /**
     * Données associées au contrôleur.
     */
    protected String data;

    /**
     * Constructeur protégé pour initialiser un ControllerBase avec une vue et des
     * données.
     *
     * @param view La vue associée au contrôleur.
     * @param data Les données associées au contrôleur.
     */
    protected ControllerBase(ViewBase view, String data) {
        this.view = view;
        this.data = data;
        initialize();
    }

    /**
     * Méthode statique pour obtenir le contrôleur approprié pour une vue donnée.
     *
     * @param view La vue pour laquelle obtenir le contrôleur.
     * @param data Les données associées à la vue (optionnelles).
     * @return Le contrôleur correspondant à la vue donnée.
     * @throws IllegalArgumentException Si le nom de la vue est invalide.
     */
    public static ControllerBase getController(ViewBase view, String data) {
        switch (view.getName().toLowerCase()) {
            case "main":
                return new MainController(view);

            case "replay":
                return new ReplayController(view, data);

            default:
                throw new IllegalArgumentException("Invalid view name...!");
        }
    }

    /**
     * Méthode statique pour obtenir le contrôleur approprié pour une vue donnée.
     * Utilise le constructeur avec la vue seule, sans données.
     *
     * @param view La vue pour laquelle obtenir le contrôleur.
     * @return Le contrôleur correspondant à la vue donnée.
     * @throws IllegalArgumentException Si le nom de la vue est invalide.
     */
    public static ControllerBase getController(ViewBase view) {
        return getController(view, null);
    }

    /**
     * Méthode abstraite permettant d'initialiser le contrôleur.
     * Cette méthode doit être implémentée par les classes filles pour initialiser
     * le contrôleur spécifique.
     */
    protected abstract void initialize();
}
