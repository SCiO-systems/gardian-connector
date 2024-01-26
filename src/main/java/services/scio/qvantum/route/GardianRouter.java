package services.scio.qvantum.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.idempotent.kafka.KafkaIdempotentRepository;
import services.scio.qvantum.beans.GardianBean;
import services.scio.qvantum.models.elasticsearch.ElasticsearchCount;
import services.scio.qvantum.models.resource.Resource;
import services.scio.qvantum.process.ExceptionHandler;
import org.elasticsearch.search.SearchContextMissingException;

public class GardianRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        onException(SearchContextMissingException.class)
                .handled(false)
                .process(new ExceptionHandler())
                .marshal().json()
                .to("file://C:\\Users\\Anastasis\\Desktop\\gardianConnector\\errors\\");

        onException(Exception.class)
                .handled(true)
                .process(new ExceptionHandler())
                .marshal().json()
                .to("file://C:\\Users\\Anastasis\\Desktop\\gardianConnector\\errors\\");

        from("direct:fetch-gardian-documents")
                .routeId("fetch_gardian_documents")
                .setProperty("alias",simple("{{GARDIAN_INDEX}}"))

                .bean(GardianBean.class, "getOpenDocsInitialScroll")
                .split(body())
                .to("seda:dispatch")
                .end()
                .loopDoWhile(simple("${in.header:loop}"))
                .bean(GardianBean.class,"getOpenDocuments")
                .split(body())
                .to("seda:dispatch");

        from("seda:dispatch")
                .routeId("dispatch")
                .marshal().json(JsonLibrary.Jackson, Resource.class)
                .to("kafka:{{KAFKA_OUT}}?brokers={{KAFKA_BROKER}}");

    }
}

