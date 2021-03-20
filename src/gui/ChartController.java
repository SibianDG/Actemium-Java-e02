package gui;

import gui.controllers.GuiController;
import gui.viewModels.ChartViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ChartController extends GuiController {

    @FXML
    private GridPane gridCharts;

    @FXML
    private Button btnPowerBi;

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

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Ticket Statuses");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount of Tickets");

        BarChart<String, Integer> barChart = new BarChart(xAxis, yAxis);

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
    void btnPowerBiOnAction(ActionEvent event) throws IOException {
        Desktop.getDesktop().open(new File("src/powerBi/PowerBiTest.pbix"));
    }
}
