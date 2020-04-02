package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import ch.epfl.rigel.astronomy.StarCatalogue.Builder;
import ch.epfl.rigel.astronomy.StarCatalogue.Loader;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

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
        
        String b;
        try(BufferedReader s = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII))){       
	        // reads line by line of the input stream
	        while ((b = s.readLine()) != null) {             
	
	            //splits the lines in columns
	            String[] splitLine = b.split(",");     
	            
	            // associates the variable needed to define a star to the correct column
	            int  hipparcosId = Integer.parseInt(splitLine[columnEnum.ID.ordinal()]);
	            float magnitude = Float.parseFloat(splitLine[columnEnum.MAG.ordinal()]);
	            float colorIndex = Float.parseFloat(splitLine[columnEnum.CI.ordinal()]);
	            double rarad = Double.parseDouble(splitLine[columnEnum.RARAD.ordinal()]);
	            double decrad = Double.parseDouble(splitLine[columnEnum.DECRAD.ordinal()]);                
	            EquatorialCoordinates equatorialPos = EquatorialCoordinates.of(rarad, decrad);
	            String bayer = splitLine[columnEnum.BAYER.ordinal()];
	            String con = splitLine[columnEnum.CON.ordinal()];
	
	            String name;
	            if(splitLine[columnEnum.PROPER.ordinal()] == null) {
	                name = bayer + con;                    
	            } else {
	                name = splitLine[columnEnum.PROPER.ordinal()];
	            }                
	
	            // creates new star to be added to the starList
	            Star star = new Star(hipparcosId, name, equatorialPos, magnitude, colorIndex);
	            // add the star to the builder
	            builder.addStar(star);
	        }        
        }
    }
}
