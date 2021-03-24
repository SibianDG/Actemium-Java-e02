package gui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import gui.viewModels.ChartViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import languages.LanguageResource;

public class ChartController extends GridPane {

    @FXML
    private GridPane gridContent;

    @FXML
    private VBox hboxAllButtons;

    @FXML
    private Button btnTicket;

    @FXML
    private Button btnTicketType;

    @FXML
    private Button btnMonthly;

    @FXML
    private Button btnContractType;

    @FXML
    private Button btnContract;

    @FXML
    private Button btnTechnician;

    @FXML
    private Button btnPowerBi;

    @FXML
    private GridPane gridCharts;

    @FXML
    private Button btnAverageTimeTicket;

    private DashboardFrameController dashboardFrameController;
    private final ChartViewModel chartViewModel;

    public ChartController(DashboardFrameController dashboardFrameController, ChartViewModel chartViewModel) {
        super();

        this.dashboardFrameController = dashboardFrameController;
        this.chartViewModel = chartViewModel;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Charts.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        initializePane();
    }

    private void initializePane() {
        Text text = new Text(String.format("%s%n%n%s", LanguageResource.getString("nothingSelected"), LanguageResource.getString("select_a_button")));
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        text.setFill(Color.rgb(23, 61, 120));
        gridCharts.add(text, 0, 0);
        gridCharts.setAlignment(Pos.CENTER);
        setValignment(text, VPos.CENTER);
        setHalignment(text, HPos.CENTER);
        btnTicket.setText(LanguageResource.getString("ticket_status"));
        btnTicketType.setText(LanguageResource.getString("ticket_type"));
        btnMonthly.setText(LanguageResource.getString("monthly_tickets"));
        btnContractType.setText(LanguageResource.getString("contract_types"));
        btnContract.setText(LanguageResource.getString("contracts"));
        btnTechnician.setText(LanguageResource.getString("technicians"));
        btnAverageTimeTicket.setText(LanguageResource.getString("averageTicketTimeButton"));
    }

    @FXML
    void btnPowerBiOnAction(ActionEvent event) throws IOException {
        if (goToStatisticsConfirmationAlert())
            Desktop.getDesktop().open(new File("src/powerBi/PowerBiTest.pbix"));
    }

