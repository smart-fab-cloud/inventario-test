package inventario.prodotto;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class PostTest {
    
    private WebTarget inventario;
    private String codice;
    private String descrizione;
    private int quant;
    
    public PostTest() {
        // Creazione del client e connessione al servizio
        Client cli = ClientBuilder.newClient();
        inventario = cli.target("http://localhost:50000/inventario");
        // e inizializzazione dati test
        codice = "qwerty123456";
        descrizione = "prodotto per unit test";
        quant = 1010;
    }
    
    @Before
    public void aggiuntaProdotto() {
        inventario.queryParam("codice", codice)
                .queryParam("descrizione", descrizione)
                .queryParam("quant", quant)
                .request()
                .post(Entity.entity("", MediaType.TEXT_PLAIN));
    }
    
    @Test
    public void testPostNotAllowed() {
        Response rPost = inventario.path(codice)
                            .request()
                            .post(Entity.entity("", MediaType.TEXT_PLAIN));
        
        assertEquals(405, rPost.getStatus());
    }
    
    @After
    public void eliminazioneProdotto() {
        inventario.path(codice).request().delete();
    }
}
