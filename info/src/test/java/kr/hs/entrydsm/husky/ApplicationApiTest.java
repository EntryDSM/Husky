package kr.hs.entrydsm.husky;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.hs.entrydsm.husky.domain.application.dto.SetDocsRequest;
import kr.hs.entrydsm.husky.domain.application.dto.SetGedScoreRequest;
import kr.hs.entrydsm.husky.domain.application.dto.SetScoreRequest;
import kr.hs.entrydsm.husky.domain.user.dto.SelectTypeRequest;
import kr.hs.entrydsm.husky.entities.applications.repositories.GEDApplicationRepository;
import kr.hs.entrydsm.husky.entities.applications.repositories.UnGraduatedApplicationRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

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
    private GEDApplicationRepository gedApplicationRepository;

    @Autowired
    private UnGraduatedApplicationRepository unGraduatedApplicationRepository;

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
    @WithMockUser(username = "test7", password = "1234")
    public void setIntroAndPlanApi() throws Exception {
        //given
        String url = "http://localhost:" + port;

        //when
        setAndGetIntroApi();
        setAndGetPlanApi();

        //then
        mvc.perform(get(url + "/process/me"))
                .andExpect(status().isOk());
    }

    public void setAndGetIntroApi() throws Exception {
        //given
        String url = "http://localhost:" + port;

        //when
        mvc.perform(patch(url + "/applications/me/intro"))
                .andExpect(status().isBadRequest());

        //then
        mvc.perform(patch(url + "/applications/me/intro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertToJson(new SetDocsRequest("test"))))
                .andExpect(status().isNoContent());

        mvc.perform(get(url + "/applications/me/intro"))
                .andExpect(status().isOk());
    }

    public void setAndGetPlanApi() throws Exception {
        //given
        String url = "http://localhost:" + port;

        //when
        mvc.perform(patch(url + "/applications/me/plan"))
                .andExpect(status().isBadRequest());

        //then
        mvc.perform(patch(url + "/applications/me/plan")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertToJson(new SetDocsRequest("test"))))
                .andExpect(status().isNoContent());

        mvc.perform(get(url + "/applications/me/plan"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test6", password = "1234")
    public void setGedScoreApi() throws Exception {
        //given
        String url = "http://localhost:" + port;

        //when
        selectUserType(url, "GED");

        mvc.perform(patch(url + "/applications/me/score/ged")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertToJson(new SetGedScoreRequest(100))))
                .andExpect(status().isNoContent());

        //then
        mvc.perform(get(url + "/applications/me/score"))
                .andExpect(status().isOk());

        mvc.perform(get(url + "/process/me"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test7", password = "1234")
    public void setScoreApi() throws Exception {
        //given
        String url = "http://localhost:" + port;

        //when
        selectUserType(url, "UNGRADUATED");

        SetScoreRequest request = SetScoreRequest.builder()
                .volunteerTime(100)
                .fullCutCount(100)
                .periodCutCount(100)
                .lateCount(100)
                .earlyLeaveCount(100)
                .korean("XAAAA")
                .social("XAAAA")
                .history("XAAAA")
                .english("XAAAA")
                .math("XAAAA")
                .science("XAAAA")
                .techAndHome("XAAAA")
                .build();

        mvc.perform(patch(url + "applications/me/score")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertToJson(request)))
                .andExpect(status().isNoContent());

        //then
        mvc.perform(get(url + "/applications/me/score"))
                .andExpect(status().isOk());

        mvc.perform(get(url + "/process/me"))
                .andExpect(status().isOk());
    }

    private String convertToJson(Object object) throws JsonProcessingException {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .writeValueAsString(object);
    }

    private void selectUserType(String url, String gradeType) throws Exception {
        mvc.perform(patch(url + "/users/me/type")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .writeValueAsString(SelectTypeRequest.builder()
                                .gradeType(gradeType)
                                .applyType("COMMON")
                                .additionalType("NOT_APPLICABLE")
                                .isDaejeon(true)
                                .gedPassDate(LocalDate.parse("2020-02-20"))
                                .graduatedDate(LocalDate.parse("2020-02-20"))
                                .build()))
        );
    }

}
