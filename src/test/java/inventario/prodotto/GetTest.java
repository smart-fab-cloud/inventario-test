package inventario.prodotto;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class GetTest {

    private WebTarget inventario;
    private String codice;
    private String descrizione;
    private int quant;
    
    public GetTest() {
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
    public void testReperimentoProdotto() throws ParseException {
        // Reperimento del prodotto
        Response rGet = inventario.path(codice).request().get();
        
        // Verifica che la risposta sia 200 Ok
        assertEquals(Status.OK.getStatusCode(),rGet.getStatus());
        
        // Verifica che i dati del prodotto siano corretti
        JSONParser p = new JSONParser();
        JSONObject prodotto = (JSONObject) p.parse(rGet.readEntity(String.class));
        String codiceProdotto = (String) prodotto.get("codice"); 
        assertEquals(codice, codiceProdotto);
        String descrizioneProdotto = (String) prodotto.get("descrizione");
        assertEquals(descrizione, descrizioneProdotto);
        Long quantProdotto = (Long) prodotto.get("quant");
        assertEquals(quant, quantProdotto.intValue());
    }
    
    @Test 
    public void testReperimentoProdottoInesistente() {
        // Reperimento di un prodotto inesistente
        Response rGet = inventario.path(codice + "$" + codice).request().get();
        
        // Verifica che la risposta sia 404 Not Found
        assertEquals(Status.NOT_FOUND.getStatusCode(),rGet.getStatus());
    }
    
    @After
    public void eliminazioneProdotto() {
        inventario.path(codice).request().delete();
    }       
}
