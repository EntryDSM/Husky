package kr.hs.entrydsm.husky;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.hs.entrydsm.husky.domain.user.dto.SelectTypeRequest;
import kr.hs.entrydsm.husky.entities.applications.GraduatedApplication;
import kr.hs.entrydsm.husky.entities.applications.repositories.GEDApplicationRepository;
import kr.hs.entrydsm.husky.entities.applications.repositories.GraduatedApplicationRepository;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InfoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
                .receiptCode(1)
                .email("test")
                .createdAt(LocalDateTime.now())
                .password("1234")
                .build());

        userRepository.save(User.builder()
                .receiptCode(2)
                .email("test2")
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
    @WithMockUser(username = "test", password = "1234")
    public void select_type_api() throws Exception {
        //given
        String url = "http://localhost:" + port;

        //when
        ResultActions resultActions = select_type_request(url, "GRADUATED")
                .andDo(print())
                .andExpect(status().isNoContent());

//      then
        GraduatedApplication gred =
                ((List<GraduatedApplication>) graduatedApplicationRepository.findAll()).get(0);
        System.out.println(gred.getGraduatedDate() + " : " + gred.getCreatedAt());
//        GEDApplication ged = ((List<GEDApplication>) gedApplicationRepository.findAll()).get(0);
//
//        System.out.println(ged.getEmail() + " passed At " + ged.getGedPassDate() + " ged createdAt " +
//                ged.getCreatedAt());

//        UnGraduatedApplication ungred =
//                ((List<UnGraduatedApplication>) unGraduatedApplicationRepository.findAll()).get(0);
//        System.out.println(ungred.getCreatedAt());
    }

    @Test
    @WithMockUser(username = "test2", password = "1234")
    public void get_type_api() throws Exception {
        //given
        String url = "http://localhost:" + port;

        //when
        select_type_request(url, "GED");

        //then
        mvc.perform(get(url + "/users/me/type"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private ResultActions select_type_request(String url, String gradeType) throws Exception {
        SelectTypeRequest request = SelectTypeRequest.builder()
                .grade_type(gradeType)
                .apply_type("COMMON")
                .additional_type("NOT_APPLICABLE")
                .is_daejeon(true)
                .ged_pass_date(LocalDate.parse("2020-02-20"))
                .graduated_date(LocalDate.parse("2020-02-20"))
                .build();

        return mvc.perform(patch(url + "/users/me/type")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .writeValueAsString(request)));
    }
}
