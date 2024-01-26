package services.scio.qvantum.beans;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.ScrollRequest;
import co.elastic.clients.elasticsearch.core.ScrollResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch._types.Time;

import org.apache.camel.Exchange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.scio.qvantum.models.resource.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Iterator;


public class GardianBean {

    private static final Logger logger = LogManager.getLogger(GardianBean.class);
    private static final int SCROLL_SIZE = 1000;

    public ArrayList<Resource> getOpenDocsInitialScroll (Exchange exchange) {
        String alias  = exchange.getProperty("alias",String.class);

        ElasticsearchClient client = exchange.getContext().getRegistry()
                .lookupByNameAndType("es_client",ElasticsearchClient.class);

        ArrayList<Resource> resources = new ArrayList<>();

        try {
            SearchResponse<Map> response = client.search(s -> s
                            .index(alias)
                            .query(q -> q
                                    .match(
                                            m -> m
                                                    .field("rights.access")
                                                    .query("Open")
                                    )
                            )
                            .size(SCROLL_SIZE)
                            .scroll(Time.of(t->t.time("10m"))),
                    Map.class
            );

            String scrollId = response.scrollId();
            exchange.setProperty("scrollID", scrollId);
            exchange.getIn().setHeader("loop", true);
            List<Hit<Map>> hits = response.hits().hits();

            if (hits != null
                    && !hits.isEmpty()) {
                resources = getResourcesFromHits(resources, hits);
            }
            else {
                exchange.getIn().setHeader("loop", false);
            }

        } catch (IOException e) {
            return null;
        }

        return resources;
    }


    public ArrayList<Resource> getOpenDocuments(Exchange exchange) {

        ElasticsearchClient client = exchange.getContext().getRegistry()
                .lookupByNameAndType("es_client",ElasticsearchClient.class);

        String scrollID = exchange.getProperty("scrollID", String.class);

        ArrayList<Resource> resources = new ArrayList<>();

        try {
            String finalScrollId = scrollID;
//            System.out.println(finalScrollId);
            ScrollRequest sr = ScrollRequest.of(a -> a
                    .scrollId(finalScrollId).scroll(Time.of(t->t.time("10m"))));

            ScrollResponse<Map> scrollResponse = client.scroll(sr, Map.class);

            scrollID = scrollResponse.scrollId();

            exchange.setProperty("scrollID", scrollID);

            List<Hit<Map>> hits = scrollResponse.hits().hits();

            if (!hits.isEmpty()) {
                resources = getResourcesFromHits(resources, hits);
            }
            else {
                System.out.println("No more hits time to stop looping");
                exchange.getIn().setHeader("loop", false);
            }

            return resources;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    private ArrayList<Resource> getResourcesFromHits (ArrayList<Resource> resources, List<Hit<Map>> hits) {
        for (Hit<Map> hit : hits) {
            Map metadataDocument = hit.source();

            if (Objects.requireNonNull(metadataDocument).get("resource_files") != null) {
                ArrayList<Map> files = (ArrayList<Map>) metadataDocument
                        .get("resource_files");
                if (!files.isEmpty()) {
                    Iterator<Map> it = files.iterator();
                    while (it.hasNext()){
                        Map file = it.next();
                        String mimeType = file.get("mime_type").toString();
                        if(mimeType.equalsIgnoreCase("text/pdf")){
                            String url = ((ArrayList<Map>)file.get("location")).get(0).get("url").toString();
                            Resource resource =
                                    new Resource(
                                            metadataDocument.get("dataHARVEST_id").toString(),
                                            url);
                            resources.add(resource);
                            logger.debug(resource.toString());
                            break;
                        }
                    }
                }
            }
        }
        return resources;
    }

}
