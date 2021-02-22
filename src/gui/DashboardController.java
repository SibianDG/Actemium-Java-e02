package gui;

import domain.DomainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Light;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.IntStream;

public class DashboardController extends GridPane {
    private DomainController domainController;

    @FXML
    private GridPane gridDashboard;

    @FXML
    private GridPane gridContent;

    @FXML
    private ImageView imgNotifications;

    @FXML
    private ImageView imgMyAccount;

    @FXML
    private Text txtName;

    @FXML
    private Text txtRole;

    @FXML
    private ImageView imgLogout;

    @FXML
    private Text txtTitle;

    //private List<>

    public DashboardController(DomainController domainController) throws FileNotFoundException {
        super();
        this.domainController = domainController;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }

        //TODO creating dashboard dynamically based on user role (dashboard buttons)???

        initializeText();

    }

    public void initializegridPane() throws FileNotFoundException {
        initialize_gridPane(3, 2);

        String[] itemNames = {"manage Contract","consult Knowledge base", "create Ticket", "outstanding tickets", "resolved tickets", "statistics"};
        String[] itemImages = {"icon_manage.png","icon_consult.png", "icon_create.png", "icon_outstanding.png", "icon_resolved.png", "icon_statistics.png"};

        for (int i = 0; i < 6; i++) {
            addDashboardItem(itemNames[i], createImageView(itemImages[i]), i%3, i/3);
        }
    }

    public ImageView createImageView(String image) throws FileNotFoundException {

        FileInputStream input = new FileInputStream("src/pictures/dashboard/" + image);

        ImageView imageView = new ImageView(new Image(input));
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        return imageView;


    }

    private void addDashboardItem(String name, ImageView image, int x, int y) {
        gridContent.add(image, x, y);
    }

    private void initialize_gridPane(int x, int y) {
        // initalize central gridpane
    	
    	double height = gridContent.getHeight()/y;
    	double width = gridContent.getWidth()/x;

        for (int i = 0; i < x; i++) {
            gridContent.addColumn(i);
            gridContent.getColumnConstraints().add(new ColumnConstraints(width, height, -1, Priority.ALWAYS, HPos.CENTER, false));
        }
        for (int i = 0; i < y; i++) {
            gridContent.addRow(i);
            gridContent.getRowConstraints()
                    .add(new RowConstraints(width, height, -1, Priority.ALWAYS, VPos.CENTER, false));
        }

        for (Node node : gridContent.getChildren()) {
            node.minWidth(300);
            node.minHeight(300);
            node.maxHeight(300);
            node.maxHeight(300);
            node.prefWidth(300);
            node.prefHeight(300);
        }

    }

    @FXML
    void bnLogoutOnActon(ActionEvent event) {

    }

    private void initializeText() {
        //txtName.setText(String.format("%s %s" , domainController.giveUserFirstName(), domainController.giveUserLastName()));
        //txtRole.setText(domainController.giveUserType());
    }



    
    private void loadScene(String title, Object controller) { // method for switching to the next screen
        Scene scene = new Scene((Parent) controller);
        Stage stage = (Stage) this.getScene().getWindow();
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

}
