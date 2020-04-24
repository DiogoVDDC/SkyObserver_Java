package ch.epfl.rigel.gui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Class representing an observable ZonedDateTime.
 * @author Theo Houle (312432)
 *
 */
public final class DateTimeBean {

	//The date property.
	private final ObjectProperty<LocalDate> date;
	//The time property.
	private final ObjectProperty<LocalTime> time;
	//The zone property.
	private final ObjectProperty<ZoneId> zone;
	
	/**
	 * DateTimeBean constructor.
	 * @param date: the initial local date.
	 * @param time: the initial local time
	 * @param zone: the initial zone.
	 */
	public DateTimeBean(LocalDate date, LocalTime time, ZoneId zone) {
		this.date = new SimpleObjectProperty<>(date);
		this.time = new SimpleObjectProperty<>(time);
		this.zone = new SimpleObjectProperty<>(zone);
	}
	
	/**
	 * Getter for the date proerty.
	 * @return: the date property.
	 */
	public ObjectProperty<LocalDate> dateProperty(){
		return date;
	}
	/**
	 * Getter for the date propertie's date.
	 * @return: the date property.
	 */
	public LocalDate getDate() {
		return date.getValue();
	}
	/**
	 * Setter for the date property's date.
	 * @param newDate: the new date.
	 */
	public void setDate(LocalDate newDate) {
		date.set(newDate);
	}
	
	/**
	 * Getter for the time porperty.
	 * @return: the time property.
	 */
	public ObjectProperty<LocalTime> timeProperty(){
		return time;
	}
	
	/**
	 * Getter for the time property's time.
	 * @return : the time of the time property.
	 */
	public LocalTime getTime() {
		return time.getValue();
	}
	/**
	 * Setter for the time property's time.
	 * @param newTime:t the new local time.
	 */
	public void setTime(LocalTime newTime) {
		time.set(newTime);
	}
	
	/**
	 * Getter for the zone property.
	 * @return: the zone property.
	 */
	public ObjectProperty<ZoneId> zoneProperty(){
		return zone;
	}
	/**
	 * Getter for the zone property's zone.
	 * @return: the zone of the zone property.
	 */
	public ZoneId getZone() {
		return zone.getValue();
	}
	/**
	 * Setter for the time property's time.
	 * @param newZone: the new zoneId.
	 */
	public void setZone(ZoneId newZone) {
		zone.setValue(newZone);
	}
	
	/**
	 * Getter for the triplet date, time and zone as a ZonedDateTime.
	 * @return: the ZonedDateTime created using the time, date
	 * and zone properties.
	 */
	public ZonedDateTime getZonedDateTime() {
		return ZonedDateTime.of(LocalDateTime.of(date.getValue(), time.getValue()),
				zone.getValue());
	}
	
	/**
	 * Setter for the whole triplet date, time and zone using ZonedDateTime.
	 * @param newZonedDateTime: the new ZonedDateTime.
	 */
	public void setZonedDateTime(ZonedDateTime newZonedDateTime) {
		date.setValue(newZonedDateTime.toLocalDate());
		time.setValue(newZonedDateTime.toLocalTime());
		zone.setValue(newZonedDateTime.getZone());
	}
}
