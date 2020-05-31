package ch.epfl.rigel.gui;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try (InputStream hs = resourceStream("/hygdata_v3.csv");
                InputStream ast = resourceStream("/asterisms.txt")) {
            // Read the star catalogue and asterism catalogue
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hs, HygDatabaseLoader.INSTANCE)
                    .loadFrom(ast, AsterismLoader.INSTANCE).build();

            DateTimeBean dateTimeBean = new DateTimeBean();
            dateTimeBean.setZonedDateTime(ZonedDateTime.now());

            // initialises the observer's location
            ObserverLocationBean observerLocationBean = new ObserverLocationBean();
            observerLocationBean
                    .setCoordinates(GeographicCoordinates.ofDeg(6.57, 46.52));

            // initialises the viewing center and fov
            ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
            viewingParametersBean
                    .setCenter(HorizontalCoordinates.ofDeg(180, 42));
            viewingParametersBean.setFieldOfView(70);

            // create animator and sky canvas manager
            TimeAnimator animator = new TimeAnimator(dateTimeBean);
            SkyCanvasManager skyCanvasManager = new SkyCanvasManager(catalogue,
                    dateTimeBean, observerLocationBean, viewingParametersBean);

            // sets up the canvas which will be drawn on
            Canvas sky = skyCanvasManager.canvas();
            Pane root = new Pane(sky);
            sky.widthProperty().bind(root.widthProperty());
            sky.heightProperty().bind(root.heightProperty());

            // initialises the scene
            BorderPane scene = new BorderPane();
            scene.setCenter(root);
            scene.setTop(controlBar(viewingParametersBean, dateTimeBean,
                    observerLocationBean, animator));
            scene.setBottom(
                    informationBar(viewingParametersBean, skyCanvasManager));

            primaryStage.setTitle("Rigel");
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.setScene(new Scene(scene));
            primaryStage.show();
            sky.requestFocus();
        }
    }

    /**
     * Creates the bar of controls
     * @param viewParam: Parameters for the viewing location.
     * @param dateTimeB: Date and time property.
     * @param obsLocB: Location of the observer.
     * @param animator: Movement animator.
     * @return: the control bar.
     * @throws IOException
     */
    private VBox controlBar(ViewingParametersBean viewParam,
            DateTimeBean dateTimeB, ObserverLocationBean obsLocB,
            TimeAnimator animator) throws IOException {

        HBox controlBarTop =  controlBarTop(dateTimeB, obsLocB, animator);
        HBox controlBarBottom = controlBarBottom(obsLocB, viewParam, animator);

        return new VBox(controlBarTop, controlBarBottom);
    }
    
    /**
     * Creates the top part of the control bar.
     * @param dateTimeB:  Date and time property.
     * @param obsLocB: Location of the observer.
     * @param animator:  Movement animator.
     * @return: the top part of the control bar.
     * @throws IOException
     */
    private HBox controlBarTop(DateTimeBean dateTimeB, ObserverLocationBean obsLocB, TimeAnimator animator) throws IOException {
        // Child containing date, time and zone controls
        HBox obsTime = initialiseDateTimeZone(dateTimeB, animator);
        obsTime.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        // Child containing the location of the observation.
        HBox obsPos = initialiseLonLat(obsLocB);
        obsPos.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
        
        // Child containing all the buttons to control the time accelerator
        HBox timeAcceleratorControl = initialiseButton(dateTimeB, animator);
        timeAcceleratorControl.setStyle("-fx-spacing: inherit;");


        // Creates the control bar with the correct styling
        HBox controlBar = new HBox(obsPos, obsTime, timeAcceleratorControl);
        controlBar.setStyle("-fx-spacing: 4; -FX-PADDING: 4;");
        
        return controlBar;
    }

    /**
     * Initialises the latitude and longitude controls.
     * @param obsLocB: Observator's location.
     * @return: the controls for latitude and longitude.
     */
    private HBox initialiseLonLat(ObserverLocationBean obsLocB) {
        // Lat and lon labels.
        Label lonL = new Label("Longitude(°):");
        Label latL = new Label("Latitude(°):");

        // Longitude text field
        TextField lonTF = new TextField();
        TextFormatter<Number> lonFormatter = textFormatter("lon");

        // Latitude text field
        TextField latTF = new TextField();
        TextFormatter<Number> latFormatter = textFormatter("lat");

        // Setting style and formatter
        latTF.setTextFormatter(latFormatter);
        latTF.setStyle("-fx-pref-width: 60; -fx-alignment: baseline_right;");
        lonTF.setTextFormatter(lonFormatter);
        lonTF.setStyle("-fx-pref-width: 60; -fx-alignment: baseline_right;");

        // Binding the obsever's location to the lon and lat text fields.
        latFormatter.valueProperty()
                .bindBidirectional(obsLocB.latDegProperty());
        lonFormatter.valueProperty()
                .bindBidirectional(obsLocB.lonDegProperty());

        return new HBox(lonL, lonTF, latL, latTF);
    }

    /**
     * Initialises the controls for the date time and zone.
     * @param dateTimeB: the date time and zone property.
     * @return: controls for the date time and zone.
     */
    private HBox initialiseDateTimeZone(DateTimeBean dateTimeB, TimeAnimator animator) {
        // Time label and text field.
        Label timeL = new Label("Heure:");
        TextField timeTF = new TextField();
        timeTF.disableProperty().bind(animator.runningProperty());
        // Time formatting
        timeTF.setStyle("-fx-pref-width: 75; -fx-alignment: baseline-right;");
        DateTimeFormatter hmsFormatter = DateTimeFormatter
                .ofPattern("HH:mm:ss");
        LocalTimeStringConverter stringConverter = new LocalTimeStringConverter(
                hmsFormatter, hmsFormatter);
        TextFormatter<LocalTime> timeFormatter = new TextFormatter<>(
                stringConverter);
        timeTF.setTextFormatter(timeFormatter);

        // Binding the timeFormatter to the dateTimeBean
        timeFormatter.valueProperty()
                .bindBidirectional(dateTimeB.timeProperty());

        // Date label and picker.
        Label dateL = new Label("Date:");
        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setStyle("-fx-pref-width: 120;");
        datePicker.disableProperty().bind(animator.runningProperty());
        // Binding the date picker to the date time bean
        datePicker.valueProperty().bindBidirectional(dateTimeB.dateProperty());

        // Creating the list of zoneId
        List<ZoneId> zoneIdList = new ArrayList<ZoneId>();
        for (String s : ZoneId.getAvailableZoneIds()) {
            zoneIdList.add(ZoneId.of(s));
        }

        // Zone id selector.
        ComboBox<ZoneId> zoneIdBox = new ComboBox<>(
                FXCollections.observableArrayList(zoneIdList));

        // Zone id styling
        zoneIdBox.setStyle("-fx-pref-width: 180;");
        zoneIdBox.setValue(ZoneId.systemDefault());
        zoneIdBox.disableProperty().bind(animator.runningProperty());
        // Binding of zone property to zoneid box
        dateTimeB.zoneProperty().bindBidirectional(zoneIdBox.valueProperty());

        return new HBox(dateL, datePicker, timeL, timeTF, zoneIdBox);
    }

    /**
     * Initialises the button for the simulation.
     * @param dateTimeB: date time property.
     * @param animator: Movement animator.
     * @param obsTime: Property of the time of observation.
     * @param obsPos: Property of the observator's location.
     * @return: the controls for the time animation in a HBox.
     * @throws IOException
     */
    private HBox initialiseButton(DateTimeBean dateTimeB, TimeAnimator animator)
            throws IOException {
        // Accelerator choices menu.
        ChoiceBox<NamedTimeAccelerator> acceleratorChoice = new ChoiceBox<NamedTimeAccelerator>(
                FXCollections
                        .observableArrayList(NamedTimeAccelerator.values()));
        acceleratorChoice.setValue(NamedTimeAccelerator.TIMES_300);
        acceleratorChoice.disableProperty().bind(animator.runningProperty());
        // Loading font for the button images.
        InputStream fontStream = getClass()
                .getResourceAsStream("/Font Awesome 5 Free-Solid-900.otf");
        Font defaultFont = Font.loadFont(fontStream, 15);
        fontStream.close();

        // Reset Button
        Button resetButton = new Button(ButtonImages.RESTART.getUniCode());
        resetButton.setFont(defaultFont);
        resetButton.setOnAction(e -> {
            dateTimeB.setZonedDateTime(ZonedDateTime.now());
        });
        resetButton.disableProperty().bind(animator.runningProperty());

        // Paused Button
        Button playPauseButt = new Button(ButtonImages.PLAY.getUniCode());
        playPauseButt.setFont(defaultFont);

        // Play/Pause button actions when pressed.
        playPauseButt.setOnAction(e -> {
            // Starts and stop the animation depending on whether it's already
            // running or not.
            if (animator.isRunning()) {
                animator.stop();
                playPauseButt.setText(ButtonImages.PLAY.getUniCode());
            } else {
                animator.start();
                playPauseButt.setText(ButtonImages.PAUSE.getUniCode());
            }
        });

        // Binding the time accelerator to the selected accelerator.
        animator.acceleratorProperty().bind(Bindings
                .select(acceleratorChoice.valueProperty(), "accelerator"));

        return new HBox(acceleratorChoice, resetButton, playPauseButt);
    }
    
    /**
     * Creates the bottom part of the control bar.
     * @param obsLocB: property of the observer's location.
     * @return: the bottom part of the control bar.
     */
    private HBox controlBarBottom(ObserverLocationBean obsLocB, ViewingParametersBean viewParam, TimeAnimator animator) {
 
    	HBox namedLocations = namedLocationsButton(obsLocB, animator);
    	namedLocations.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

    	HBox toggleLonLatLine  = toggleLonLatButton(viewParam);
    	toggleLonLatLine.setStyle("-fx-spacing: inherit; -fx-alignment: center-left;");
    	
    	HBox controlBarBottom = new HBox(namedLocations, toggleLonLatLine);
    	controlBarBottom.setStyle("-fx-spacing: 4; -FX-PADDING: 4;");
    	return controlBarBottom;
    }
    
    /**
     * Controls for the named location selection.
     * @param obsLocB: Property of the observer's location.
     * @return: name location selector.
     */
    private HBox namedLocationsButton(ObserverLocationBean obsLocB, TimeAnimator animator) {
       	Label namedLocation_l = new Label("Position connues:");
    	ChoiceBox<NamedPositions> locationsBox = new ChoiceBox<NamedPositions>(FXCollections.observableArrayList(NamedPositions.values()));
    	locationsBox.setStyle("-fx-pref-width: 160;");
    	locationsBox.setValue(NamedPositions.epfl);
    	locationsBox.disableProperty().bind(animator.runningProperty());
    	locationsBox.valueProperty().addListener((o, oV, nV) ->{
    		obsLocB.setCoordinates(locationsBox.getValue().coords());
    	});
    	return new HBox(namedLocation_l, locationsBox);
    }
    
    private HBox toggleLonLatButton(ViewingParametersBean viewParam) {
    	Label toggle_l = new Label("Toggle lon/lat lines");
    	CheckBox toggleLonLatLine = new CheckBox();
    	viewParam.enLatLonLinesProperty().bind(toggleLonLatLine.selectedProperty());
    	return new HBox(toggle_l, toggleLonLatLine);
    }

    private InputStream resourceStream(String resourceName) {
        return getClass().getResourceAsStream(resourceName);
    }


    /**
     * Allows to create text formatter for the lattitude of longitude.
     * @param latOrlon:
     *            "lat" or "lon".
     * @return: the text formatter for the latitude of longitude.
     */
    static private TextFormatter<Number> textFormatter(String latOrlon) {
        NumberStringConverter strinConverter = new NumberStringConverter(
                "#0.00");
        UnaryOperator<TextFormatter.Change> filter = (change -> {
            try {
                String newText = change.getControlNewText();
                double newLonDeg = strinConverter.fromString(newText)
                        .doubleValue();
                if (latOrlon.equals("lon")) {
                    return GeographicCoordinates.isValidLonDeg(newLonDeg)
                            ? change
                            : null;
                } else if (latOrlon.equals("lat")) {
                    return GeographicCoordinates.isValidLatDeg(newLonDeg)
                            ? change
                            : null;
                } else
                    throw new IllegalArgumentException();
            } catch (Exception e) {
                return null;
            }
        });
        return new TextFormatter<Number>(strinConverter, 0, filter);
    }

    /**
     * Enum storing the unicode string for the different button images.
     * @author Theo Houle (312432)
     */
    private enum ButtonImages {
        RESTART("\uf0e2"), PLAY("\uf04b"), PAUSE("\uf04c");

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

    // Return the information bar containing the object closest to mouse, fov
    // and azimuth and height
    private static BorderPane informationBar(
            ViewingParametersBean viewingParametersBean,
            SkyCanvasManager skyCanvasManager) {
        // Create empty text
        Text fovT = new Text();
        Text closestObjectT = new Text();
        Text mousePosT = new Text();

        // Creates a binding between the fov text and fov parameter
        fovT.textProperty().bind(Bindings.format("Champ de vue: %.1f°",
                viewingParametersBean.fieldOfViewProperty()));

        // Creates binding between the closest object text and the object under
        // mouse parameter
        closestObjectT.textProperty().bind(Bindings.createStringBinding(() -> {
            if (skyCanvasManager.objectUnderMouseProperty().get() == null) {
                return "";
            } else {
                return skyCanvasManager.objectUnderMouseProperty().get().info();
            }
        }, skyCanvasManager.objectUnderMouseProperty()));

        // Creates binding between the mouse position text and coordinates of
        // the mouse properties
        mousePosT.textProperty()
                .bind(Bindings.format("Azimut: %.1f°, Hauteur: %.1f°",
                        skyCanvasManager.mouseAzDegProperty(),
                        skyCanvasManager.mouseAltDegProperty()));

        // Creates the info Bar with the correct styles
        BorderPane infoBar = new BorderPane(closestObjectT, null, mousePosT,
                null, fovT);
        infoBar.setStyle("-fx-padding: 4;\r\n; -fx-background-color: white;");

        return infoBar;
    }
}
