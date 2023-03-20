package com.tm.gogo.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.UUID;

@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String LOG_ID = "";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        request.setAttribute(LOG_ID, uuid);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        log.info("Before Interceptor Request [{}][{}][{}]", timestamp, uuid, requestURI);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = (String) request.getAttribute(LOG_ID);
        int status = response.getStatus();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        log.info("After Interceptor Request [{}][{}][{}][{}]", timestamp, uuid, status, requestURI);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = (String) request.getAttribute(LOG_ID);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        if(ex != null) {
            log.info("Complete Interceptor Request [{}][{}][{}][{}]", timestamp, uuid, requestURI, handler);
        }
    }
}
