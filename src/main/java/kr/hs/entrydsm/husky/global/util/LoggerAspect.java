package kr.hs.entrydsm.husky.global.util;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class LoggerAspect {

    @Pointcut("execution(* kr.hs.entrydsm.husky.domain.*.controller.*Controller.*(..))")
    public void loggerPointCut() {
    }

    @Around("loggerPointCut()")
    public Object methodLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            Object result = proceedingJoinPoint.proceed();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            String controllerName = proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName();
            String methodName = proceedingJoinPoint.getSignature().getName();

            Enumeration<String> headerNames = request.getHeaderNames();
            StringBuilder stringBuilder = new StringBuilder();
            if (headerNames != null) {
                while (headerNames.hasMoreElements())
                    stringBuilder.append(request.getHeader(headerNames.nextElement())).append("|");
            }

            Map<String, Object> params = new HashMap<>();
            try {
                params.put("method", request.getMethod());
                params.put("uri", request.getRequestURI());
                params.put("cont", controllerName);
                params.put("func", methodName);
                params.put("header", stringBuilder.toString());
                params.put("body", getBody(request));
                params.put("params", getParams(request));
            } catch (Exception e) {
                log.error("LoggerAspect error", e);
            }
            log.info("I, " + LocalDateTime.now() + " " + params.values().toString());

            return result;

        } catch (Throwable throwable) {
            throw throwable;
        }
    }

    private static HashMap<String, String> getParams(HttpServletRequest request) {
        HashMap<String, String> map = new HashMap<>();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            String replaceParam = param.replaceAll("\\.", "-");
            map.put(replaceParam, request.getParameter(param));
        }
        return map;
    }

    public static String getBody(HttpServletRequest request) throws IOException {
        try {
            return request.getReader().lines().collect(Collectors.joining());
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return "";
    }

}
