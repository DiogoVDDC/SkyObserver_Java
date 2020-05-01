package ch.epfl.rigel.gui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.print.attribute.DateTimeSyntax;

import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;

public final class SkyCanvasManager {

    
    //Observable sky.
    private final ObservableObjectValue<ObservedSky> observedSky;
    
    private final ObservableObjectValue<StereographicProjection> projection;
    
    
    public SkyCanvasManager(StarCatalogue starCatalogue, ViewingParametersBean viewingParametersBean,
            DateTimeBean dateTimeBean, ObserverLocationBean observerLocationBean) {
        
        projection = Bindings.createObjectBinding(() -> new StereographicProjection(viewingParametersBean.getHorizontalCoordinates()),
                viewingParametersBean.horizontalCoordinateProperty());
                
                
        
        observedSky = Bindings.createObjectBinding(() -> new ObservedSky(dateTimeBean.getZonedDateTime(),
             observerLocationBean.getObservableObjectCoordinates().getValue(), projection.getValue(), starCatalogue),
                viewingParametersBean.horizontalCoordinateProperty(), dateTimeBean.dateProperty(),
              dateTimeBean.timeProperty(), dateTimeBean.zoneProperty(), observerLocationBean.getObservableObjectCoordinates(),
              projection);              
          
    }
}









