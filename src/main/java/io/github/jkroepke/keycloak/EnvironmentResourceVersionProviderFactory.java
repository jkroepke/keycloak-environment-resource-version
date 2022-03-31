package io.github.jkroepke.keycloak;

import org.keycloak.Config;
import org.keycloak.common.Version;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;

public class EnvironmentResourceVersionProviderFactory implements RealmResourceProviderFactory {
    public static final String ID = "environment-resource-version";
    private static final String KEYCLOAK_THEME_RESOURCE_VERSION = "KEYCLOAK_THEME_RESOURCE_VERSION";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public int order() {
        return 9999;
    }

    @Override
    public RealmResourceProvider create(KeycloakSession keycloakSession) {
        return new EnvironmentResourceVersionProvider(keycloakSession);
    }

    @Override
    public void init(Config.Scope scope) {
        setResourceVersion();
    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {
        setResourceVersion();
    }

    @Override
    public void close() {
    }

    private void setResourceVersion() {
        String resourceVersion = System.getenv(KEYCLOAK_THEME_RESOURCE_VERSION);

        if (resourceVersion != null) {
            Version.RESOURCES_VERSION = resourceVersion;
        }
    }
}
