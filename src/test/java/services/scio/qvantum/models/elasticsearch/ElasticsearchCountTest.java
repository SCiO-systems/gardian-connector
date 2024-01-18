package services.scio.qvantum.models.elasticsearch;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import services.scio.qvantum.models.resource.Resource;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ElasticsearchCountTest {

    @Test
    public void testDefaultConstructor() {
        ElasticsearchCount count = new ElasticsearchCount();
        assertEquals(count.getClass(), ElasticsearchCount.class);
        assertEquals(null, count.getCount());
        assertEquals(null, count.getShards());
        assertEquals(count.getAdditionalProperties().getClass(), LinkedHashMap.class);
        assertEquals(new LinkedHashMap<String, Object>(), count.getAdditionalProperties());
    }

    @Test
    public void testParametrizedConstructor() {
        Integer count = Instancio.create(Integer.class);
        Shards sh = Instancio.create(Shards.class);
        ElasticsearchCount escount = new ElasticsearchCount(count, sh);
        assertEquals(escount.getClass(), ElasticsearchCount.class);
        assertEquals(count, escount.getCount());
        assertEquals(sh, escount.getShards());
        assertEquals(escount.getAdditionalProperties().getClass(), LinkedHashMap.class);
        assertEquals(new LinkedHashMap<String, Object>(), escount.getAdditionalProperties());
    }

    @Test
    public void testSetAndGet() {
        Integer count = Instancio.create(Integer.class);
        Shards sh = Instancio.create(Shards.class);
        String proname = Instancio.create(String.class);
        Object propval = Instancio.create(Object.class);
        Map<String, Object> additionalProps = new LinkedHashMap<String, Object>();
        additionalProps.put(proname, propval);
        ElasticsearchCount escount = new ElasticsearchCount();
        escount.setCount(count);
        escount.setShards(sh);
        escount.setAdditionalProperty(proname, propval);
        assertEquals(count, escount.getCount());
        assertEquals(sh, escount.getShards());
        assertEquals(additionalProps, escount.getAdditionalProperties());
    }

    @Test
    public void testToString() {
        ElasticsearchCount escount = Instancio.create(ElasticsearchCount.class);
        String expected = "ElasticsearchCount{" +
                "count=" + escount.getCount() +
                ", shards=" + escount.getShards() +
                ", additionalProperties=" + escount.getAdditionalProperties() +
                '}';
        assertEquals(expected, escount.toString());
    }


}