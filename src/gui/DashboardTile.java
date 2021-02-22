package gui;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;

import java.util.Observer;

public class DashboardTile extends VBox implements InvalidationListener, EventHandler<ActionEvent> {

    DashboardController dashboardController;

    @FXML
    private ImageView imageView;

    @FXML
    private Text text;

    public DashboardTile(ImageView imageView, String text, DashboardController dashboardController) {
        this.imageView = imageView;
        this.text = new Text(text);
        this.dashboardController = dashboardController;
        initialize();

        dashboardController.addListener(this);

        setOnMouseClicked(e -> {
            //this.text.setText("clicked");
            invalidated(dashboardController);
        });
    }

    private void clicked(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Jow");
        alert.show();
    }

    public void initialize(){
        this.getChildren().addAll(imageView, text);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(25);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    @Override
    public void invalidated(Observable observable) {
        dashboardController.reactionFromTile();
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        dashboardController.reactionFromTile();
    }
}

