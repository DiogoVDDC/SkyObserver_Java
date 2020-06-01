package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;

public enum NamedPositions {
	balcony("Mon Balcon", 46.53, 6.62),
	epfl("EPFL", 46.52, 6.57),
	eiffelTower("Tour Eiffel", 48.86, 2.29),
	MaunaKea("Mauna Kea Observatoire", 19.82, -155.47),
	cervin("Cervin", 45.98, 7.66);
	
	private final GeographicCoordinates coords;
	private final String name;
	
	private NamedPositions(String name, double lat, double lon) {
		this.coords = GeographicCoordinates.ofDeg(lon, lat);
		this.name = name;
	}
	
	public GeographicCoordinates coords() {
		return coords;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
