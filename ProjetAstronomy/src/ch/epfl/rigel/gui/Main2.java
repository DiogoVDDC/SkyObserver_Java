package ch.epfl.rigel.gui;


import java.time.LocalDate;
import java.util.function.UnaryOperator;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class Main2 extends Application{

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Rigel");
		primaryStage.setMinWidth(800);
		primaryStage.setMinHeight(600);
		BorderPane scene = new BorderPane();
		scene.setTop(controlBar());
		primaryStage.setScene(new Scene(scene));
		primaryStage.show();
	}

	private static HBox controlBar() {
		
		//Lat and lon labels.
		Label lonL = new Label("Longitude(°):");
		Label latL = new Label("Latitude(°):");
		//latitude text field
		TextField latTF = new TextField();
		latTF.setTextFormatter(textFormatter("lat"));
		latTF.setStyle("-fx-pref-width: 60; -fx-alignment: baseline_right;");
		latTF.setText("46.52");
		//Longitude text field
		TextField lonTF = new TextField();
		lonTF.setTextFormatter(textFormatter("lon"));
		lonTF.setStyle("-fx-pref-width: 60; -fx-alignment: baseline_right;");
		lonTF.setText("6.57");
		HBox obsPos = new HBox(lonL, lonTF, latL, latTF);
		obsPos.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
		
		//Date label and picker.
		Label dateL = new Label("Date:");
		DatePicker datePicker = new DatePicker(LocalDate.now());
		datePicker.setStyle("-fx-pref-width: 75; -fx-alignment: baseline-right;");
		
		//Time label and text field.
		Label timeL = new Label("Heure:");
		TextField timeTF = new TextField("oui");
		
		HBox obsTime = new HBox(dateL, datePicker, timeL, timeTF);
		obsTime.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
		
		// Binding the obsever's location to the lon and lat text fields.
		ObserverLocationBean obsLoc = new ObserverLocationBean();
		obsLoc.latDegProperty().bind(textFormatter("lat").valueProperty());
		obsLoc.lonDegProperty().bind(textFormatter("lon").valueProperty());
		
		HBox controlBar = new HBox(obsPos, obsTime);
		controlBar.setStyle("-fx-spacing: 4; -FX-PADDING: 4;");
		return controlBar;
	}

	/**
	 * Allows to create text formatter for the lattitude of longitude.
	 * @param latOrlon: "lat" or "lon".
	 * @return: the text formatter for the lattitude of longitude.
	 */
	static private TextFormatter<Number> textFormatter(String latOrlon){
		NumberStringConverter strinConverter = new NumberStringConverter("#0.00");
		UnaryOperator<TextFormatter.Change> filter = (change ->{
			try {
				String newText = change.getControlNewText();
				double newLonDeg = strinConverter.fromString(newText).doubleValue();
				if (latOrlon.equals("lon")) {
					return GeographicCoordinates.isValidLonDeg(newLonDeg)
							? change
							: null;
				} else if(latOrlon.equals("lat")) {
					return GeographicCoordinates.isValidLatDeg(newLonDeg)
							? change
							: null;
				} else throw new IllegalArgumentException();
			} catch (Exception e) {
				return null;
			}
		});
		return new TextFormatter<Number>(strinConverter, 0, filter);
	}
}
