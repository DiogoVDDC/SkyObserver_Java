package ch.epfl.rigel.gui;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.ZonedDateTime;
import java.util.function.UnaryOperator;
import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

public class Main2 extends Application{

	public static void main(String[] args) {
		launch(args);
	}
	
    @Override
    public void start(Stage primaryStage) throws Exception {
        try (InputStream hs = resourceStream("/hygdata_v3.csv"); InputStream ast = resourceStream("/asterisms.txt")){
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hs, HygDatabaseLoader.INSTANCE).loadFrom(ast, AsterismLoader.INSTANCE)
                    .build();

            ZonedDateTime when = ZonedDateTime.parse("2020-02-17T20:15:00+01:00");
            DateTimeBean dateTimeBean = new DateTimeBean();
            dateTimeBean.setZonedDateTime(when);

            ObserverLocationBean observerLocationBean = new ObserverLocationBean();
            observerLocationBean.setCoordinates(GeographicCoordinates.ofDeg(6.57, 46.52));	    


            ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
            viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(180, 42));
            viewingParametersBean.setFieldOfView(70);

            TimeAnimator animator = new TimeAnimator(dateTimeBean);
            
            SkyCanvasManager skyCanvasManager = new SkyCanvasManager(catalogue, dateTimeBean, observerLocationBean, viewingParametersBean);     

            Canvas sky = skyCanvasManager.canvas();
            Pane root = new Pane(sky);

            sky.widthProperty().bind(root.widthProperty());
            sky.heightProperty().bind(root.heightProperty());
            
            
            BorderPane scene = new BorderPane();
            scene.setCenter(root);
            scene.setTop(controlBar(viewingParametersBean, dateTimeBean, observerLocationBean, animator));
            scene.setBottom(informationBar(viewingParametersBean, skyCanvasManager));     
            
            
            primaryStage.setTitle("Rigel");
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.setScene(new Scene(scene));
            primaryStage.show();
            sky.requestFocus();
            }
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
	
    private InputStream resourceStream(String resourceName) {
        return getClass().getResourceAsStream(resourceName);
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


    private static BorderPane informationBar(ViewingParametersBean viewingParametersBean, SkyCanvasManager skyCanvasManager) {
       // System.out.println(viewingParametersBean.getFieldOfView());
        Text fovT = new Text();

        fovT.textProperty().bind(Bindings.format("Champ de vue: %.1f°" , viewingParametersBean.fieldOfViewProperty()));
        // create string
        
        
        
        Text closestObjectT = new Text();

        closestObjectT.textProperty().bind(Bindings.createStringBinding(() -> {
            if(skyCanvasManager.objectUnderMouseProperty().get() == null) {
                return "";
            } else {
                return skyCanvasManager.objectUnderMouseProperty().get().info();
            }
        }, skyCanvasManager.objectUnderMouseProperty()));

       
        Text mousePosT = new Text();

        mousePosT.textProperty().bind(Bindings.format("Azimut: %.1f°, hauteur: %.1f°",  
                skyCanvasManager.mouseAzDegProperty(), skyCanvasManager.mouseAltDegProperty()));

        BorderPane infoBar = new BorderPane(closestObjectT, null, mousePosT, null, fovT);
        infoBar.setStyle("-fx-padding: 4;\r\n; -fx-background-color: white;");

        return infoBar;

    }
}
