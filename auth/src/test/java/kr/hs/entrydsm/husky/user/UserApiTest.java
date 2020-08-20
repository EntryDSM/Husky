package kr.hs.entrydsm.husky.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import kr.hs.entrydsm.husky.AuthApplication;
import kr.hs.entrydsm.husky.dto.request.AccountRequest;
import kr.hs.entrydsm.husky.dto.request.ChangePasswordRequest;
import kr.hs.entrydsm.husky.dto.request.VerifyCodeRequest;
import kr.hs.entrydsm.husky.entities.users.User;
import kr.hs.entrydsm.husky.entities.users.repositories.UserRepository;
import kr.hs.entrydsm.husky.entities.verification.EmailVerification;
import kr.hs.entrydsm.husky.entities.verification.EmailVerificationRepository;
import kr.hs.entrydsm.husky.entities.verification.EmailVerificationStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AuthApplication.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "local"})
class UserApiTest {

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

    @Test
    public void emailVerifyTest() throws Exception {
        VerifyCodeRequest request = new VerifyCodeRequest("leaguelugas@test.com", "ABCDE");
        requestMvc(put("/user/email/verify"), request);
    }

    @Test
    public void signUpTest() throws Exception {
        emailVerifyTest();
        AccountRequest request = new AccountRequest("leaguelugas@test.com", "P@ssw0rd");
        requestMvc(post("/user"), request);
    }

    @Test
    public void passwordChangeTest() throws Exception {
        signUpTest();
        ChangePasswordRequest request = new ChangePasswordRequest("leaguelugas@test.com", "P@ssw0rd123");
        requestMvc(put("/user/password"), request);

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
