package ch.epfl.rigel.astronomy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class HygDataBaseLoaderTest {

    private static final String HYG_CATALOGUE_NAME =
            "/hygdata_v3.csv";
    
    private static final String ASTERISM_CATALOGUE_NAME =
            "/asterisms.txt";

    @Test
    public void hygDatabaseIsCorrectlyInstalled() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            assertNotNull(hygStream);
        }
    }

    @Test
    public void hygDatabaseContainsRigel() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .build();
            Star rigel = null;
            for (Star s : catalogue.stars()) {
                if (s.name().equalsIgnoreCase("rigel"))
                    rigel = s;
            }
            assertNotNull(rigel);
        }
    }
    
    @Test
    public void testhygdatabaseDefaultString() throws IOException{

        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .build();
            int i = 0;
            for(Star star : catalogue.stars()) {
                if (star.name().charAt(0) == '?') {
                    i = 1;
                    assertEquals(' ', star.name().charAt(1));
                }
            }
            assertEquals(1,i);
        }        
    }
    
    
    @Test
    public void variousTestsAndReadablePrintfOnCompletelyFinishedStarCatalogue() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            InputStream asterismStream = getClass()
                    .getResourceAsStream(ASTERISM_CATALOGUE_NAME);
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE).loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();
            Star rigel = null;
            for (Star s : catalogue.stars()) {
                if (s.name().equalsIgnoreCase("rigel"))
                    rigel = s;
            }
            assertNotNull(rigel);

            List<Star> allStar = new ArrayList<Star>();
            allStar.addAll(catalogue.stars());

            System.out.println("LIST OF STARS :");
            for(Star s : allStar){
                System.out.print(s.hipparcosId() + " ");
            } //should print out the same star IDS as in the fichier (check visually)
            System.out.println();
            System.out.println();

            System.out.println("ASTERISMS : ");
            int i;

            
            System.out.println(catalogue.asterisms().size());
            //vérifier visuellement en utilisant CTRL-F que les astérismes contenu dans ASTERISMS sont bien les memes
            //flemme de coder une méthode qui vérifie automatiquement
            for(Asterism asterism : catalogue.asterisms()){
                List<Integer> cAstInd = catalogue.asterismIndices(asterism);
                System.out.println(catalogue.asterismIndices(asterism));
                i = 0;
                for(Star star : asterism.stars()){
                    System.out.print("Hip : ");
                    System.out.print(star.hipparcosId());
                    System.out.print("  foundHipparcos : ");
                    System.out.print(allStar.get(cAstInd.get(i)).hipparcosId());

                    /*TEST : l'index stoqué dans asterismIndices renvoie le meme hipparcosId que
                    l'index stoqué dans l'astérisme voulu : */
                    assertEquals(allStar.get(cAstInd.get(i)).hipparcosId(), star.hipparcosId());
                    System.out.print(" ||| ");
                    i++;
                }
                System.out.println();
            }
        }
    }
}


