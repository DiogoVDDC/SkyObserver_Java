import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        Star s = new Star(123, "Diogo", EquatorialCoordinates.of(0, 0), 1f , -0.03f);
//        System.out.println(s.colorTemperature());
        
        
        try (BufferedReader s = new BufferedReader( new FileReader("resources/hygdata_v3.csv"))){
        	String b;
        	int line = 0;
        	while ((b = s.readLine()) != null && line < 1200) {
        		System.out.println(b);
        		line += 1;
        	}
        }
    }
}
       



