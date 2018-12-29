package com.sample.security.jwt;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AllowCrossOriginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        httpServletResponse.setHeader("Access-Control-Allow-Origin"  ,"*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods" , "GET,PUT,OPTIONS,POST,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers" , "Origin, X-Requested-With, Content-Type, Accept, Authorization");

        if(Boolean.FALSE == httpServletRequest.getMethod().toUpperCase().equals("OPTIONS"))
        {
            chain.doFilter(request,response);
        }else
        {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Override
    public void destroy() {

    }
}
