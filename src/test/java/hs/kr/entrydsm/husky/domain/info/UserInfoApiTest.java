package hs.kr.entrydsm.husky.domain.info;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.hs.entrydsm.husky.HuskyApplication;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GEDApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GraduatedApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.UnGraduatedApplicationRepository;
import kr.hs.entrydsm.husky.domain.school.domain.School;
import kr.hs.entrydsm.husky.domain.school.domain.repositories.SchoolRepository;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.enums.AdditionalType;
import kr.hs.entrydsm.husky.domain.user.domain.enums.ApplyType;
import kr.hs.entrydsm.husky.domain.user.domain.enums.GradeType;
import kr.hs.entrydsm.husky.domain.user.domain.enums.Sex;
import kr.hs.entrydsm.husky.domain.user.domain.repositories.UserRepository;
import kr.hs.entrydsm.husky.domain.user.dto.SelectTypeRequest;
import kr.hs.entrydsm.husky.domain.user.dto.SetUserInfoRequest;
import org.junit.jupiter.api.*;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {HuskyApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class UserInfoApiTest {

    @LocalServerPort
    private int port;

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
    @Order(1)
    @WithMockUser(username = "1", password = "1234")
    public void setAndGetInfoApi() throws Exception {
        //given
        String url = "http://localhost:" + port;

        //when
        SetUserInfoRequest request = SetUserInfoRequest.builder()
                .name("test")
                .sex(Sex.MALE)
                .birthDate(LocalDate.parse("2020-01-23"))
                .studentNumber("30120")
                .schoolCode("1")
                .schoolTel("010-1234-5678")
                .parentName("test2")
                .parentTel("010-1234-5678")
                .applicantTel("010-1234-5678")
                .homeTel("070-111-1111")
                .address("대전 유성구")
                .detailAddress("대덕 소프트웨어 마이스터 고등학교")
                .postCode("11111")
                .photo(null)
                .build();

        select_user_type(url, "UNGRADUATED");

        //then
        mvc.perform(patch(url + "/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isAccepted());

        mvc.perform(get(url + "/users/me"))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(get(url + "/process/me"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @WithMockUser(username = "2", password = "1234")
    public void setAndGetGedInfoApi() throws Exception {
        //given
        String url = "http://localhost:" + port;

        //when
        SetUserInfoRequest request = SetUserInfoRequest.builder()
                .name("test2")
                .sex(Sex.FEMALE)
                .birthDate(LocalDate.parse("2020-01-23"))
                .parentName("test2")
                .parentTel("010-1111-1111")
                .applicantTel("010-1234-5678")
                .homeTel("070-111-1111")
                .address("대전 유성구")
                .detailAddress("대덕소프트웨어마이스터고등학교")
                .postCode("11111")
                .photo("54c2c17d-ea4c-4d92-a6ea-c54d8beacb4f.png")
                .build();

        select_user_type(url, "GED");

        //then
        mvc.perform(patch(url + "/users/me/ged")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isAccepted());

        mvc.perform(patch(url + "/users/me/ged"))
                .andDo(print())
                .andExpect(status().isBadRequest());

        mvc.perform(get(url + "/users/me"))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(get(url + "/process/me"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private void select_user_type(String url, String gradeType) throws Exception {
        mvc.perform(patch(url + "/users/me/type")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .writeValueAsString(SelectTypeRequest.builder()
                                .gradeType(GradeType.valueOf(gradeType))
                                .applyType(ApplyType.COMMON)
                                .additionalType(AdditionalType.NOT_APPLICABLE)
                                .isDaejeon(true)
                                .gedPassDate(YearMonth.of(2020, 2))
                                .graduatedDate(YearMonth.of(2020, 2))
                                .build()))
        );
    }

    @Test
    @WithMockUser(username = "3", password = "1234")
    public void getUserStatus() throws Exception {
        //given
        String url = "http://localhost:" + port;

        //when
        mvc.perform(get(url + "/users/me/status"))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(patch(url + "/users/me/status"))
                .andDo(print())
                .andExpect(status().isNotAcceptable());

        mvc.perform(get(url + "/users/me/status"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
