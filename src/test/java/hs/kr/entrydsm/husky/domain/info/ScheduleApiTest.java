package hs.kr.entrydsm.husky.domain.info;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.hs.entrydsm.husky.infra.redis.EmbeddedRedisConfig;
import kr.hs.entrydsm.husky.HuskyApplication;
import kr.hs.entrydsm.husky.domain.schedule.dao.ScheduleRepository;
import kr.hs.entrydsm.husky.domain.schedule.domain.Schedule;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {HuskyApplication.class, EmbeddedRedisConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "value=test")
@ActiveProfiles({"test", "local"})
class ScheduleApiTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Value("${value}")
    private String secret;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @After
    public void tearDown() {
        scheduleRepository.deleteAll();
    }

    @Test
    public void save_schedule() throws Exception {
        //given
        String id = "엔트리_멘토링";
        String startDate = "2020-07-23 12:30";
        String endDate = "2020-07-25 09:40";

        //when
        ResultActions resultActions = this.postRequest(id, startDate, endDate, this.secret);

        //then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString(id)));
    }

    @Test
    public void save_forbidden() throws Exception {
        //given
        String id = "엔트리_멘토링";
        String startDate = "2020-07-23 12:30";
        String endDate = "2020-07-25 09:40";

        //when
        ResultActions resultActions = this.postRequest(id, startDate, endDate, "entry");

        //then
        resultActions
                .andExpect(status().isForbidden());
    }

    @Test
    public void get_schedules() throws Exception {
        //given
        this.postRequest("test", "2020-07-26 12:30", "2020-07-31 06:30", this.secret);
        this.postRequest("develop", "2020-07-23 13:40", "2020-07-25 09:40", this.secret);
        String url = "/schedules";

        //when
        ResultActions resultActions = this.mvc.perform(get(url).characterEncoding("utf-8"));

        //then
        resultActions
                .andExpect(status().isOk())
                .andDo(print());
    }

    private ResultActions postRequest(String id, String startDate, String endDate, String secretKey) throws Exception {
        String url = "/schedules";
        return this.mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("secret", secretKey)
                .content(convertObjectToJson(Schedule.builder()
                        .id(id)
                        .startDate(LocalDateTime.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                        .endDate(LocalDateTime.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                        .build())
                )
        );
    }

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return mapper.writeValueAsString(object);
    }

}
