package services.scio.qvantum.models.resource;

import java.util.Objects;

public class Resource {

    private String id;

    private String source = "gardian_index";
    private String url;

    public Resource() {
    }

    public Resource(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return Objects.equals(getId(), resource.getId()) && Objects.equals(getSource(), resource.getSource()) && Objects.equals(getUrl(), resource.getUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSource(), getUrl());
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id='" + id + '\'' +
                ", source='" + source + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
