package kr.hs.entrydsm.husky.global.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class RequestLogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        WrappedRequest wrappedRequest = new WrappedRequest(request);
        this.doFilter(wrappedRequest, response, filterChain);
        log.info(logContent(wrappedRequest, response));
    }

    private String logContent(HttpServletRequest request, HttpServletResponse response) {

        Map<String, String> headers = Collections.list(request.getHeaderNames()).stream()
                .collect(Collectors.toMap(header -> header, request::getHeader));
        String headerString = Arrays.toString(headers.entrySet().toArray());
        String paramString = Arrays.toString(getParams(request).entrySet().toArray());

        return List.of(
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                headerString,
                getBody(request),
                paramString
        ).toString();

    }

    private String getBody(HttpServletRequest request) {
        try {
            String body = request.getReader().lines().collect(Collectors.joining());
            if (body.length() < 2)
                body = "{}";
            return body;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "{}";
    }

    private Map<String, String> getParams(HttpServletRequest request) {
        HashMap<String, String> map = new HashMap<>();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            String replaceParam = param.replaceAll("\\.", "-");
            map.put(replaceParam, request.getParameter(param));
        }
        return map;
    }

}
