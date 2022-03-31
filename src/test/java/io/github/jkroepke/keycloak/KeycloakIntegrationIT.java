package io.github.jkroepke.keycloak;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.output.ToStringConsumer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class KeycloakIntegrationIT {
    private static final String KEYCLOAK_VERSION = System.getProperty("keycloak.version", "17.0.1");
    protected static final String KEYCLOAK_IMAGE = System.getProperty("keycloak.dockerImage", "quay.io/keycloak/keycloak");

    @Test
    public void shouldDeployProviderAndCallCustomEndpoint() throws Exception {
        String KEYCLOAK_THEME_RESOURCE_VERSION = "special-theme-version-tag";

        try (KeycloakContainer keycloak = new KeycloakContainer(KEYCLOAK_IMAGE + ":" + KEYCLOAK_VERSION)
                .withEnv("KEYCLOAK_THEME_RESOURCE_VERSION", KEYCLOAK_THEME_RESOURCE_VERSION)
                .withProviderClassesFrom("target/classes")) {
            keycloak.start();

            URL obj = new URL(keycloak.getAuthServerUrl());
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();

            assertThat(responseCode, is(HttpURLConnection.HTTP_OK));

            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                assertThat(response.toString(), containsString("/" + KEYCLOAK_THEME_RESOURCE_VERSION + "/"));
            }
        }
    }
}
