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

    private int hipparcosId;// MOVE IN LOAD?
    private float colorIndex;// MOVE IN LOAD?
    private float magnitude;// MOVE IN LOAD?
    private double rarad;// MOVE IN LOAD?
    private double decrad;// MOVE IN LOAD?
    private String bayer;// MOVE IN LOAD?
    private String con; // MOVE IN LOAD?
    private EquatorialCoordinates equatorialPos; // MOVE IN LOAD?
    private String name; // MOVE IN LOAD?
    
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
        int line = 0;
        try(BufferedReader s = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII))){       
	        // reads line by line of the input stream
	        while ((b = s.readLine()) != null) {       
	
	            // skips the first line that has the column names
	            if(line != 0) {	                
    	            //splits the lines in columns
    	            String[] splitLine = b.split(",");  
    	            hipparcosId = 0;
    	            magnitude = 0;
    	            colorIndex = 0;
    	            rarad = 0;
    	            decrad = 0;    	            
    	           
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

    	            equatorialPos = EquatorialCoordinates.of(rarad, decrad);
    	            
    	            if(!splitLine[columnEnum.BAYER.ordinal()].isBlank()) {
    	                bayer = splitLine[columnEnum.BAYER.ordinal()];
    	            } else {
    	                bayer = "? ";
    	            }

    	            con = splitLine[columnEnum.CON.ordinal()]; 	     
    	
    	            
    	            if(splitLine[columnEnum.PROPER.ordinal()].isBlank()) {
    	                name = bayer + con;        	      
    	            } else {
    	                name = splitLine[columnEnum.PROPER.ordinal()];
    	            }                
    	     
    	            // creates new star to be added to the starList
    	            Star star = new Star(hipparcosId, name, equatorialPos, magnitude, colorIndex);
    	            
    	            System.out.println("star in hig loader" + star.toString());
    	            // add the star to the builder
    	            builder.addStar(star);
	            } 
	            
	            line++;
	        }        
        }
    }
}
