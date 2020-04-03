package ch.epfl.rigel.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import javafx.scene.paint.Color;


public class BlackBodyColor {
	
	/*
	 * Private constructor to prevent instantiation.
	 */
	private BlackBodyColor() {}
	
	/**
	 * Method allowing to know for a given degree the corresponding color according to 
	 * the file from Mitchell Charity.
	 * @param degreesK: the degree in Kelvin.
	 * @return: the corresponding colour as an instance of Color from the JavaFX library.
	 * @throws IOException: If an error occur while reading the file.
	 * @throws IllegalArgumentException: if the given degree isn't in the file.
	 */
	public static Color colorForTemperatur(int degreesK) throws IOException{
		degreesK = (int)Math.round(degreesK/100.0) * 100;
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(BlackBodyColor.class.getResourceAsStream("/bbr_color.txt"), StandardCharsets.US_ASCII))){
			//String which will contain the content of each line.
			String line;
			//Will keep reading line until there's no more(null).
			while((line = reader.readLine()) != null) {
				if(line.charAt(0) == ' ') {
					//We are only interested in the line starting with a space and with the text "10deg".
					if (line.substring(10, 15).equals("10deg")) {
						//If the degree in the current line is the same as the given one, return it's color.
						if (Integer.parseInt(line.substring(1, 6).replaceAll(" ","")) == degreesK){
							
							return Color.web(line.substring(80, 87));
						}
					}
				}
			}
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
		throw new IllegalArgumentException();
	}

}
