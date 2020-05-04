package ch.epfl.rigel.gui;

import java.time.ZonedDateTime;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Class representing a time animator which periodically updates a given time 
 * based on a time accelerator.
 * @author Theo Houle (312432)
 *
 */
public final class TimeAnimator extends AnimationTimer{

	//The observable date.
	private final DateTimeBean dateTime;
	//Observable boolean to know wether the animation is running or not.
	private final SimpleBooleanProperty running;
	//Observable accelerator allowing to update the time.
	private final ObjectProperty<TimeAccelerator> accelerator;
	//Pointer of the time since the beggining of the clock gievn by the last call of handle method.
	private long elapsedTime;
	//Boolean allowing to know if the start method has just been called.
	private boolean justStarted;	
	
	/**
	 * TimeAnimator constructor.
	 * @param dateTime: initial time.
	 */
	public TimeAnimator(DateTimeBean dateTime) {
		this.dateTime = dateTime;
		accelerator = new SimpleObjectProperty<TimeAccelerator>(null);
		running = new SimpleBooleanProperty();
		elapsedTime = 0;
		justStarted = false;
	}
	
	@Override
	public void start() {
		super.start();
		justStarted = true;
		running.setValue(true);
	}
	
	@Override
	public void stop() {
		super.stop();
		running.setValue(false);
	}

	@Override
	public void handle(long now) {
		//If handle is being called for the first time, stores the initial time value.
		if (justStarted) {		    
			elapsedTime =  now;
			justStarted = false;
		} else {
			//Compute elapsed time since last call.
			long deltaTime = now - elapsedTime;
			//Adjut the date time according to the delta of time elapsed.
			ZonedDateTime newObservedTime = accelerator.getValue().adjust(dateTime.getZonedDateTime(), deltaTime);
			dateTime.setZonedDateTime(newObservedTime);
			//Update the elapsed time.
			elapsedTime = now;
		}
	}
	
	/**
	 * Getter for the running property.
	 * @return: the running property.
	 */
	public ReadOnlyBooleanProperty runningProperty() {
		return (ReadOnlyBooleanProperty) ReadOnlyBooleanProperty.booleanExpression(running);
	}	
	/**
	 * Getter for the running property's boolean value.
	 * @return: boolean value of the running property.
	 */
	public boolean isRunning() {
		return running.getValue();
	}
	
	/**
	 * Getter for the accelerator propety.
	 * @return: the accelertor property.
	 */
	public ObjectProperty<TimeAccelerator> acceleratorProperty(){
		return accelerator;
	}
	/**
	 * Getter for the accelerator property's time accelerator.
	 * @return
	 */
	public TimeAccelerator getAccelerator() {
		return accelerator.getValue();
	}
	
	/**
	 * Setter for the accelerator property's time accelerartor.
	 * @param newAccelerator: the new time accelerator.
	 */
	public void setAccelerator(TimeAccelerator newAccelerator) {
		accelerator.setValue(newAccelerator);
	}
	
}
