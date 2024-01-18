package services.scio.qvantum.models.elasticsearch;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ShardsTest {

    @Test
    public void testDefaultConstructor() {
        Shards shard = new Shards();
        assertEquals(shard.getClass(), Shards.class);
        assertEquals(null, shard.getTotal());
        assertEquals(null, shard.getSuccessful());
        assertEquals(null, shard.getSkipped());
        assertEquals(null, shard.getFailed());
        assertEquals(shard.getAdditionalProperties().getClass(), LinkedHashMap.class);
        assertEquals(new LinkedHashMap<String, Object>(), shard.getAdditionalProperties());
    }

    @Test
    public void testParametrizedConstructor() {
        Integer total = Instancio.create(Integer.class);
        Integer succesfull = Instancio.create(Integer.class);
        Integer skipped = Instancio.create(Integer.class);
        Integer failed = Instancio.create(Integer.class);
        Shards shard = new Shards(total, succesfull, skipped, failed);
        assertEquals(shard.getClass(), Shards.class);
        assertEquals(total, shard.getTotal());
        assertEquals(skipped, shard.getSkipped());
        assertEquals(succesfull, shard.getSuccessful());
        assertEquals(failed, shard.getFailed());
    }

    @Test
    public void testSetAndGet() {
        Integer total = Instancio.create(Integer.class);
        Integer succesfull = Instancio.create(Integer.class);
        Integer skipped = Instancio.create(Integer.class);
        Integer failed = Instancio.create(Integer.class);
        String proname = Instancio.create(String.class);
        Object propval = Instancio.create(Object.class);
        Map<String, Object> additionalProps = new LinkedHashMap<String, Object>();
        additionalProps.put(proname, propval);
        Shards shard = new Shards();
        shard.setTotal(total);
        shard.setFailed(failed);
        shard.setSuccessful(succesfull);
        shard.setSkipped(skipped);
        shard.setAdditionalProperty(proname, propval);
        assertEquals(total, shard.getTotal());
        assertEquals(failed, shard.getFailed());
        assertEquals(succesfull, shard.getSuccessful());
        assertEquals(skipped, shard.getSkipped());
        assertEquals(additionalProps, shard.getAdditionalProperties());
    }

    @Test
    public void testEqualsForEquality() {
        Integer total = Instancio.create(Integer.class);
        Integer succesfull = Instancio.create(Integer.class);
        Integer skipped = Instancio.create(Integer.class);
        Integer failed = Instancio.create(Integer.class);
        Shards shard = new Shards(total, succesfull, skipped, failed);
        Shards shard2 = new Shards(total, succesfull, skipped, failed);
        assertTrue(shard.equals(shard2));
    }

    @Test
    public void testEqualsForInequality() {
        Shards shard1 = Instancio.create(Shards.class);
        Shards shard2 = Instancio.create(Shards.class);
        assertFalse(shard1.equals(shard2));
    }

    @Test void testEqualsForTypeInequality() {
        Shards sh = Instancio.create(Shards.class);
        Object blah = Instancio.create(Object.class);
        assertFalse(sh.equals(blah));
    }

    @Test
    public void testToString() {
        Shards shard = Instancio.create(Shards.class);
        Integer total = shard.getTotal();
        Integer failed = shard.getFailed();
        Integer successful = shard.getSuccessful();
        Integer skipped = shard.getSkipped();
        Map<String, Object> additionalProps = shard.getAdditionalProperties();

        StringBuilder sb = new StringBuilder();
        sb.append(Shards.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(shard))).append('[');
        sb.append("total");
        sb.append('=');
        sb.append(total);
        sb.append(',');
        sb.append("successful");
        sb.append('=');
        sb.append(successful);
        sb.append(',');
        sb.append("skipped");
        sb.append('=');
        sb.append(skipped);
        sb.append(',');
        sb.append("failed");
        sb.append('=');
        sb.append(failed);
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(additionalProps);
        sb.append(',');
        sb.setCharAt((sb.length()- 1), ']');

        assertEquals(sb.toString(), shard.toString());

        Shards shard2 = new Shards(total, successful, skipped, failed);
        sb = new StringBuilder();
        sb.append(Shards.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(shard2))).append('[');
        sb.append("total");
        sb.append('=');
        sb.append(total);
        sb.append(',');
        sb.append("successful");
        sb.append('=');
        sb.append(successful);
        sb.append(',');
        sb.append("skipped");
        sb.append('=');
        sb.append(skipped);
        sb.append(',');
        sb.append("failed");
        sb.append('=');
        sb.append(failed);
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        additionalProps = new LinkedHashMap<String, Object>();
        sb.append((additionalProps == null)?"<null>":additionalProps);
        sb.append(']');

        assertEquals(sb.toString(), shard2.toString());
        System.out.println(shard.toString());

    }

    @Test
    public void testHashCode() {
        Shards shard = Instancio.create(Shards.class);
        Integer total = shard.getTotal();
        Integer failed = shard.getFailed();
        Integer successful = shard.getSuccessful();
        Integer skipped = shard.getSkipped();
        Map<String, Object> additionalProps = shard.getAdditionalProperties();
        int result = 1;
        result = ((result* 31)+((total == null)? 0 :total.hashCode()));
        result = ((result* 31)+((failed == null)? 0 :failed.hashCode()));
        result = ((result* 31)+((additionalProps == null)? 0 :additionalProps.hashCode()));
        result = ((result* 31)+((successful == null)? 0 :successful.hashCode()));
        result = ((result* 31)+((skipped == null)? 0 :skipped.hashCode()));
        assertEquals(result, shard.hashCode());
    }

}