package hs.kr.entrydsm.husky.domain.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import kr.hs.entrydsm.husky.infra.redis.EmbeddedRedisConfig;
import kr.hs.entrydsm.husky.HuskyApplication;
import kr.hs.entrydsm.husky.domain.auth.dto.request.AccountRequest;
import kr.hs.entrydsm.husky.domain.auth.dto.request.ChangePasswordRequest;
import kr.hs.entrydsm.husky.domain.auth.dto.request.VerifyCodeRequest;
import kr.hs.entrydsm.husky.domain.auth.domain.verification.EmailVerification;
import kr.hs.entrydsm.husky.domain.auth.domain.verification.EmailVerificationRepository;
import kr.hs.entrydsm.husky.domain.auth.domain.verification.EmailVerificationStatus;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.repositories.UserRepository;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles({"test", "local"})
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {HuskyApplication.class, EmbeddedRedisConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        emailVerificationRepository.save(
                EmailVerification.builder()
                        .email("leaguelugas@test.com")
                        .authCode("ABCDE")
                        .status(EmailVerificationStatus.UNVERIFIED)
                        .build()
        );
    }

    @AfterEach
    public void clean() {
        emailVerificationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Tag("Second")
    @Test
    public void emailVerifyTest() throws Exception {
        VerifyCodeRequest request = new VerifyCodeRequest("leaguelugas@test.com", "ABCDE");
        requestMvc(put("/users/email/verify"), request);
    }

    @Tag("Second")
    @Test
    public void signUpTest() throws Exception {
        emailVerifyTest();
        AccountRequest request = new AccountRequest("leaguelugas@test.com", "P@ssw0rd");
        requestMvc(post("/users"), request);
    }

    @Tag("Second")
    @Test
    public void passwordChangeTest() throws Exception {
        signUpTest();
        ChangePasswordRequest request = new ChangePasswordRequest("leaguelugas@test.com", "P@ssw0rd123");
        requestMvc(put("/users/password"), request);

        User user = userRepository.findByEmail("leaguelugas@test.com")
                .orElseThrow();

        assert passwordEncoder.matches("P@ssw0rd123", user.getPassword());
    }

    private void requestMvc(MockHttpServletRequestBuilder method, Object obj) throws Exception {
        String baseUrl = "http://localhost:" + port;

        mvc.perform(method
                .content(new ObjectMapper()
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .writeValueAsString(obj))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

}