    private boolean goToStatisticsConfirmationAlert() {
        boolean confirmed;
        String headerText = LanguageResource.getString("goToStatistics_confirmation_header");
        String text = LanguageResource.getString("goToStatistics_confirmation_text");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, text);
        alert.setHeaderText(headerText);
        alert.getDialogPane().getStylesheets().add("file:src/start/styles.css");
        alert.getDialogPane().getStyleClass().add("alert");
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream("/pictures/icon.png")));
        Optional<ButtonType> result = alert.showAndWait();
        confirmed = result.get() == ButtonType.OK;
        return confirmed;
    }

    @FXML
    void btnContractOnAction(ActionEvent event) {
        resetActiveButtonStyle();
        btnContract.getStyleClass().add("btn-blue-active");
        gridCharts.getChildren().clear();

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(LanguageResource.getString("months"));

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(LanguageResource.getString("amountOfContracts"));

        BarChart<String, Integer> barChart = new BarChart(xAxis, yAxis);
        barChart.setTitle(LanguageResource.getString("contractsPerMonth") + LocalDate.now().getYear());

        Map<String, Integer> data = chartViewModel.barChartMonthlyContracts();

        XYChart.Series<String, Integer> dataSeries = new XYChart.Series<>();
        dataSeries.setName(Integer.toString(LocalDate.now().getYear()));
        data.forEach((k, v) -> dataSeries.getData().add(new XYChart.Data(k, v)));
        barChart.getData().add(dataSeries);

        gridCharts.add(barChart, 0,0);
    }

    @FXML
    void btnContractTypeOnAction(ActionEvent event) {
        resetActiveButtonStyle();
        btnContractType.getStyleClass().add("btn-blue-active");
        gridCharts.getChildren().clear();

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(LanguageResource.getString("contractTypes"));

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(LanguageResource.getString("amountOfContracts"));

        BarChart<String, Integer> barChart = new BarChart(xAxis, yAxis);
        barChart.setTitle(LanguageResource.getString("amountofContractperType"));

        Map<String, Integer> data = chartViewModel.barChartAmountOfContractsPerType();

        XYChart.Series<String, Integer> dataSeries = new XYChart.Series<>();
        dataSeries.setName(LanguageResource.getString("amountOfContracts"));
        data.forEach((k, v) -> {
            dataSeries.getData().add(new XYChart.Data(k, v));
        });
        barChart.getData().add(dataSeries);

        gridCharts.add(barChart, 0,0);
    }


    @FXML
    void btnTechnicianOnAction(ActionEvent event) {
        resetActiveButtonStyle();
        btnTechnician.getStyleClass().add("btn-blue-active");
        gridCharts.getChildren().clear();

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(LanguageResource.getString("technicians"));

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(LanguageResource.getString("amountOfTickets"));

        StackedBarChart<String, Integer> barChart = new StackedBarChart(xAxis, yAxis);
        barChart.setTitle(LanguageResource.getString("ticketsTechnician"));

        Map<Long, Integer> data1 = chartViewModel.barChartDataAmountOfResolvedTicketsAsignedToTechnician();
        Map<Long, Integer> data2 = chartViewModel.barChartDataAmountOfOutstandingTicketsAsignedToTechnician();

        XYChart.Series<String, Integer> dataSeries1 = new XYChart.Series<>();
        dataSeries1.setName(LanguageResource.getString("resolved"));
        data1.forEach((k, v) -> {
            dataSeries1.getData().add(new XYChart.Data(chartViewModel.getnameOftechnicianByID(k), v));
        });
        barChart.getData().add(dataSeries1);

        XYChart.Series<String, Integer> dataSeries2 = new XYChart.Series<>();
        dataSeries2.setName(LanguageResource.getString("outstanding"));
        data2.forEach((k, v) -> {
            dataSeries2.getData().add(new XYChart.Data(chartViewModel.getnameOftechnicianByID(k), v));
        });
        barChart.getData().add(dataSeries2);


        gridCharts.add(barChart, 0, 0);
    }

    @FXML
    void btnTicketOnAction(ActionEvent event) {
        resetActiveButtonStyle();
        btnTicket.getStyleClass().add("btn-blue-active");
        gridCharts.getChildren().clear();

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(LanguageResource.getString("ticket_status"));
        xAxis.setTickLabelsVisible(false);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(LanguageResource.getString("amountOfTickets"));

        BarChart<String, Integer> barChart = new BarChart(xAxis, yAxis);
        barChart.setTitle(LanguageResource.getString("amountOfTicketsPerStatus"));

        Map<String, Integer> data = chartViewModel.barChartDataAmountOfTicketsPerStatus();

        data.forEach((k, v) -> {
            XYChart.Series<String, Integer> dataSeries = new XYChart.Series<>();
            dataSeries.setName(k);
            dataSeries.getData().add(new XYChart.Data(k, v));
            barChart.getData().add(dataSeries);
        });

        gridCharts.add(barChart, 0,0);
    }

    @FXML
    void btnMonthlyOnAction(ActionEvent event) {
        resetActiveButtonStyle();
        btnMonthly.getStyleClass().add("btn-blue-active");
        gridCharts.getChildren().clear();

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(LanguageResource.getString("months"));

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(LanguageResource.getString("amountOfTickets"));

        BarChart<String, Integer> barChart = new BarChart(xAxis, yAxis);
        barChart.setTitle(LanguageResource.getString("ticketsPerMonth") + LocalDate.now().getYear());

        Map<String, Integer> data = chartViewModel.barChartMonthlyTickets();

        XYChart.Series<String, Integer> dataSeries = new XYChart.Series<>();
        dataSeries.setName(Integer.toString(LocalDate.now().getYear()));
        data.forEach((k, v) -> dataSeries.getData().add(new XYChart.Data(k, v)));
        barChart.getData().add(dataSeries);

        gridCharts.add(barChart, 0,0);
    }

    @FXML
    void btnTicketTypeOnAction(ActionEvent event) {
        resetActiveButtonStyle();
        btnTicketType.getStyleClass().add("btn-blue-active");
        gridCharts.getChildren().clear();

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(LanguageResource.getString("ticket_type"));
        xAxis.setTickLabelsVisible(false);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(LanguageResource.getString("amountOfTickets"));

        BarChart<String, Integer> barChart = new BarChart(xAxis, yAxis);
        barChart.setTitle(LanguageResource.getString("ticketsperType"));

        Map<String, Integer> data = chartViewModel.barChartTicketType();

        data.forEach((k, v) -> {
            XYChart.Series<String, Integer> dataSeries = new XYChart.Series<>();
            dataSeries.setName(k);
            dataSeries.getData().add(new XYChart.Data(k, v));
            barChart.getData().add(dataSeries);
        });

        gridCharts.add(barChart, 0,0);
    }

    private void resetActiveButtonStyle(){
        hboxAllButtons.getChildren().forEach(b -> b.getStyleClass().remove("btn-blue-active"));
    }

    @FXML
    void btnAverageTimeTicketOnAction(ActionEvent event) {
        resetActiveButtonStyle();
        btnAverageTimeTicket.getStyleClass().add("btn-blue-active");
        gridCharts.getChildren().clear();

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(LanguageResource.getString("ticket"));
        xAxis.setTickLabelsVisible(false);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(LanguageResource.getString("AverageTime"));

        BarChart<String, Double> barChart = new BarChart(xAxis, yAxis);
        barChart.setTitle(LanguageResource.getString("AverageTimeTicket"));

        double data = chartViewModel.chartAverageTimeNeededToSolveTickets();

        XYChart.Series<String, Double> dataSeries = new XYChart.Series<>();
        dataSeries.setName(LanguageResource.getString("solveTicket"));
        dataSeries.getData().add(new XYChart.Data(LanguageResource.getString("ticket"), data));
        barChart.getData().add(dataSeries);

        gridCharts.add(barChart, 0,0);
    }

}
