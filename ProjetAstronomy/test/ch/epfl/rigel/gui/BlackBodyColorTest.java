package ch.epfl.rigel.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class BlackBodyColorTest {

	@Test
	void workWithLegalValues() throws IOException {
		assertEquals("0xc8d9ffff".toString(), BlackBodyColor.colorForTemperature(10493).toString());
		assertEquals("0xc8d9ffff".toString(), BlackBodyColor.colorForTemperature(10500).toString());
		assertEquals("0xb2cbffff".toString(), BlackBodyColor.colorForTemperature(15200).toString());
		assertEquals("0x9bbcffff".toString(), BlackBodyColor.colorForTemperature(39700).toString());
		assertEquals("0xff5d00ff".toString(), BlackBodyColor.colorForTemperature(1300).toString());
		assertEquals("0xaec8ffff".toString(), BlackBodyColor.colorForTemperature(16945).toString());
	}
	
	@Test
	void throwsWithIllegalValues() {
		assertThrows(IllegalArgumentException.class, ()->{
			BlackBodyColor.colorForTemperature(900);
		});
		
		assertThrows(IllegalArgumentException.class, ()->{
			BlackBodyColor.colorForTemperature(41000);
		});
	}

}
