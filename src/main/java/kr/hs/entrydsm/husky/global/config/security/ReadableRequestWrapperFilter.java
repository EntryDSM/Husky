package kr.hs.entrydsm.husky.global.config.security;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.*;

@Component
public class ReadableRequestWrapperFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RereadableRequestWrapper rereadableRequestWrapper = new RereadableRequestWrapper((HttpServletRequest)request);
        chain.doFilter(rereadableRequestWrapper, response);
    }

}
