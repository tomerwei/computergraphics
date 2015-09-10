package cgresearch.apps.hlsvis;

import static org.junit.Assert.*;

import org.junit.Test;

import cgresearch.apps.hlsvis.hls.Connection;
import cgresearch.apps.hlsvis.hls.Connections;
import cgresearch.apps.hlsvis.hls.City.Location;

/**
 * JUnit-Testklasse für Transportbeziehungen
 * 
 * @author Philipp Jenke
 *
 */
public class TestTransportConnections {

	@Test
	public void testToJson() {
		Connections transportConnections = new Connections();
		transportConnections.addConnection(new Connection(
				Location.Berlin, Location.Hamburg, 196));
		transportConnections.addConnection(new Connection(
				Location.Hamburg, Location.Koeln, 256));
		String jSonMessage = transportConnections.toJson();
		String expected = "{\n\"Transportbeziehungen\":\n[\n{\"StartLokation\":\"Berlin\",\"ZielLokation\":\"Hamburg\",\"Dauer\":196},\n{\"StartLokation\":\"Hamburg\",\"ZielLokation\":\"Koeln\",\"Dauer\":256}\n]\n}";
		assertEquals(expected, jSonMessage);
	}
}
