package kr.hs.entrydsm.husky.global.slack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import kr.hs.entrydsm.husky.global.slack.dto.Attachment;
import kr.hs.entrydsm.husky.global.slack.dto.Field;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackSenderManager {

    @Value("${slack.webhook.url}")
    private String webHookUrl;

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    public void send(Exception exception) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        this.send(request, exception);
    }

    public void send(HttpServletRequest request, Exception exception) {
        SlackMessageRequest attachments = this.toAttachments(request, exception);
        restTemplate.postForEntity(webHookUrl, writeValueAsString(attachments), String.class);
    }

    private String getBody(HttpServletRequest request) {
        try {
            return request.getReader().lines().collect(Collectors.joining());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return "";
    }

    private String getHeader(HttpServletRequest request) {
        Map<String, String> headers = Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(h -> h, request::getHeader));
        return Arrays.toString(headers.entrySet().toArray());
    }

    private SlackMessageRequest toAttachments(HttpServletRequest request, Exception exception) {
        return SlackMessageRequest.builder()
                .attachments(List.of(
                        Attachment.builder()
                                .color("#FF0000")
                                .pretext("[ERROR] An error has occurred below.")
                                .authorName("Bug Reporter")
                                .title(exception.getLocalizedMessage())
                                .text(getStackTrace(exception))
                                .fields(List.of(
                                        Field.builder()
                                                .title("Request URI")
                                                .value(request.getRequestURI())
                                                .build(),
                                        Field.builder()
                                                .title("Request Header")
                                                .value(getHeader(request))
                                                .build(),
                                        Field.builder()
                                                .title("Request Body")
                                                .value(getBody(request))
                                                .build()
                                ))
                                .footer("Bug Reporter")
                                .ts(System.currentTimeMillis() / 1000)
                                .build()))
                .build();
    }

    private String getStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    private String writeValueAsString(Object obj) {
        try {
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(String.format("JsonProcessingException occurred: %s", e));
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @PostConstruct
    public void initialize() {
        this.objectMapper = new ObjectMapper();
        this.restTemplate = new RestTemplate();
    }

}
