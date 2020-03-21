package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;


import ch.epfl.rigel.astronomy.StarCatalogue.Builder;
import ch.epfl.rigel.astronomy.StarCatalogue.Loader;

public enum AsterismLoader implements Loader{
    INSTANCE;
    
    private List<Star> starList;    

    @Override
    public void load(InputStream inputStream, Builder builder)
            throws IOException {
        
        
        String b;
        BufferedReader s = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII));
       
        // read the inputStream line by line
        while ((b = s.readLine()) != null) {             
            
            // get the hipparcosId in a string array
            String[] splitLine = b.split(",");   
            
            // finds the star with the same hipparcosId as the one in the splitline string
            for(int i = 0; i < splitLine.length; i++) {
                for(int k = 0; k < builder.stars().size(); k++) {
                    if(builder.stars().get(k).hipparcosId() == Integer.parseInt(splitLine[i])) {
                        // add the star to the star list which will make the asterism
                        starList.add(builder.stars().get(k));
                    }
                }
            }

            Asterism asterism = new Asterism(starList);
            builder.addAsterisms(asterism);
        }     
    }

}
