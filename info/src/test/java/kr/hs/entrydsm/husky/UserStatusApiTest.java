package kr.hs.entrydsm.husky;

import kr.hs.entrydsm.husky.domain.user.exception.UserNotFoundException;
import kr.hs.entrydsm.husky.entities.users.User;
import kr.hs.entrydsm.husky.entities.users.enums.Sex;
import kr.hs.entrydsm.husky.entities.users.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InfoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserStatusApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    private void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        userRepository.save(User.builder()
                .receiptCode(5)
                .name("huewilliams")
                .sex(Sex.MALE)
                .email("test5")
                .createdAt(LocalDateTime.now())
                .password("1234")
                .build());

    }

    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "test5", password = "1234")
    public void getUserStatus() throws Exception {
        //given
        String url = "http://localhost:" + port;

        //when
        mvc.perform(get(url + "/users/me/status"))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(patch(url + "/users/me/status"))
                .andDo(print())
                .andExpect(status().isOk());

        //then
        User user = userRepository.findByEmail("test5").orElseThrow(UserNotFoundException::new);
        System.out.println(user.getStatus().getUser().getName() + " final submitted : " +
                user.getStatus().isFinalSubmit());
    }
}
