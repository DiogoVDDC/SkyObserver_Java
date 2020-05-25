package ch.epfl.rigel.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

import ch.epfl.rigel.Preconditions;
import javafx.scene.paint.Color;

public class BlackBodyColor {

    private final static Map<Integer, Color> tempToColorMap = initTempForColorMap();

    // Private constructor to prevent instantiation.
    private BlackBodyColor() {
    }

    /**
     * Constructor for the map which maps for every temperature the
     * corresponding colour.
     * @throws IOException:
     *             If an error occur while reading the file.
     * @throws IllegalArgumentException:
     *             if the given degree isn't in the file.
     */
    private static Map<Integer, Color> initTempForColorMap()
            throws UncheckedIOException {
        Map<Integer, Color> tempToColorMap = new TreeMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                BlackBodyColor.class.getResourceAsStream("/bbr_color.txt"),
                StandardCharsets.US_ASCII))) {
            // String which will contain the content of each line.
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.charAt(0) == ' ') {
                    // We are only interested in the line starting with a space
                    // and with the text "10deg".
                    if (line.substring(10, 15).equals("10deg")) {
                        // If the degree in the current line is the same as the
                        // given one, return it's color.
                        tempToColorMap.put(
                                Integer.parseInt(line.substring(1, 6).strip()),
                                Color.web(line.substring(80, 87)));
                    }
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return tempToColorMap;
    }

    /**
     * Method allowing to know for a given degree the corresponding color
     * according to the file from Mitchell Charity.
     * @param degreesK:
     *            the degree in Kelvin.
     * @return: the corresponding colour as an instance of Color from the JavaFX
     *          library.
     */
    public static Color colorForTemperature(int degreesK) throws IOException {
        degreesK = (int) Math.round(degreesK / 100.0) * 100;
        Preconditions.checkArgument(tempToColorMap.containsKey(degreesK));
        return tempToColorMap.get(degreesK);
    }

}
