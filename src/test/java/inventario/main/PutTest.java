package inventario.main;

import com.sun.org.apache.xerces.internal.util.Status;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.junit.Test;
import static org.junit.Assert.*;

public class PutTest {
        
    private WebTarget inventario;
    
    public PutTest() { 
        // Creazione del client e connessione al servizio
        Client cli = ClientBuilder.newClient();
        inventario = cli.target("http://localhost:50000/inventario");
    }    
    
    @Test
    public void testPutNotAllowed() {
        // Richiesta di Put sulla risorsa principale
        Response rPut = inventario.request()
                            .put(Entity.entity("", MediaType.TEXT_PLAIN));
        
        // Verifica che la risposta sia "405 Not Allowed"
        assertEquals(405, rPut.getStatus());
    }
    
}
