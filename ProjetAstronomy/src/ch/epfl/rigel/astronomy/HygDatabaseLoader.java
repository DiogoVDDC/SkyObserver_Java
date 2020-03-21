package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import ch.epfl.rigel.astronomy.StarCatalogue.Builder;
import ch.epfl.rigel.astronomy.StarCatalogue.Loader;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

public enum HygDatabaseLoader implements Loader{
    INSTANCE;

    private int hipparcosId;
    private float colorIndex;
    private float magnitude;
    private double rarad;
    private double decrad;
    private String bayer;
    private String con;
    private EquatorialCoordinates equatorialPos;
    private String name;
    
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
        BufferedReader s = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII));
       
        // reads line by line of the input stream
        while ((b = s.readLine()) != null) {             

            //splits the lines in columns
            String[] splitLine = b.split(",");     
            
            // associates the variable needed to define a star to the correct column
            hipparcosId = Integer.parseInt(splitLine[columnEnum.ID.ordinal()]);
            magnitude = Float.parseFloat(splitLine[columnEnum.MAG.ordinal()]);
            colorIndex = Float.parseFloat(splitLine[columnEnum.CI.ordinal()]);
            rarad = Double.parseDouble(splitLine[columnEnum.RARAD.ordinal()]);
            decrad = Double.parseDouble(splitLine[columnEnum.DECRAD.ordinal()]);                
            equatorialPos = EquatorialCoordinates.of(rarad, decrad);
            bayer = splitLine[columnEnum.BAYER.ordinal()];
            con = splitLine[columnEnum.CON.ordinal()];

            if(splitLine[columnEnum.PROPER.ordinal()] != null) {
                name = bayer + con;                    
            } else {
                name = splitLine[columnEnum.PROPER.ordinal()];
            }                

            // creates new star to be added to the starList
            Star star = new Star(hipparcosId, name, equatorialPos, magnitude, colorIndex);
            builder.addStar(star);
        }        
    }
}