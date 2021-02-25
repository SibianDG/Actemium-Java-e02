package gui.controllers;

import java.io.IOException;

import domain.facades.Facade;
import gui.DashboardFrameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public abstract class GuiController extends GridPane {

	private Facade facade;
	private GuiController parentController;
	private DashboardFrameController dashboardFrameController;

	protected AnchorPane loadFXML(String relativeFXMLPath, GuiController controllerToAppoint, Facade facadeToAppoint) {
		try {

			// The FXML that will be loaded will be a child of "this"
			controllerToAppoint
					.injectParentController(this)
					.injectFacade(facadeToAppoint)
					.injectMainController(this.getMainController());

			// load the AnchorPane and set it's controller
			FXMLLoader loader = new FXMLLoader(GuiController.class.getResource("/gui/" + relativeFXMLPath));
			loader.setController(controllerToAppoint);
			return (AnchorPane) loader.load();

		} catch (IOException e) {
			System.err.println("FXML not loaded");
			e.printStackTrace();
			return new AnchorPane(new Label("Error loading..."));
		}
	}

	/*
	 * Injections return "this" after injecting to make chainable
	 */

	protected GuiController injectParentController(GuiController controller) {
		this.parentController = controller;
		return this;
	}

	protected GuiController injectFacade(Facade facade) {
		this.facade = facade;
		return this;
	}

	protected GuiController injectMainController(DashboardFrameController dfc) {
		this.dashboardFrameController = dfc;
		return this;
	}

	/*
	 * Getters
	 */
	protected Facade getFacade() {
		return facade;
	}

	protected GuiController getParentController() {
		return parentController;
	}

	protected DashboardFrameController getMainController() {
		return dashboardFrameController;
	}

}
