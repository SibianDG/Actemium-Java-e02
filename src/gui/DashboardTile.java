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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Popup;

import java.util.Map;
import java.util.Observer;


public class DashboardTile extends VBox implements InvalidationListener, EventHandler<ActionEvent> {

    DashboardController dashboardController;

    private final Color textColor;
    private final Color backgroundColor;

    @FXML
    private ImageView imageView;

    @FXML
    private Text text;

    public DashboardTile(ImageView imageView, String text, DashboardController dashboardController, int i) {
        this.imageView = imageView;
        this.text = new Text(text);
        this.dashboardController = dashboardController;
        this.textColor = Map.of(0, Color.rgb(23, 61, 120), 1, Color.WHITE).get(i);
        this.backgroundColor = Map.of(0, Color.WHITE, 1, Color.rgb(192, 204, 15)).get(i);

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
        text.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        text.setFill(textColor);
        this.setBackground(new Background(new BackgroundFill(backgroundColor, new CornerRadii(50), null)));
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

