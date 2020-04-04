package ch.epfl.rigel.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class BlackBodyColorTest {

	@Test
	void workWithLegalValues() throws IOException {
		assertEquals("0xc8d9ffff".toString(), BlackBodyColor.colorForTemperatur(10493).toString());
		assertEquals("0xc8d9ffff".toString(), BlackBodyColor.colorForTemperatur(10500).toString());
		assertEquals("0xb2cbffff".toString(), BlackBodyColor.colorForTemperatur(15200).toString());
		assertEquals("0x9bbcffff".toString(), BlackBodyColor.colorForTemperatur(39700).toString());
		assertEquals("0xff5d00ff".toString(), BlackBodyColor.colorForTemperatur(1300).toString());
		assertEquals("0xaec8ffff".toString(), BlackBodyColor.colorForTemperatur(16945).toString());
	}
	
	@Test
	void throwsWithIllegalValues() {
		assertThrows(IllegalArgumentException.class, ()->{
			BlackBodyColor.colorForTemperatur(900);
		});
		
		assertThrows(IllegalArgumentException.class, ()->{
			BlackBodyColor.colorForTemperatur(41000);
		});
	}

}
