package ch.epfl.rigel.gui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javafx.beans.property.ObjectProperty;

public final class DateTimeBean {

	private final ObjectProperty<LocalDate> date;
	private final ObjectProperty<LocalTime> time;
	private final ObjectProperty<ZoneId> zone;
	
	public DateTimeBean(ObjectProperty<LocalDate> date, ObjectProperty<LocalTime> time,
			ObjectProperty<ZoneId> zone) {
		this.date = date;
		this.time = time;
		this.zone = zone;
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
	 * @return
	 */
	public LocalDate getDate() {
		return date.getValue();
	}
	/**
	 * Setter for the date property's date.
	 * @param newDate
	 */
	public void setDate(LocalDate newDate) {
		date.set(newDate);
	}
	
	/**
	 * Getter for the time porperty.
	 * @return
	 */
	public ObjectProperty<LocalTime> timeProperty(){
		return time;
	}
	
	/**
	 * Getter for the time property's time.
	 * @return
	 */
	public LocalTime getTime() {
		return time.getValue();
	}
	/**
	 * Setter for the time property's time.
	 * @param newTime
	 */
	public void setTime(LocalTime newTime) {
		time.set(newTime);
	}
	
	/**
	 * Getter for the zone property.
	 * @return
	 */
	public ObjectProperty<ZoneId> zoneProperty(){
		return zone;
	}
	/**
	 * Getter for the zone property's zone.
	 * @return
	 */
	public ZoneId getZone() {
		return zone.getValue();
	}
	/**
	 * Setter for the time property's time.
	 * @param newZone
	 */
	public void setZone(ZoneId newZone) {
		zone.setValue(newZone);
	}
	
	public ZonedDateTime getZonedDateTime() {
		return ZonedDateTime.of(LocalDateTime.of(date.getValue(), time.getValue()),
				zone.getValue());
	}
	
	public void setZonedDateTime(ZonedDateTime newZonedDateTime) {
		setDate(newZonedDateTime.toLocalDate());
		setTime(newZonedDateTime.toLocalTime());
		setZone(newZonedDateTime.getZone());
	}
}
