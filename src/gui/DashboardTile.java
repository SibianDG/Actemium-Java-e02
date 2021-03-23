package gui;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.Map;


public class DashboardTile extends VBox {

    private final Color textColor;
    private final Color backgroundColor;

    @FXML
    private ImageView imageView;

    @FXML
    private Text text;

    public DashboardTile(ImageView imageView, String text, int i) {
        this.imageView = imageView;
        this.text = new Text(text);
        this.textColor = Map.of(0, Color.rgb(23, 61, 120), 1, Color.WHITE).get(i);
        this.backgroundColor = Map.of(0, Color.WHITE, 1, Color.rgb(192, 204, 15)).get(i);
        this.getStyleClass().add("dashboardTile");
        initialize();
    }

    public void initialize(){
        this.getChildren().addAll(imageView, text);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(25);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        text.setFill(textColor);
        text.setTextAlignment(TextAlignment.CENTER);
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

}

