package kr.hs.entrydsm.husky;

import kr.hs.entrydsm.husky.domain.schedule.dao.ScheduleRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest(classes = InfoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ScheduleApiTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @After
    public void tearDown() throws Exception {
        scheduleRepository.deleteAll();
    }

    @Test
    public void save_schedule() throws Exception {
        //given
        String id = "엔트리_멘토링";
        String startDate = "2020-07-23";
        String endDate = "2020-07-25";
        String url = "/schedules";

        //when
        ResultActions resultActions = this.mvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"" + id + "\"," +
                                "\"startDate\":\"" + startDate + "\"," +
                                "\"endDate\":\"" + endDate + "\"}")
        );

        //then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString(id)));
    }

}
