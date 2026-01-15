package dirogue.example.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

/**
 * Vue pour le mode de replay.
 * Cette vue permet la visualisation d'un rapport sauvegardé avec des
 * fonctionnalités de lecture.
 */
public class ReplayView extends ViewBase {

    /**
     * Conteneur principal de la vue de replay.
     */
    private VBox rootPane;

    /**
     * Label pour afficher le message associé à la rencontre en cours.
     */
    private Label messageLabel;

    /**
     * ImageView pour afficher l'image correspondant à la rencontre en cours.
     */
    private ImageView imageView;

    /**
     * Bouton pour passer à la rencontre suivante dans le rapport.
     */
    private Button forwardButton;

    /**
     * Bouton pour revenir à la rencontre précédente dans le rapport.
     */
    private Button backwardButton;

    /**
     * Bouton pour quitter le mode de replay.
     */
    private Button exitButton;

    /**
     * Constructeur par défaut pour la vue de replay.
     */
    public ReplayView() {
        super();
    }

    /**
     * Retourne le nom de la vue de replay.
     *
     * @return Le nom de la vue de replay ("Replay").
     */
    @Override
    public String getName() {
        return "Replay";
    }

    /**
     * Renvoie la racine (root) de la vue de replay.
     *
     * @return La racine (root) de la vue de replay.
     */
    @Override
    public Parent getRoot() {
        return rootPane;
    }

    /**
     * Renvoie le label pour afficher le message de la rencontre.
     *
     * @return Le label pour afficher le message.
     */
    public Label getMessageLabel() {
        return messageLabel;
    }

    /**
     * Renvoie l'ImageView pour afficher l'image de la rencontre.
     *
     * @return L'ImageView pour afficher l'image.
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * Renvoie le bouton pour passer à la rencontre suivante.
     *
     * @return Le bouton pour avancer dans le rapport.
     */
    public Button getForwardButton() {
        return forwardButton;
    }

    /**
     * Renvoie le bouton pour revenir à la rencontre précédente.
     *
     * @return Le bouton pour reculer dans le rapport.
     */
    public Button getBackwardButton() {
        return backwardButton;
    }

    /**
     * Renvoie le bouton pour quitter le mode de replay.
     *
     * @return Le bouton pour sortir du mode replay.
     */
    public Button getExitButton() {
        return exitButton;
    }

    /**
     * Crée l'interface utilisateur pour la vue de replay.
     * Initialise les éléments graphiques tels que les boutons, l'image, et le label.
     */
    @Override
    protected void createUI() {
        throw new UnsupportedOperationException("TODO");
    }
}
