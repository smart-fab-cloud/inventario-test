package inventario.prodotto;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import jdk.net.SocketFlow;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class DeleteTest {
    private WebTarget inventario;
    private String codice;
    private String descrizione;
    private int quant;
    
    public DeleteTest() {
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
    public void testEliminazioneProdotto() {
        // Eliminazione del prodotto inserito
        Response rDelete = inventario.path(codice).request().delete();
        // Verifica che rDelete sia 200 OK
        assertEquals(Status.OK.getStatusCode(), rDelete.getStatus());
    }
    
    @Test
    public void testEliminazioneProdottoInesistente() {
        // Eliminazione del prodotto inserito
        Response rDelete = inventario.path(codice + "$" + codice).request().delete();
        // Verifica che rDelete sia 404 Not Found 
        assertEquals(Status.NOT_FOUND.getStatusCode(), rDelete.getStatus());
    }
    
}
