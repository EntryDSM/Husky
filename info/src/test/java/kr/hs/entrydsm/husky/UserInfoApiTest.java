package kr.hs.entrydsm.husky;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.hs.entrydsm.husky.domain.user.dto.SelectTypeRequest;
import kr.hs.entrydsm.husky.domain.user.dto.SetUserInfoRequest;
import kr.hs.entrydsm.husky.entities.applications.repositories.GEDApplicationRepository;
import kr.hs.entrydsm.husky.entities.applications.repositories.GraduatedApplicationRepository;
import kr.hs.entrydsm.husky.entities.applications.repositories.UnGraduatedApplicationRepository;
import kr.hs.entrydsm.husky.entities.schools.School;
import kr.hs.entrydsm.husky.entities.schools.repositories.SchoolRepository;
import kr.hs.entrydsm.husky.entities.users.User;
import kr.hs.entrydsm.husky.entities.users.repositories.UserRepository;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {InfoApplication.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserInfoApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private GEDApplicationRepository gedApplicationRepository;

    @Autowired
    private UnGraduatedApplicationRepository unGraduatedApplicationRepository;

    @Autowired
    private GraduatedApplicationRepository graduatedApplicationRepository;

    @BeforeEach
    private void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        userRepository.save(User.builder()
                .receiptCode(1)
                .email("test3")
                .createdAt(LocalDateTime.now())
                .password("1234")
                .build());

        userRepository.save(User.builder()
                .receiptCode(2)
                .email("test4")
                .createdAt(LocalDateTime.now())
                .password("1234")
                .build());

        schoolRepository.save(School.builder()
                .schoolName("대마고")
                .schoolFullName("대전 대마고")
                .schoolAddress("대전 유성구")
                .schoolCode("1")
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
    @WithMockUser(username = "test3", password = "1234")
    public void set_and_get_info_api() throws Exception {
        //given
        String url = "http://localhost:" + port;

        //when
        SetUserInfoRequest request = SetUserInfoRequest.builder()
                .name("test")
                .sex("MALE")
                .birth_date(LocalDate.parse("2020-01-23"))
                .student_number("30120")
                .school_code("1")
                .school_tel("010-0000-0000")
                .parent_name("test2")
                .parent_tel("010-1111-1111")
                .applicant_tel("010-0000-1111")
                .address("대전 유성구")
                .detail_address("대덕 소프트웨어마이스터 고등학교")
                .post_code("11111")
                .photo("test.png")
                .build();

        select_user_type(url, "UNGRADUATED");

        //then
        mvc.perform(post(url + "/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated());

        mvc.perform(get(url + "/users/me"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test4", password = "1234")
    public void set_and_get_ged_info_api() throws Exception {
        //given
        String url = "http://localhost:" + port;

        //when
        SetUserInfoRequest request = SetUserInfoRequest.builder()
                .name("test2")
                .sex("FEMALE")
                .birth_date(LocalDate.parse("2020-01-23"))
                .parent_name("test2")
                .parent_tel("010-1111-1111")
                .applicant_tel("010-0000-1111")
                .address("대전 유성구")
                .detail_address("대덕 소프트웨어마이스터 고등학교")
                .post_code("11111")
                .photo("test.png")
                .build();

        select_user_type(url, "GED");

        //then
        mvc.perform(post(url + "/users/me/ged")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated());

        mvc.perform(get(url + "/users/me"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private void select_user_type(String url, String gradeType) throws Exception {
        mvc.perform(patch(url + "/users/me/type")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .writeValueAsString(SelectTypeRequest.builder()
                                .grade_type(gradeType)
                                .apply_type("COMMON")
                                .additional_type("NOT_APPLICABLE")
                                .is_daejeon(true)
                                .ged_pass_date(LocalDate.parse("2020-02-20"))
                                .graduated_date(LocalDate.parse("2020-02-20"))
                                .build()))
        );
    }
}
