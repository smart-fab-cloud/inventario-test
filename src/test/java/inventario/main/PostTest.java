package inventario.main;

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
import org.junit.Test;
import static org.junit.Assert.*;

public class PostTest {
    
    private WebTarget inventario;
    
    public PostTest() {
        // Creazione del client e connessione al servizio
        Client cli = ClientBuilder.newClient();
        inventario = cli.target("http://localhost:50000/inventario");
    }
    
    @Test
    public void testPostSoloCodiceCreated() throws ParseException {
        String codice = "qwerty123456";
        
        // Post di "codice" (senza ulteriori parametri)
        Response rPost = inventario.queryParam("codice", codice)
                            .request()
                            .post(Entity.entity("", MediaType.TEXT_PLAIN));
        
        // Verifica che la risposta "rPost" sia 201 Created
        assertEquals(Status.CREATED.getStatusCode(), rPost.getStatus());
        
        // Reperimento della risorsa creata
        Response rGet = inventario.path(codice)
                            .request()
                            .get();
        
        // Eliminazione della risorsa creata
        inventario.path(codice).request().delete();
        
        // Verifica che il prodotto sia stato inserito correttamente
        // (descrizione di default: "n.d.")
        // (quantità di default: 0)
        JSONParser p = new JSONParser();
        JSONObject prodotto = (JSONObject) p.parse(rGet.readEntity(String.class));
        String codiceProdotto = (String) prodotto.get("codice"); 
        assertEquals(codice, codiceProdotto);
        String descrizioneProdotto = (String) prodotto.get("descrizione");
        assertEquals("n.d.", descrizioneProdotto);
        Long quantProdotto = (Long) prodotto.get("quant");
        assertEquals(0, quantProdotto.intValue());
    }
    
    @Test
    public void testPostConParametriCreated() throws ParseException {
        String codice = "qwerty123456";
        String descrizione = "karma charmeleon";
        int quant = 89;
        
        // Post di "codice", "descrizione" e "quant"
        Response rPost = inventario.queryParam("codice", codice)
                            .queryParam("descrizione", descrizione)
                            .queryParam("quant", quant)
                            .request()
                            .post(Entity.entity("", MediaType.TEXT_PLAIN));
        
        // Reperimento della risorsa creata
        Response rGet = inventario.path(codice)
                            .request()
                            .get();
        
        // Eliminazione della risorsa creata
        inventario.path(codice).request().delete();
        
        // Verifica che la risposta "rPost" sia 201 Created
        assertEquals(Status.CREATED.getStatusCode(), rPost.getStatus());
        // Verifica che il prodotto sia stato inserito correttamente
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
    public void testPostBadRequest() {
        // Tentativo di post senza specifica di codice
        Response rPost1 = inventario.request()
                            .post(Entity.entity("", MediaType.TEXT_PLAIN));
        // Tentativo di post con codice vuoto
        Response rPost2 = inventario.queryParam("codice", "")
                            .request()
                            .post(Entity.entity("", MediaType.TEXT_PLAIN));
        
        // Verifica che in entrambi i casi la risposta sia 400 Bad Request
        assertEquals(Status.BAD_REQUEST.getStatusCode(),rPost1.getStatus());
        assertEquals(Status.BAD_REQUEST.getStatusCode(),rPost2.getStatus());
    }
    
    @Test
    public void testPostConflict() {
        String codice = "qwerty123456";
        
        // Creazione di una risorsa con codice "codice"
        Response rPost = inventario.queryParam("codice", codice)
                            .request()
                            .post(Entity.entity("", MediaType.TEXT_PLAIN));
              
        // Tentativo di creazione di un'altra risorsa con stesso "codice"
        // (senza ulteriori parametri)
        Response rPost1 = inventario.queryParam("codice", codice)
                            .request()
                            .post(Entity.entity("", MediaType.TEXT_PLAIN));
        
        // Tentativo di creazione di un'altra risorsa con stesso "codice"
        // (con specifica dei parametri "descrizione" e "quant"
        Response rPost2 = inventario.queryParam("codice", codice)
                            .queryParam("descrizione", "asd asd")
                            .queryParam("quant", 5)
                            .request()
                            .post(Entity.entity("", MediaType.TEXT_PLAIN));
        
        // Eliminazione del prodotto con codice "codice"
        inventario.path(codice).request().delete();
        
        // Verifica che entrambi i tentativi siano 409 Conflict
        assertEquals(Status.CONFLICT.getStatusCode(), rPost1.getStatus());
        assertEquals(Status.CONFLICT.getStatusCode(), rPost2.getStatus());
    }

}
