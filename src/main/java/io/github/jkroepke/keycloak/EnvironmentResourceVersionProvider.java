package io.github.jkroepke.keycloak;

import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

class EnvironmentResourceVersionProvider implements RealmResourceProvider {
    public EnvironmentResourceVersionProvider(KeycloakSession session) { }

    @Override
    public Object getResource() {
        return this;
    }

    @Override
    public void close() {
    }
}
