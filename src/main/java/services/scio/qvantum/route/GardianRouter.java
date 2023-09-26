package services.scio.qvantum.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.idempotent.kafka.KafkaIdempotentRepository;
import services.scio.qvantum.beans.GardianBean;
import services.scio.qvantum.models.elasticsearch.ElasticsearchCount;

public class GardianRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        from("direct:fetch-gardian-documents")
                .routeId("fetch_gardian_documents")
                .setProperty("size",simple("{{SIZE}}"))
                .setProperty("alias",simple("{{GARDIAN_INDEX}}"))
                .enrich().simple("http:{{ES_URL}}:{{ES_PORT}}/{{GARDIAN_INDEX}}/_count")
                .unmarshal().json(JsonLibrary.Jackson, ElasticsearchCount.class)
                .process(exchange -> {
                    ElasticsearchCount ec = exchange.getIn().getBody(ElasticsearchCount.class);
                    int size = exchange.getProperty("size",Integer.class);
                    int loops = (ec.getCount()/size)+1;
                    exchange.getIn().setBody(loops,Integer.class);
                })
                .setProperty("from",constant(0))
                .loop(simple("${in.body}"))
                    .bean(GardianBean.class,"getOpenDocuments")
                    .split(body())
                        .process(exchange -> {
                            int from = exchange.getProperty("from",Integer.class);
                            from = from +1;
                            exchange.setProperty("from",from);
                        })
                        .to("seda:dispatch");

        from("seda:dispatch")
                .routeId("dispatch")
                .idempotentConsumer(simple("${in.body.id}"))
                .idempotentRepository("insertDbIdemRepo")
                .skipDuplicate(true);
                //.to(kafka)

    }
}
