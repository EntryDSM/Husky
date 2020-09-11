package hs.kr.entrydsm.husky.domain.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import kr.hs.entrydsm.husky.domain.auth.dto.request.AccountRequest;
import kr.hs.entrydsm.husky.infra.redis.EmbeddedRedisConfig;
import kr.hs.entrydsm.husky.HuskyApplication;
import kr.hs.entrydsm.husky.domain.school.domain.School;
import kr.hs.entrydsm.husky.domain.school.domain.repositories.SchoolRepository;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ContextConfiguration(classes = {HuskyApplication.class, EmbeddedRedisConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SchoolApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private SchoolRepository schoolRepository;

    @BeforeEach
    private void setup() {

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        schoolRepository.save(new School("1234",
                "대마고",
                "대전교육청 대마고",
                "유성구"));

    }

    @Test
    private void return_schools_page() throws Exception {

        String url = "http://localhost:" + port;

        mvc.perform(get(url + "/schools")
                .param("eduOffice", "대")
                .param("name", "대")
                .param("page", "0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].school_code").value("1234"));

    }

    @Test
    private void invalid_parameter_error() throws Exception{

        String url = "http://localhost:" + port;

        mvc.perform(get(url + "/schools")
                .param("eduOffice", "대")
                .param("page", "0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("invalid parameter"));
        
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
