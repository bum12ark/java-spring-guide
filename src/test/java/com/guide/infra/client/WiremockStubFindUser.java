package com.guide.infra.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.serviceUnavailable;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.google.common.net.HttpHeaders;
import org.springframework.http.MediaType;

public class WiremockStubFindUser {

    private static final String FIND_USER_URL = "/api/users/%s";

    public static void stubFindUser(long userId, String expectedJsonResponse) {
        stubFor(
                get(String.format(FIND_USER_URL, userId))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(expectedJsonResponse)));
    }

    public static void stubFindUserThrowsConnectionTimeout(long userId) {
        stubFor(get(String.format(FIND_USER_URL, userId)).willReturn(aResponse().withFixedDelay(600)));
    }

    public static void stubFindUserThrowsNotFoundException(long userId) {
        stubFor(get(String.format(FIND_USER_URL, userId)).willReturn(notFound()));
    }

    public static void stubFindUserThrowsServerUnavailable(long userId) {
        stubFor(get(String.format(FIND_USER_URL, userId)).willReturn(serviceUnavailable()));
    }

    public static void verify(int expected, long userId) {
        WireMock.verify(
                exactly(expected), getRequestedFor(urlEqualTo(String.format(FIND_USER_URL, userId))));
    }
}
