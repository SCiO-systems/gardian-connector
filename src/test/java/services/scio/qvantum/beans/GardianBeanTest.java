package services.scio.qvantum.beans;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.lw.LightweightCamelContext;
import org.apache.camel.spi.PropertiesComponent;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.support.DefaultRegistry;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import services.scio.qvantum.models.resource.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GardianBeanTest {

    private CamelContext ctx;

    Exchange setUpTheTestEnv () {
        this.ctx = new DefaultCamelContext();

        PropertiesComponent properties = ctx.getPropertiesComponent();
        properties.setLocation("classpath:/env.properties");

        DefaultRegistry registry = (DefaultRegistry) ctx.getRegistry();
        ((org.apache.camel.impl.DefaultCamelContext) ctx).setRegistry(registry);

        String esURL = properties.loadPropertiesAsMap().get("ES_URL").toString();
        int esPORT = Integer.parseInt(properties.loadPropertiesAsMap().get("ES_PORT").toString());

        RestClient restClient = RestClient.builder(
                new HttpHost(esURL, esPORT)).build();

        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        ElasticsearchClient client = new ElasticsearchClient(transport);

        registry.bind("es_client", client);

        Exchange ex = new DefaultExchange(ctx);

        ex.setProperty("alias", "gardian_index");

        return ex;
    }

    @Test
    void getOpenDocsInitialScrollTestHappypathReturnType () {
        Exchange ex = setUpTheTestEnv();

        GardianBean gb = new GardianBean();
//        List<Resource> lr = gb.getOpenDocuments(ex);

        assertEquals(gb.getOpenDocsInitialScroll(ex).getClass(), ArrayList.class);
    }


    @Test
    void getOpenDocumentsHappypathReturnType () {
        Exchange ex = setUpTheTestEnv();
        GardianBean gb = new GardianBean();
        gb.getOpenDocsInitialScroll(ex);  //call get Initial first to set the scrollID property
        assertEquals(gb.getOpenDocuments(ex).getClass(), ArrayList.class);
    }

    @Test
    void getOpenDocsInitialScrollTestExceptionThrowing () {
        Exchange ex = setUpTheTestEnv();
        GardianBean gb = new GardianBean();
        ex.setProperty("alias", "blahblah");

        Exception exception = assertThrows(Exception.class, () -> {
            gb.getOpenDocsInitialScroll(ex);
        });

    }

    @Test
    void getOpenDocumentsTestExceptionThrowing () {
        Exchange ex = setUpTheTestEnv();
        GardianBean gb = new GardianBean();
        ex.setProperty("alias", "blahblah");

        Exception exception = assertThrows(Exception.class, () -> {
            gb.getOpenDocuments(ex);
        });

    }

    @Test
    void getDocsInitialScrollTestResultIsOfTypeResource () {
        Exchange ex = setUpTheTestEnv();
        GardianBean gb = new GardianBean();
        ArrayList<Resource> results = gb.getOpenDocsInitialScroll(ex);
        assertEquals(results.get(0).getClass(), Resource.class);
        assertEquals(results.get(0).getSource().getClass(), String.class);
        assertEquals(results.get(0).getId().getClass(), String.class);
        assertEquals(results.get(0).getUrl().getClass(), String.class);
        assertFalse(results.get(0).getSource().isEmpty());
        assertFalse(results.get(0).getId().isEmpty());
        assertFalse(results.get(0).getUrl().isEmpty());
    }

    @Test
    void getDocumentsTestResultIsOfTypeResource () {
        Exchange ex = setUpTheTestEnv();
        GardianBean gb = new GardianBean();
        gb.getOpenDocsInitialScroll(ex);
        ArrayList<Resource> results = gb.getOpenDocuments(ex);
        assertEquals(results.get(0).getClass(), Resource.class);
        assertEquals(results.get(0).getSource().getClass(), String.class);
        assertEquals(results.get(0).getId().getClass(), String.class);
        assertEquals(results.get(0).getUrl().getClass(), String.class);
        assertFalse(results.get(0).getSource().isEmpty());
        assertFalse(results.get(0).getId().isEmpty());
        assertFalse(results.get(0).getUrl().isEmpty());
    }

    @Test
    void getOpenDocumentsThrowsExceptionWithoutScrollIDSet() {
        Exchange ex = setUpTheTestEnv();
        GardianBean gb = new GardianBean();
        String scrollId = Instancio.create(String.class);
        ex.setProperty("scrollID", scrollId);

        Exception exception = assertThrows(Exception.class, () -> {
            gb.getOpenDocuments(ex);
        });
    }

}