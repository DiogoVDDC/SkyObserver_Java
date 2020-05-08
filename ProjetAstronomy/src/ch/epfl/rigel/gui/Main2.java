package ch.epfl.rigel.gui;


import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.function.UnaryOperator;
import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class Main2 extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    private InputStream resourceStream(String resourceName) {
        return getClass().getResourceAsStream(resourceName);
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

            SkyCanvasManager skyCanvasManager = new SkyCanvasManager(catalogue, dateTimeBean, observerLocationBean, viewingParametersBean);        
            
//
//            skyCanvasManager.objectUnderMouseProperty().addListener(
//                    (p, o, n) -> {if (n != null) System.out.println(n);});

            Canvas sky = skyCanvasManager.canvas();
            Pane root = new Pane(sky);

            sky.widthProperty().bind(root.widthProperty());
            sky.heightProperty().bind(root.heightProperty());
            
            
            BorderPane scene = new BorderPane();
            scene.setCenter(root);
            scene.setTop(controlBar());
            scene.setBottom(informationBar(viewingParametersBean, skyCanvasManager));     
            
            
            primaryStage.setTitle("Rigel");
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.setScene(new Scene(scene));
            primaryStage.show();
            sky.requestFocus();
            }
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

    private static BorderPane informationBar(ViewingParametersBean viewingParametersBean, SkyCanvasManager skyCanvasManager) {

        Text fovT = new Text();
        fovT.textProperty().bind(Bindings.format("Champ de vue: %.1f°" , viewingParametersBean.fieldOfViewProperty()));            
        
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
