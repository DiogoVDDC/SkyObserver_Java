package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.rigel.Preconditions;

/**
 * Class allowing to create a catalogue of stars.
 * @author Theo Houle (312432)
 *
 */
public final class StarCatalogue {
	
	//List of stars of the catalogue.
	private final List<Star> stars;
	//Map with key asterism and a list of indices (in the hyg star catalogue) of each star for the asterism. 
	private final Map<Asterism, List<Integer>> asterismsIndices;

	/**
	 * Constructor for the catalogue.
	 * @param stars: list of stars in the catalogue.
	 * @param asterisms: list of asterism.
	 * @throws IOException: If error while reading the hyg stars catalogue file.
	 */
	StarCatalogue(List<Star> stars, List<Asterism> asterisms) throws IOException{
		asterismsIndices = new HashMap<>();
		//Check if all stars in the list are in the asterisms.
		for (Star s : stars) {
			for (Asterism a: asterisms) {
				Preconditions.checkArgument(a.stars().contains(s));
				//Adds to the map the asterism and the corresponding list of indices.
				asterismsIndices.put(a, getIndices(a));
			}
		}
		
		this.stars = List.copyOf(stars);
	}
	
	/**
	 * Getter for the list of stars.
	 * @return
	 */
	public List<Star> stars() {
		return List.copyOf(stars);
	}
	
	/**
	 * Getter for the asterisms.
	 * @return
	 */
	public Set<Asterism> asterisms() {
		return asterismsIndices.keySet();
	}
	
	/**
	 * Allows to get for a given asterism the list of indices of the stars in the same order.
	 * @param asterism
	 * @return : list of indices of each star according to hyg star catalogue.
	 * @throws IOException :if there is an error while reading hyg catalogue.
	 */
	private List<Integer> getIndices(Asterism asterism) throws IOException{
		List<Star> starsInAste = asterism.stars();
		List<Integer> indices = new ArrayList<>(starsInAste.size());
		try (BufferedReader s = new BufferedReader( new InputStreamReader( new FileInputStream("resources/hygdata_v3.csv"), StandardCharsets.US_ASCII))) {
			String b;
			//Pointer to know at which line we are.
			int line = 0;
			while ((b = s.readLine()) != null) {
				String[] strTab = b.split(",");
				for  (Star star :starsInAste) {
					//Check if the line corresponds to a star in the asterism in which case it adds the line index in the list. 
					//The position 1 corresponds to the hipparcos ID.
					if (Integer.valueOf(strTab[1]) == star.hipparcosId()) {
						indices.set(starsInAste.indexOf(star), line);
					}
				}
				line += 1;
			}
		}
		return indices;
	}
	
	/**
	 * Getter for the list of indices of the star in a given asterism.
	 * @param asterism : the asterism.
	 * @return : the list of indices.
	 */
	List<Integer> asterismIndices(Asterism asterism) {
		return asterismsIndices.get(asterism);
	}
}
