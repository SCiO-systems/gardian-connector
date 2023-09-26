package services.scio.qvantum.beans;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.ScrollRequest;
import co.elastic.clients.elasticsearch.core.ScrollResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import org.apache.camel.Exchange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.scio.qvantum.models.resource.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GardianBean {

    private static final Logger logger = LogManager.getLogger(GardianBean.class);

    public List<Resource> getOpenDocuments(Exchange exchange){

        String alias  = exchange.getProperty("alias",String.class);
        int size  = exchange.getProperty("size",Integer.class);
        int from  = exchange.getProperty("from",Integer.class);

        ElasticsearchClient client = exchange.getContext().getRegistry()
                .lookupByNameAndType("es_client",ElasticsearchClient.class);

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
                            .from(from)
                            .size(size),
                    Map.class
            );
            List<Hit<Map>> hits = response.hits().hits();

            ArrayList<Resource> resources = new ArrayList<>();
            for (Hit<Map> hit: hits) {
                Map metadataDocument = hit.source();

                if(Objects.requireNonNull(metadataDocument).get("resource_files") != null){
                        ArrayList<Map> files = (ArrayList<Map>)metadataDocument
                                .get("resource_files");
                        if(!files.isEmpty()){
                            String mimeType = files.get(0).get("mime_type").toString();
                            if(mimeType.equalsIgnoreCase("text/pdf")){
                                String url = ((ArrayList)files.get(0).get("location")).get(0).toString();
                                Resource resource =
                                        new Resource(
                                                metadataDocument.get("dataHARVEST_id").toString(),
                                                url);
                                resources.add(resource);
                                logger.debug(resource.toString());
                            }
                        }
                }

            }

            logger.info("Gathered Resources: "+resources.size());
            logger.info("From: "+from);
            logger.info("Size: "+size);
            return resources;

        } catch (IOException e) {
            return null;
            /*ElasticsearchError ee = new ElasticsearchError(alias,"isCGIAR",e.getMessage());
            logger.error(ee);*/
        }
    }

}
