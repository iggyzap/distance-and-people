package com.izapolsky.distance.search.api;

import com.fasterxml.jackson.annotation.*;

import java.math.BigDecimal;

/**
 * Simple value object to read stream of json data
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
public class Customer {

    public String name;
    @JsonProperty("user_id")
    public Long userId;
    public BigDecimal longitude;
    public BigDecimal latitude;
}
