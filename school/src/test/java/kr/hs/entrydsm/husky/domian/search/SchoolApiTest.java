package kr.hs.entrydsm.husky.domian.search;

import kr.hs.entrydsm.husky.SchoolApplication;
import kr.hs.entrydsm.husky.entities.schools.School;
import kr.hs.entrydsm.husky.entities.schools.repositories.SchoolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {SchoolApplication.class})
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
    public void setup() {

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        schoolRepository.save(new School("1234",
                "대마고",
                "대전교육청 대마고",
                "유성구"));
    }

    @Test
    public void return_schools_page() throws Exception {

        String url = "http://localhost:" + port;

        mvc.perform(get(url + "/schools")
                .param("eduOffice", "대")
                .param("name", "대")
                .param("page", "0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].schoolCode").value("1234"));
    }

    @Test
    public void invalid_parameter_error() throws Exception{

        String url = "http://localhost:" + port;

        mvc.perform(get(url + "/schools")
                .param("eduOffice", "대")
                .param("page", "0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("invalid parameter"));
    }

}
