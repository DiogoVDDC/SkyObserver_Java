package ch.epfl.rigel.astronomy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

import org.junit.Test;

public class AsterismLoaderTest {

    private static final String HYG_CATALOGUE_NAME =
            "/hygdata_v3.csv";

    private static final String ASTERISM_CATALOGUE_NAME =
            "/asterisms.txt";


    @Test
    public void asterismLoaderBeltegeuse() throws IOException {

        StarCatalogue catalogue;
        StarCatalogue.Builder catalogueBuilder = new StarCatalogue.Builder();

        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            catalogue = catalogueBuilder
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .build();
        }
        
        System.out.println(catalogue.stars().get(1).toString());

        try (InputStream asterismStream = getClass()
                .getResourceAsStream(ASTERISM_CATALOGUE_NAME)) {
            catalogue = catalogueBuilder
                    .loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();
        }

        System.out.println("asterism");
       
        System.out.println(catalogue.asterisms().isEmpty());

        for(Iterator<Asterism> aIterator = catalogue.asterisms().iterator(); aIterator.hasNext();) {
            Asterism a = aIterator.next();
            for(Star s: a.stars()) {
                System.out.print(s.hipparcosId());
            }
        }

        Queue<Asterism> a = new ArrayDeque<>();
        Star beltegeuse = null;
        for (Asterism ast : catalogue.asterisms()) {

            for (Star s : ast.stars()) {
                // two asterisms satify this
                if (s.name().equalsIgnoreCase("Rigel")) 
                { 
                    a.add(ast);
                    System.out.println("asterism found");
                }
            }
        }

        int astCount = 0;

        // loop through the two asterism found
        for (Asterism ast : a) {
            ++astCount;
            for (Star s : ast.stars()) {
                if (s.name().equalsIgnoreCase("Betelgeuse")) { 
                    beltegeuse = s; 
                }
            }
        }


        assertNotNull(beltegeuse);
        assertEquals(2,astCount);
    }
}

