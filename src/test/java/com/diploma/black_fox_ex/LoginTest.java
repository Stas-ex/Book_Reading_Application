package com.diploma.black_fox_ex;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DisplayName("Login test")
@RunWith(SpringRunner.class)
public class LoginTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Проверка запрещенных ссылок")
    public void redirectLoginPageTest() throws Exception {
        List<String> links = List.of(
                "/profile", "/book/new",
                "/book/favorite/1", "/book/n/comment",
                "/profile-books/1", "/help"
        );

        for (String link : links) {
            mvc.perform(get(link))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("http://localhost/login"));
        }
    }

    @Test
    public void badCredentials() throws Exception {
        this.mvc.perform(post("/login")
                        .param("user", "Stanislav")
                        .param("password", "123"))
                .andExpect(status().isForbidden());
    }

}
