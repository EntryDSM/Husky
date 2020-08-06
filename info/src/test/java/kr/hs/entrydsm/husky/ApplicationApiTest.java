package kr.hs.entrydsm.husky;

import kr.hs.entrydsm.husky.entities.users.User;
import kr.hs.entrydsm.husky.entities.users.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InfoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ApplicationApiTest {

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
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        userRepository.save(User.builder()
                .receiptCode(6)
                .email("test6")
                .createdAt(LocalDateTime.now())
                .password("1234")
                .build());

        userRepository.save(User.builder()
                .receiptCode(7)
                .email("test7")
                .createdAt(LocalDateTime.now())
                .password("1234")
                .build());
    }

    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "test6", password = "1234")
    public void add_and_get_intro_api() throws Exception {
        //given
        String url = "http://localhost:" + port;

        //when
        mvc.perform(patch(url + "/applications/me/intro"))
                .andExpect(status().isBadRequest());

        //then
        mvc.perform(patch(url + "/applications/me/intro")
                .contentType(MediaType.APPLICATION_JSON)
                .content("안녕하세요"))
                .andExpect(status().isNoContent());

        mvc.perform(get(url + "/applications/me/intro"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test7", password = "1234")
    public void add_and_get_plan_api() throws Exception {
        //given
        String url = "http://localhost:" + port;

        //when
        mvc.perform(patch(url + "/applications/me/plan"))
                .andExpect(status().isBadRequest());

        //then
        mvc.perform(patch(url + "/applications/me/plan")
                .contentType(MediaType.APPLICATION_JSON)
                .content("안녕하세요"))
                .andExpect(status().isNoContent());

        mvc.perform(get(url + "/applications/me/plan"))
                .andExpect(status().isOk());
    }

}
