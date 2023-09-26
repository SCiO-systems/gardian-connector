package services.scio.qvantum.models.elasticsearch;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;

public class ElasticsearchCount {

    @JsonProperty("count")
    private Integer count;
    @JsonProperty("_shards")
    private Shards shards;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    public ElasticsearchCount() {
    }

    public ElasticsearchCount(Integer count, Shards shards) {
        this.count = count;
        this.shards = shards;
        this.additionalProperties = additionalProperties;
    }

    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    @JsonProperty("count")
    public void setCount(Integer count) {
        this.count = count;
    }

    @JsonProperty("_shards")
    public Shards getShards() {
        return shards;
    }

    @JsonProperty("_shards")
    public void setShards(Shards shards) {
        this.shards = shards;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "ElasticsearchCount{" +
                "count=" + count +
                ", shards=" + shards +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}
