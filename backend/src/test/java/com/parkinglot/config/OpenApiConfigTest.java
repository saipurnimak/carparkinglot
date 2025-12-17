package com.parkinglot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    @Test
    void customOpenAPI_containsExpectedInfoAndSecurity() {
        OpenApiConfig cfg = new OpenApiConfig();
        OpenAPI api = cfg.customOpenAPI();

        assertNotNull(api.getInfo());
        assertEquals("Parking Garage API", api.getInfo().getTitle());
        assertEquals("1.0.0", api.getInfo().getVersion());
        assertNotNull(api.getInfo().getContact());
        assertEquals("support@parkinggarage.com", api.getInfo().getContact().getEmail());

        Components comps = api.getComponents();
        assertNotNull(comps);
        assertTrue(comps.getSecuritySchemes().containsKey("Bearer Authentication"));
        assertFalse(api.getSecurity().isEmpty());
    }
}
