package kr.hs.entrydsm.husky.global.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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

    private String logContent(WrappedRequest request, HttpServletResponse response) {

        Map<String, String> headers = Collections.list(request.getHeaderNames()).stream()
                .collect(Collectors.toMap(header -> header, request::getHeader));
        String requestIp = Optional.ofNullable(request.getHeader("x-real-ip"))
                .orElse("127.0.0.1");
        String headerString = Arrays.toString(headers.entrySet().toArray());
        String paramString = Arrays.toString(getParams(request).entrySet().toArray());

        return List.of(
                request.getMethod(),
                request.getRequestURI(),
                requestIp,
                response.getStatus(),
                headerString,
                getBody(request),
                paramString
        ).toString();

    }

    private String getBody(WrappedRequest request) {
        try {
            String body = request.getBody();
            if (body.length() < 2)
                body = "{}";
            return body;
        } catch (Exception e) {
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
