package gui;

import gui.controllers.GuiController;
import gui.viewModels.ChartViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import languages.LanguageResource;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class ChartController extends GuiController {

    @FXML
    private GridPane gridContent;

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

    private DashboardFrameController dashboardFrameController;
    private ChartViewModel chartViewModel;

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

        initializeBarChart();
    }

    private void initializeBarChart() {

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
        gridCharts.getChildren().clear();

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Months");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount of Contracts");

        BarChart<String, Integer> barChart = new BarChart(xAxis, yAxis);
        barChart.setTitle("Contracts created per month for " + LocalDate.now().getYear());

        Map<String, Integer> data = chartViewModel.barChartMonthlyContracts();

        XYChart.Series<String, Integer> dataSeries = new XYChart.Series<>();
        dataSeries.setName(Integer.toString(LocalDate.now().getYear()));
        data.forEach((k, v) -> dataSeries.getData().add(new XYChart.Data(k, v)));
        barChart.getData().add(dataSeries);

        gridCharts.add(barChart, 0,0);
    }

    @FXML
    void btnContractTypeOnAction(ActionEvent event) {
        gridCharts.getChildren().clear();

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Contract Types");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount of Contracts");

        BarChart<String, Integer> barChart = new BarChart(xAxis, yAxis);
        barChart.setTitle("Amount of Contract per Type");

        Map<String, Integer> data = chartViewModel.barChartAmountOfContractsPerType();

        XYChart.Series<String, Integer> dataSeries = new XYChart.Series<>();
        dataSeries.setName("Amount of contracts");
        data.forEach((k, v) -> {
            dataSeries.getData().add(new XYChart.Data(k, v));
        });
        barChart.getData().add(dataSeries);

        gridCharts.add(barChart, 0,0);
    }


    @FXML
    void btnTechnicianOnAction(ActionEvent event) {
        gridCharts.getChildren().clear();

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Technicians");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount of Tickets");

        StackedBarChart<String, Integer> barChart = new StackedBarChart(xAxis, yAxis);
        barChart.setTitle("Amount of tickets asigned to a Technician");

        Map<Long, Integer> data1 = chartViewModel.barChartDataAmountOfResolvedTicketsAsignedToTechnician();
        Map<Long, Integer> data2 = chartViewModel.barChartDataAmountOfOutstandingTicketsAsignedToTechnician();

        XYChart.Series<String, Integer> dataSeries1 = new XYChart.Series<>();
        dataSeries1.setName("Resolved");
        data1.forEach((k, v) -> {
            dataSeries1.getData().add(new XYChart.Data(chartViewModel.getnameOftechnicianByID(k), v));
        });
        barChart.getData().add(dataSeries1);

        XYChart.Series<String, Integer> dataSeries2 = new XYChart.Series<>();
        dataSeries2.setName("Outstanding");
        data2.forEach((k, v) -> {
            dataSeries2.getData().add(new XYChart.Data(chartViewModel.getnameOftechnicianByID(k), v));
        });
        barChart.getData().add(dataSeries2);


        gridCharts.add(barChart, 0, 0);
    }

    @FXML
    void btnTicketOnAction(ActionEvent event) {
        gridCharts.getChildren().clear();

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Ticket Statuses");
        xAxis.setTickLabelsVisible(false);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount of Tickets");

        BarChart<String, Integer> barChart = new BarChart(xAxis, yAxis);
        barChart.setTitle("Amount of tickets per status");

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
        gridCharts.getChildren().clear();

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Months");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount of Tickets");

        BarChart<String, Integer> barChart = new BarChart(xAxis, yAxis);
        barChart.setTitle("Tickets per month for " + LocalDate.now().getYear());

        Map<String, Integer> data = chartViewModel.barChartMonthlyTickets();

        XYChart.Series<String, Integer> dataSeries = new XYChart.Series<>();
        dataSeries.setName(Integer.toString(LocalDate.now().getYear()));
        data.forEach((k, v) -> dataSeries.getData().add(new XYChart.Data(k, v)));
        barChart.getData().add(dataSeries);

        gridCharts.add(barChart, 0,0);
    }

    @FXML
    void btnTicketTypeOnAction(ActionEvent event) {
        gridCharts.getChildren().clear();

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Ticket Types");
        xAxis.setTickLabelsVisible(false);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount of Tickets");

        BarChart<String, Integer> barChart = new BarChart(xAxis, yAxis);
        barChart.setTitle("Tickets per Type");

        Map<String, Integer> data = chartViewModel.barChartTicketType();

        data.forEach((k, v) -> {
            XYChart.Series<String, Integer> dataSeries = new XYChart.Series<>();
            dataSeries.setName(k);
            dataSeries.getData().add(new XYChart.Data(k, v));
            barChart.getData().add(dataSeries);
        });

        gridCharts.add(barChart, 0,0);
    }

}
