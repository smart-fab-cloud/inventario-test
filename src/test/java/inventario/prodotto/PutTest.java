package inventario.prodotto;

import javax.net.ssl.SSLEngineResult;
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

public class PutTest {
    
    private WebTarget inventario;
    private String codice;
    private String descrizione;
    private int quant;
    
    public PutTest() {
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
    public void aggiornamentoQuant() throws ParseException {
        int quantNuova = 55;
        // Aggiornamento risorsa (solo quant)
        Response rPut = inventario.path(codice)
                            .queryParam("quant", quantNuova)
                            .request()
                            .put(Entity.entity("", MediaType.TEXT_PLAIN));
        
        // Reperimento risorsa aggiornata
        Response rGet = inventario.path(codice)
                            .request()
                            .get();
        
        // Verifica che rPut sia 200 OK
        assertEquals(Status.OK.getStatusCode(),rPut.getStatus());
        // Verifica che il prodotto sia stato aggiornato correttamente
        JSONParser p = new JSONParser();
        JSONObject prodotto = (JSONObject) p.parse(rGet.readEntity(String.class));
        Long quantProdotto = (Long) prodotto.get("quant");
        assertEquals(quantNuova, quantProdotto.intValue());
    }
    
    @Test
    public void aggiornamentoDescrizioneEQuant() throws ParseException {
        String descrizioneNuova = "nuova descrizione";
        int quantNuova = 55;
        // Aggiornamento risorsa
        Response rPut = inventario.path(codice)
                            .queryParam("descrizione", descrizioneNuova)
                            .queryParam("quant", quantNuova)
                            .request()
                            .put(Entity.entity("", MediaType.TEXT_PLAIN));
        
        // Reperimento risorsa aggiornata
        Response rGet = inventario.path(codice)
                            .request()
                            .get();
        
        // Verifica che rPut sia 200 OK
        assertEquals(Status.OK.getStatusCode(),rPut.getStatus());
        // Verifica che il prodotto sia stato aggiornato correttamente
        JSONParser p = new JSONParser();
        JSONObject prodotto = (JSONObject) p.parse(rGet.readEntity(String.class));
        String descrizioneProdotto = (String) prodotto.get("descrizione");
        assertEquals(descrizioneNuova, descrizioneProdotto);
        Long quantProdotto = (Long) prodotto.get("quant");
        assertEquals(quantNuova, quantProdotto.intValue());
    }
    
    @Test
    public void aggiornamentoDescrizione() {
        String descrizioneNuova = "nuova descrizione";
        // Tentativo di aggiornamento risorsa (solo descrizione)
        Response rPut = inventario.path(codice)
                            .queryParam("descrizione", descrizioneNuova)
                            .request()
                            .put(Entity.entity("", MediaType.TEXT_PLAIN));
        
        // Verifica che la risposta sia 400 Bad Request
        assertEquals(Status.BAD_REQUEST.getStatusCode(),rPut.getStatus());
    }
    
    @Test
    public void aggiornamentoProdottoInesistente() {
        // Tentativo di aggiornamento risorsa inesistente
        Response rPut = inventario.path(codice + "$" + codice)
                            .queryParam("quant", 55)
                            .request()
                            .put(Entity.entity("", MediaType.TEXT_PLAIN));

        // Verifica che la risposta sia 404 Not Found
        assertEquals(Status.NOT_FOUND.getStatusCode(),rPut.getStatus());   
    }
    
    @After
    public void eliminazioneProdotto() {
        inventario.path(codice).request().delete();
    }    
}
