package inventario.main;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.junit.Test;
import static org.junit.Assert.*;

public class GetTest {
    
    private WebTarget inventario;
    
    public GetTest() { 
        // Creazione del client e connessione al servizio
        Client cli = ClientBuilder.newClient();
        inventario = cli.target("http://localhost:50000/inventario");
    }    
    
    @Test
    public void testGetNotAllowed() {
        // Richiesta di Get sulla risorsa principale
        Response rGet = inventario.request().get();
        
        // Verifica che la risposta sia "405 Not Allowed"
        assertEquals(405, rGet.getStatus());
    }
}
