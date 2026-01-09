package dirogue.example.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

/**
 * Vue principale de l'application.
 * Cette vue affiche les éléments nécessaires pour charger un rapport et rejouer
 * celui-ci.
 */
public class MainView extends ViewBase {
    /**
     * Racine de la vue principale.
     */
    private VBox root;

    /**
     * Bouton pour charger un rapport.
     */
    private Button loadButton;

    /**
     * Bouton pour rejouer le rapport chargé.
     */
    private Button replayButton;

    /**
     * Zone de texte pour afficher le rapport.
     */
    private TextArea textArea;

    /**
     * Constructeur par défaut pour la vue principale.
     */
    public MainView() {
        super();
    }

    /**
     * Retourne le nom de la vue principale.
     *
     * @return Le nom de la vue principale ("Main").
     */
    @Override
    public String getName() {
        return "Main";
    }

    /**
     * Renvoie la racine (root) de la vue principale.
     *
     * @return La racine (root) de la vue principale.
     */
    @Override
    public Parent getRoot() {
        return root;
    }

    /**
     * Renvoie le bouton de chargement de rapport.
     *
     * @return Le bouton de chargement.
     */
    public Button getLoadButton() {
        return loadButton;
    }

    /**
     * Renvoie le bouton de replay pour rejouer le rapport chargé.
     *
     * @return Le bouton de replay.
     */
    public Button getReplayButton() {
        return replayButton;
    }

    /**
     * Renvoie la zone de texte pour afficher le rapport.
     *
     * @return La zone de texte.
     */
    public TextArea getTextArea() {
        return textArea;
    }

    /**
     * Crée l'interface utilisateur pour la vue principale.
     * Initialise les éléments graphiques tels que les boutons et la zone de texte.
     */
    @Override
    protected void createUI() {
        root = new VBox();
        root.setPadding(new Insets(10));

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);

        loadButton = new Button("Load");
        replayButton = new Button("Replay");
        replayButton.setDisable(true);

        buttonBox.getChildren().addAll(loadButton, replayButton);
        root.getChildren().addAll(buttonBox, new Label("Report"));

        textArea = new TextArea();
        textArea.setPrefHeight(400);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        root.getChildren().add(textArea);
    }
}
