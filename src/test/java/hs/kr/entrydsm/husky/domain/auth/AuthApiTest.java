package hs.kr.entrydsm.husky.domain.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import kr.hs.entrydsm.husky.infra.redis.EmbeddedRedisConfig;
import kr.hs.entrydsm.husky.HuskyApplication;
import kr.hs.entrydsm.husky.domain.auth.dto.request.AccountRequest;
import kr.hs.entrydsm.husky.domain.auth.dto.response.TokenResponse;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.repositories.UserRepository;
import org.junit.BeforeClass;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {HuskyApplication.class, EmbeddedRedisConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles({"test", "local"})
class AuthApiTest {

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
                        .build()
        );
    }

    @AfterEach
    public void clean() {
        userRepository.deleteAll();
    }

    @Tag("First")
    @Test
    public void signInTest() throws Exception {
        signIn();
    }

    @Tag("First")
    @Test
    public void refreshTest() throws Exception {
        String url = "http://localhost:" + port;

        String content = signIn().getResponse().getContentAsString();
        TokenResponse response = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .readValue(content, TokenResponse.class);
        String refreshToken = response.getRefreshToken();

        mvc.perform(put(url + "/auth")
                .header("X-Refresh-Token", refreshToken))
                .andExpect(status().isOk());
    }

    @BeforeClass
    private MvcResult signIn() throws Exception {
        String url = "http://localhost:" + port;

        AccountRequest accountRequest = new AccountRequest("leaguelugas@test.com", "P@ssw0rd");

        return mvc.perform(post(url + "/auth")
                .content(new ObjectMapper()
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .writeValueAsString(accountRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andDo(print())
                .andReturn();
    }

}
