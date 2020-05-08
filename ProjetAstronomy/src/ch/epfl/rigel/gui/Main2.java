package ch.epfl.rigel.gui;


import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.function.UnaryOperator;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

public class Main2 extends Application{

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		ViewingParametersBean viewParam = new ViewingParametersBean();
		DateTimeBean dateTimeB = new DateTimeBean();
		ObserverLocationBean obsLocB = new ObserverLocationBean();
		TimeAnimator animator = new TimeAnimator(dateTimeB);
	
		
		primaryStage.setTitle("Rigel");
		primaryStage.setMinWidth(800);
		primaryStage.setMinHeight(600);
		BorderPane scene = new BorderPane();
		scene.setTop(controlBar(viewParam, dateTimeB, obsLocB, animator));
		primaryStage.setScene(new Scene(scene));
		primaryStage.show();
	}

	private HBox controlBar(ViewingParametersBean viewParam, DateTimeBean dateTimeB, 
			ObserverLocationBean obsLocB, TimeAnimator animator) throws IOException {
		
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
		// Child containing the location of the observation.
		HBox obsPos = new HBox(lonL, lonTF, latL, latTF);
		obsPos.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
		
		//Date label and picker.
		Label dateL = new Label("Date:");
		DatePicker datePicker = new DatePicker(LocalDate.now());
		datePicker.setStyle("-fx-pref-width: 120;");
		
		//Time label and text field.
		Label timeL = new Label("Heure:");
		TextField timeTF = new TextField();
		timeTF.setStyle("-fx-pref-width: 75; -fx-alignment: baseline-right;");
		DateTimeFormatter hmsFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalTimeStringConverter stringConverter = new LocalTimeStringConverter(hmsFormatter, hmsFormatter);
		TextFormatter<LocalTime> timeFormatter = new TextFormatter<>(stringConverter);
		timeTF.setTextFormatter(timeFormatter);
		timeTF.setText(LocalTime.now().truncatedTo(ChronoUnit.SECONDS).toString());
		
		//Zone id selector.
		ComboBox<String> zoneIdBox = new ComboBox<String>(FXCollections.observableArrayList(ZoneId.getAvailableZoneIds()));
		zoneIdBox.setStyle("-fx-pref-width: 180;");
		zoneIdBox.setValue(ZoneId.systemDefault().toString());
		
		//Child containg the time selection.
		HBox obsTime = new HBox(dateL, datePicker, timeL, timeTF, zoneIdBox);
		obsTime.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
		
		// Accelerator choices menu.
		ChoiceBox<NamedTimeAccelerator> acceleratorChoice = new ChoiceBox<NamedTimeAccelerator>(
					FXCollections.observableArrayList(NamedTimeAccelerator.values()));
		acceleratorChoice.setValue(NamedTimeAccelerator.TIMES_300);
		
		// Loading font for the button images.
		InputStream fontStream = getClass().getResourceAsStream("/Font Awesome 5 Free-Solid-900.otf");
		Font fontAwesome = Font.loadFont(fontStream, 15);
		fontStream.close();
		
		//Reset and Play/Pause buttons.
		Button resetButton = new Button(ButtonImages.RESTART.getUniCode());
		resetButton.setFont(fontAwesome);
		
		Button playPauseButt = new Button(ButtonImages.PLAY.getUniCode());
		playPauseButt.setFont(fontAwesome);
		playPauseButt.setOnAction(e ->{
			// Starts and stop the animation depending on whether it's already running or not.
			if (animator.isRunning()) {
				animator.stop();
			} else {
				animator.start();
			}
			// Switches between the 2 button images.
			if(playPauseButt.getText().equals(ButtonImages.PAUSE.getUniCode())) {
				playPauseButt.setText(ButtonImages.PLAY.getUniCode());
			} else {
				playPauseButt.setText(ButtonImages.PAUSE.getUniCode());
			}
		});
		
		// Child box containing the time spending.
		HBox time = new HBox(acceleratorChoice, resetButton, playPauseButt);
		time.setStyle("-fx-spacing: inherit;");
		
		// Binding the obsever's location to the lon and lat text fields.
		obsLocB.latDegProperty().bind(textFormatter("lat").valueProperty());
		obsLocB.lonDegProperty().bind(textFormatter("lon").valueProperty());
		// Binding the date, time and zone to the dateTimeBean property.
		dateTimeB.dateProperty().bind(datePicker.valueProperty());
		dateTimeB.timeProperty().bind(timeFormatter.valueProperty());
		ObservableObjectValue<ZoneId> sb = Bindings.createObjectBinding(() -> ZoneId.of(zoneIdBox.getValue()), zoneIdBox.valueProperty());
		dateTimeB.zoneProperty().bind(sb);
		
		// Binding the time accelerator to the selected accelerator.
		animator.acceleratorProperty().bind(Bindings.select(acceleratorChoice.valueProperty(), "accelerator"));

		HBox controlBar = new HBox(obsPos, obsTime, time);
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
	
	/**
	 * Enum storing the unicode string for the different button images.
	 * @author Theo Houle (312432)
	 *
	 */
	private enum ButtonImages {
		RESTART("\uf0e2"),
		PLAY("\uf04b"),
		PAUSE("\uf04c");
		
		// String containing the unicode code of the image.
		private final String unicode;
		
		private ButtonImages(String unicode) {
			this.unicode = unicode;
		}
		
		/**
		 * Unicode getter
		 * @return: the unicode string.
		 */
		public String getUniCode() {
			return unicode;
		}
	}
}
