package com.guide.infra.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserFindApiResponse(
        @JsonProperty("data") Data data, @JsonProperty("support") Support support) {

    public record Data(
            @JsonProperty("id") long id,
            @JsonProperty("email") String email,
            @JsonProperty("first_name") String firstName,
            @JsonProperty("last_name") String lastName,
            @JsonProperty("avatar") String avatar) {}

    public record Support(@JsonProperty("url") String url, @JsonProperty("text") String text) {}
}
