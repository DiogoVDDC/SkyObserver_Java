package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import ch.epfl.rigel.astronomy.StarCatalogue.Builder;
import ch.epfl.rigel.astronomy.StarCatalogue.Loader;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * Enum used to load the stars for the hyg star catalogue.
 * @author Theo Houle (312432)
 * @author Diogo Valdivieso Damasio Da Costa (311673)
 */
public enum HygDatabaseLoader implements Loader{
    INSTANCE;
    
    // Enum to get the ordinal of the columns
    private enum columnEnum{
        ID, HIP, HD, HR, GL, BF, PROPER, RA, DEC, DIST, PMRA, PMDEC,
        RV, MAG, ABSMAG, SPECT, CI, X, Y, Z, VX, VY, VZ,
        RARAD, DECRAD, PMRARAD, PMDECRAD, BAYER, FLAM, CON,
        COMP, COMP_PRIMARY, BASE, LUM, VAR, VAR_MIN, VAR_MAX;
    } 

    @Override
    public void load(InputStream inputStream, Builder builder) throws IOException {
        
    	//String used to contain the content of each line.
        String b;
        //Pointer to keep track of the current line.
        int line = 0;
        
        try(BufferedReader s = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII))){       
	        // reads line by line of the input stream until no more to read(null).
	        while ((b = s.readLine()) != null) {             
	
	            // skips the first line that has the column names
	            if(line != 0) {	                
    	            //splits the lines in columns
    	            String[] splitLine = b.split(",");  
    	            
    	            //Hipparcos number of the star.
    	            int hipparcosId = 0;
    	            //Magnitude of the star
    	            float magnitude = 0;
    	            //Color index of the star.
    	            float colorIndex = 0;
    	            //Right ascension of the star in radiant.
    	            double rarad = 0;
    	            //Declination of the star in radiant.
    	            double decrad = 0;    	            
    	           
    	            //Definig the above elements based on the values of the catalogue.
    	            if(!splitLine[columnEnum.HIP.ordinal()].isBlank()) {
    	                hipparcosId = Integer.parseInt(splitLine[columnEnum.HIP.ordinal()]);
    	            }
    	          
    	            
    	            if(!splitLine[columnEnum.MAG.ordinal()].isBlank()) {
    	                magnitude = Float.parseFloat(splitLine[columnEnum.MAG.ordinal()]);
    	            }
    	    
    	            
    	            if(!splitLine[columnEnum.CI.ordinal()].isBlank()) {
    	                colorIndex = Float.parseFloat(splitLine[columnEnum.CI.ordinal()]);
    	            }
    	        
    	            
    	            if(!splitLine[columnEnum.RARAD.ordinal()].isBlank()) {
    	                rarad = Double.parseDouble(splitLine[columnEnum.RARAD.ordinal()]);
    	            } 	            
    	         
    	            
    	            if(!splitLine[columnEnum.DECRAD.ordinal()].isBlank()) {
    	                decrad = Double.parseDouble(splitLine[columnEnum.DECRAD.ordinal()]); 
    	            }

    	            //Building the equatorial coordinates of the star given rarad and decrad.
    	            EquatorialCoordinates equatorialPos = EquatorialCoordinates.of(rarad, decrad);
    	            
    	            //Define the name of the star,  if the column for the name in the
    	            //catalogue is empty, the name consists of the bayer name and the
    	            //shortened name of it's constellation. If the bayer name
    	            //is empty, then it's replaced with "? ".
    	            String name;
    	            if(!(splitLine[columnEnum.PROPER.ordinal()].isBlank())) {
    	                name = splitLine[columnEnum.PROPER.ordinal()];        	      
    	            } else {
    	            	String bayer;
    	            	if (!splitLine[columnEnum.BAYER.ordinal()].isBlank()) {
    	            		bayer = splitLine[columnEnum.BAYER.ordinal()];
    	            	} else {
    	            		bayer = "? ";
    	            	}
    	            	String con = splitLine[columnEnum.CON.ordinal()]; 	 
    	            	name = bayer + con;
    	            }     

    	            // creates new star to be added to the starList
    	            Star star = new Star(hipparcosId, name, equatorialPos, magnitude, colorIndex);    	            
    	          
    	            // add the star to the builder
    	            builder.addStar(star);
	            } 
	            line++;
	        }        
        }
    }
}
