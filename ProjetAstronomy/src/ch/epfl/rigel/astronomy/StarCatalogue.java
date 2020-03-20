package ch.epfl.rigel.astronomy;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
		try (InputStream s = new FileInputStream("hygdata_v3.csv")) {
			int b = 0;
			//Pointer to know at which line we are.
			int line = 0;
			// A string which will be the content of each line of the file.
			String str = "";
			while ((b = s.read()) != -1) {
				//Check if the caracter corresponds to a change of line : if not, add the character to the string
			    // else, it is the end of the line and therefore check if the line is a star in the asterism and 
				// if yes, add it's line index in the list and then reset the line string("str").
				if (b != 10) {
					str += (char)b;
				} else {
					String[] strTab = str.split(",");
					for  (Star star :starsInAste) {
						if (strTab[1] == Integer.toString(star.hipparcosId())) {
							indices.set(starsInAste.indexOf(star), line);
						}
					}
					str = "";
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
