package services.scio.qvantum.models.resource;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import services.scio.qvantum.models.resource.Resource;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ResourceTest {

    @Test
    public void testDefaultConstructor() {
        Resource input = new Resource();
        assertEquals(input.getClass(), Resource.class);
        assertEquals(null, input.getId());
        assertEquals(null, input.getUrl());
        assertEquals("gardian_index", input.getSource());
    }

    @Test
    public void testParametrizedConstructor() {
        String url = Instancio.create(String.class);
        String id = Instancio.create(String.class);
        Resource input = new Resource(id, url);
        assertEquals(input.getClass(), Resource.class);
        assertEquals(id, input.getId());
        assertEquals(url, input.getUrl());
        assertEquals("gardian_index", input.getSource());
    }

    @Test
    public void testSetAndGet() {
        String url = Instancio.create(String.class);
        String id = Instancio.create(String.class);
        String source = Instancio.create(String.class);
        Resource input = new Resource();
        input.setId(id);
        input.setSource(source);
        input.setUrl(url);
        assertEquals(url, input.getUrl());
        assertEquals(id, input.getId());
        assertEquals(source, input.getSource());
    }

    @Test
    public void testEqualsForEquality() {
        String url = Instancio.create(String.class);
        String id = Instancio.create(String.class);
        Resource input1 = new Resource(id, url);
        Resource input2 = new Resource(id, url);
        assertTrue(input1.equals(input2));
    }

    @Test
    public void testEqualsForInequality() {
        Resource input1 = Instancio.create(Resource.class);
        Resource input2 = Instancio.create(Resource.class);
        assertFalse(input1.equals(input2));
    }

    @Test
    public void testToString() {
        Resource input = Instancio.create(Resource.class);
        String url = input.getUrl();
        String id = input.getId();
        String source = input.getSource();
        String expected = "Resource{" +
                "id='" + id + '\'' +
                ", source='" + source + '\'' +
                ", url='" + url + '\'' +
                '}';
        assertEquals(expected, input.toString());
    }

    @Test
    public void testHashCode() {
        Resource input = Instancio.create(Resource.class);
        String url = input.getUrl();
        String id = input.getId();
        String source = input.getSource();
        int expectedHashCode = Objects.hash(id, source, url);
        assertEquals(expectedHashCode, input.hashCode());
    }

}