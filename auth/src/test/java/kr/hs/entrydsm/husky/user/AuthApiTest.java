package kr.hs.entrydsm.husky.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.hs.entrydsm.husky.AuthApplication;
import kr.hs.entrydsm.husky.config.RedisConfig;
import kr.hs.entrydsm.husky.dto.request.AccountRequest;
import kr.hs.entrydsm.husky.dto.response.TokenResponse;
import kr.hs.entrydsm.husky.entities.users.User;
import kr.hs.entrydsm.husky.entities.users.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AuthApplication.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "local"})
public class AuthApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        userRepository.save(
                User.builder()
                        .email("leaguelugas@test.com")
                        .password(passwordEncoder.encode("P@ssw0rd"))
                        .name("임용성")
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    @AfterEach
    public void clean() {
        userRepository.deleteAll();
    }

    @Test
    public void signInTest() throws Exception {
        signIn();
    }

    @Test
    public void refreshTest() throws Exception {
        String url = "http://localhost:" + port;

        String content = signIn().getResponse().getContentAsString();
        TokenResponse response = new ObjectMapper().readValue(content, TokenResponse.class);
        String refreshToken = response.getRefreshToken();

        mvc.perform(put(url + "/auth")
                .header("X-Refresh-Token", refreshToken))
                .andExpect(status().isOk());
    }

    private MvcResult signIn() throws Exception {
        String url = "http://localhost:" + port;

        AccountRequest accountRequest = new AccountRequest("leaguelugas@test.com", "P@ssw0rd");

        return mvc.perform(post(url + "/auth")
                .content(new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .writeValueAsString(accountRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andDo(print())
                .andReturn();
    }

}
