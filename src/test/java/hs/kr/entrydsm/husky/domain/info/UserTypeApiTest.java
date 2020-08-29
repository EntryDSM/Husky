package hs.kr.entrydsm.husky.domain.info;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.hs.entrydsm.husky.infra.redis.EmbeddedRedisConfig;
import kr.hs.entrydsm.husky.HuskyApplication;
import kr.hs.entrydsm.husky.domain.user.dto.SelectTypeRequest;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GEDApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GraduatedApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.UnGraduatedApplicationRepository;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {HuskyApplication.class, EmbeddedRedisConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext
@ActiveProfiles("test")
class UserTypeApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GEDApplicationRepository gedApplicationRepository;

    @Autowired
    private UnGraduatedApplicationRepository unGraduatedApplicationRepository;

    @Autowired
    private GraduatedApplicationRepository graduatedApplicationRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    private void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        userRepository.save(User.builder()
                .email("test")
                .createdAt(LocalDateTime.now())
                .password("1234")
                .build());
    }

    @AfterEach
    public void cleanup() {
        gedApplicationRepository.deleteAll();
        graduatedApplicationRepository.deleteAll();
        unGraduatedApplicationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Order(1)
    @WithMockUser(username = "1", password = "1234")
    public void selectTypeApi() throws Exception {
        // given
        String url = "http://localhost:" + port;

        // when
        ResultActions resultActions = selectTypeRequest(url, "GRADUATED")
                .andExpect(status().isNoContent())
                .andDo(print());

        // then
        mvc.perform(get(url + "/process/me"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(2)
    @WithMockUser(username = "2", password = "1234")
    public void getTypeApi() throws Exception {
        //given
        String url = "http://localhost:" + port;

        //when
        selectTypeRequest(url, "GED");

        //then
        mvc.perform(get(url + "/users/me/type"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private ResultActions selectTypeRequest(String url, String gradeType) throws Exception {
        SelectTypeRequest request = SelectTypeRequest.builder()
                .gradeType(gradeType)
                .applyType("COMMON")
                .additionalType("NOT_APPLICABLE")
                .isDaejeon(true)
                .gedPassDate(LocalDate.parse("2020-02-20"))
                .graduatedDate(LocalDate.parse("2020-02-20"))
                .build();

        return mvc.perform(patch(url + "/users/me/type")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .writeValueAsString(request)));
    }
}
