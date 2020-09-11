package kr.hs.entrydsm.husky.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hs.entrydsm.husky.global.error.exception.BusinessException;
import kr.hs.entrydsm.husky.global.error.exception.ErrorCode;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(request, response);
        } catch (BusinessException e) {
            this.setErrorResponse(response, e.getErrorCode());
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
