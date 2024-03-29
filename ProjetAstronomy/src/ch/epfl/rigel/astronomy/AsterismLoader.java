package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.rigel.astronomy.StarCatalogue.Builder;
import ch.epfl.rigel.astronomy.StarCatalogue.Loader;

/**
 * Enum used to load an asterism from an asterism catalogue.
 * @author Theo Houle (312432)
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 *
 */
public enum AsterismLoader implements Loader{
    INSTANCE;

    @Override
    public void load(InputStream inputStream, Builder builder)
            throws IOException {

        // initialise a map that associates id to the star
        Map<Integer, Star> starIdMap = new HashMap<Integer, Star>();

        // add the stars and associated id's to the map
        for(int i = 0 ; i < builder.stars().size(); i++) {
            starIdMap.put(builder.stars().get(i).hipparcosId(), builder.stars().get(i));
        }

        //String which will contain the lines of the document.
        String b;
        
        try(BufferedReader s = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII))){

            // read the inputStream line by line
            while ((b = s.readLine()) != null) {           
                //Initialize a list which will contain the stars in the asterism.
                List<Star> starList = new ArrayList<>();

                // get the hipparcosId in a string array
                String[] splitLine = b.split(",");

                //add the star associated with the id
                for(int i = 0; i < splitLine.length; i++) {
                    starList.add(starIdMap.get(Integer.parseInt(splitLine[i])));
                }

                // create new asterism from star list
                Asterism asterism = new Asterism(starList);
                builder.addAsterism(asterism);
            }     
        }
    }
}
