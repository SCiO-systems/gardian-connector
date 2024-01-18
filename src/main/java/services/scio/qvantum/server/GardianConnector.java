package services.scio.qvantum.server;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;
import org.apache.camel.spi.PropertiesComponent;
import org.apache.camel.support.DefaultRegistry;
import org.apache.camel.processor.idempotent.kafka.KafkaIdempotentRepository;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import services.scio.qvantum.route.GardianRouter;

public class GardianConnector extends Main {
    // private static final Logger logger = LogManager.getLogger(GardianConnector.class);

    public static void main(String[] args){
        CamelContext camelContext = new DefaultCamelContext();
        GardianConnector gardianConnector = new GardianConnector();

        PropertiesComponent properties = camelContext.getPropertiesComponent();
        properties.setLocation("classpath:/env.properties");

        // Elasticsearch Properties
        String esURL = properties.loadPropertiesAsMap().get("ES_URL").toString();
        int esPORT = Integer.parseInt(properties.loadPropertiesAsMap().get("ES_PORT").toString());

        // Create the low-level client
        RestClient restClient = RestClient.builder(
                new HttpHost(esURL, esPORT)).build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        ElasticsearchClient client = new ElasticsearchClient(transport);

        KafkaIdempotentRepository kafkaIdempotentRepository =
                new KafkaIdempotentRepository("idempotent-db-inserts", "localhost:9091");

        // Registry Bindings
        DefaultRegistry registry = (DefaultRegistry) camelContext.getRegistry();
        registry.bind("es_client", client);
        registry.bind("insertDbIdemRepo", kafkaIdempotentRepository);

        ((org.apache.camel.impl.DefaultCamelContext) camelContext).setRegistry(registry);

        try {
            camelContext.addRoutes(new GardianRouter());
            camelContext.start();
            ProducerTemplate template = camelContext.createProducerTemplate();
            template.sendBody("direct:fetch-gardian-documents","");
            gardianConnector.run();
        } catch (Exception e) {
            e.printStackTrace();
            // logger.error("QVANTUM API FAILED: {}",e.getMessage());
        }

    }

}
