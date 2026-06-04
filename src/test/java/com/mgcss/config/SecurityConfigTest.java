package com.mgcss.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("testcontainers")
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testApiPathsArePublic() throws Exception {
        // Asumiendo que /api/solicitudes no requiere auth según SecurityConfig
        // Pero puede que devuelva 404 si no está el endpoint exacto, sin embargo
        // comprobamos que no devuelva 401 o 302 a /login
        mockMvc.perform(get("/api/solicitudes"))
                // Puede ser 200, 404, etc., pero NO 302 a login o 401
                .andExpect(status().isOk());
    }

    @Test
    public void testWebPathsAreSecured() throws Exception {
        // Sin autenticar debe redirigir a /login (302)
        mockMvc.perform(get("/solicitudes"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testWebPathsAccessibleWithAuth() throws Exception {
        // Con usuario mockeado debe devolver 200
        mockMvc.perform(get("/solicitudes").with(user("admin").password("admin123").roles("USER")))
                .andExpect(status().isOk());
    }
}
