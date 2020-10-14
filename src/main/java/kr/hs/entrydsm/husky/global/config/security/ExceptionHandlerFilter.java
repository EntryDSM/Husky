package kr.hs.entrydsm.husky.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hs.entrydsm.husky.global.error.exception.BusinessException;
import kr.hs.entrydsm.husky.global.error.exception.ErrorCode;
import kr.hs.entrydsm.husky.global.slack.SlackSenderManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final SlackSenderManager slackSenderManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (BusinessException e) {
            this.setErrorResponse(response, e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            slackSenderManager.send(request, e);
            e.printStackTrace();
        }
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonValue = objectMapper.writer()
                .writeValueAsString(errorCode);
        response.getWriter().write(jsonValue);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorCode.getStatus());
    }

}
