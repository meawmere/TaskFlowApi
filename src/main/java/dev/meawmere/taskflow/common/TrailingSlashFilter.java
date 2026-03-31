package dev.meawmere.taskflow.common;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TrailingSlashFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI();

        if (uri.length() > 1 && uri.endsWith("/")) {
            String newUri = uri.substring(0, uri.length() - 1);

            HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(req) {
                @Override
                public String getRequestURI() {
                    return newUri;
                }

                @Override
                public StringBuffer getRequestURL() {
                    StringBuffer url = new StringBuffer(req.getRequestURL());
                    if (url.length() > 0 && url.charAt(url.length() - 1) == '/') {
                        url.deleteCharAt(url.length() - 1);
                    }
                    return url;
                }
            };

            chain.doFilter(wrapper, response);
            return;
        }

        chain.doFilter(request, response);
    }
}