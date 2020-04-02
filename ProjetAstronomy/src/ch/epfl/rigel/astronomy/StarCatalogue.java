package ch.epfl.rigel.astronomy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.rigel.Preconditions;

/**
 * Class allowing to create a catalogue of stars.
 * @author Theo Houle (312432)
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 */
public final class StarCatalogue {

    //List of stars of the catalogue.
    private  List<Star> starList;
    //Map with key asterism and a list of indices (in the hyg star catalogue) of each star for the asterism. 
    private final Map<Asterism, List<Integer>> asterismMap;
    
    private final Map<Star, Integer> starIndexMap;

    /**
     * Constructor for the catalogue.
     * @param stars: list of stars in the catalogue.
     * @param asterisms: list of asterism.
     * @throws IOException: If error while reading the hyg stars catalogue file.
     */
    public StarCatalogue(List<Star> stars, List<Asterism> asterisms) throws IOException{
        this.starList = List.copyOf(stars);
        
        starIndexMap = new HashMap<Star, Integer>() ;
        
        for(int i = 0; i < starList.size(); i++) {
            starIndexMap.put(starList.get(i), i);
        }
        
   
        asterismMap = new HashMap<>();
        //Check if all stars in the list are in the asterisms.

        List<Integer> asterismStarIndexList = new ArrayList<Integer>();
        
        for (Asterism a: asterisms) {
            for (Star s : a.stars()) {
                Preconditions.checkArgument(stars().contains(s));
                asterismStarIndexList.add(starIndexMap.get(s));                      
            }            
            
            //Adds to the map the asterism and the corresponding list of indices.
            asterismMap.put(a, List.copyOf(asterismStarIndexList));
            asterismStarIndexList.clear();
        }
    }

    /**
     * Getter for the list of indices of the star in a given asterism.
     * @param asterism : the asterism.
     * @return : the list of indices.
     */
    public List<Integer> asterismIndices(Asterism asterism) {    
        // throw exception if asterism is not contained in Map
        Preconditions.checkArgument(asterismMap.containsKey(asterism));
       
        return asterismMap.get(asterism);
    }

    /**
     * Getter for the list of stars.
     * @return
     */
    public List<Star> stars() {
        return List.copyOf(starList);
    }

    /**
     * Getter for the asterisms.
     * @return
     */
    public Set<Asterism> asterisms() {
        return Set.copyOf(asterismMap.keySet());
    }

    /**
     * Represents a loader of star and asterism catalogues.
     * @author Theo Houle (312432)
     * @author Diogo Valdivieso Damasio Da Costa (311673)
     *
     */
   public interface Loader{        
        /**
         * Defines a generic load method to add the stars and asterisms to the builder
         * @param inputStream: the data that needs to be added to the builder
         * @param builder: the builder which we want to add the data to
         * @throws IOException: in case the inputstream is invalid
         */
        public abstract void load(InputStream inputStream, Builder builder) throws IOException;
    }

   /**
    * Represents a builder of star catalogue.
    * @author Theo Houle (312432)
    * @author Diogo Valdivieso Damasio Da Costa (311673)
    *
    */
    public final static class Builder {
        private List<Star> starList = new ArrayList<Star>();
        private List<Asterism> asterismList = new ArrayList<Asterism>();

        /**
         * @param star: the star to be added to the list
         * @return: returns the builder
         */
        public Builder addStar(Star star) {
            starList.add(star);           
            return this;
        }

        /**
         * @param asterism: the asterism to be added to the list
         * @return: returns the builder
         */
        public Builder addAsterism(Asterism asterism) {
            asterismList.add(asterism);            
            return this;
        }

        /**
         * @param inputStream: the input data we want to insert into the star catalogue
         * @param loader: the type of loader we want to use to insert the data
         * @return: returns the builder 
         * @throws IOException: in case the inputstream is invalid
         */
        public Builder loadFrom(InputStream inputStream, Loader loader) throws IOException{
            loader.load(inputStream, this);
            return this;
        }

        /**
         * @return: returns an unmodifiableList of stars
         */
        public List<Star> stars(){
            return Collections.unmodifiableList(starList);
        }

        /**
         * @return: returns an unmodifiableList of asterism
         */
        public List<Asterism> asterisms() {
            return Collections.unmodifiableList(asterismList);
        }

        /**
         * @return: returns a starcatalogue
         */
        public StarCatalogue build() throws IOException {
            return new StarCatalogue(starList, asterismList);
        }
    }
}
