package com.volkruss.security.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RedirectLoginUserFilter extends OncePerRequestFilter {

    private final String TARGET_PATH = "/login";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("RedirectLoginUserFilterのdoFilterが呼ばれました");
        //　認証済でない場合は処理を抜ける
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            filterChain.doFilter(request,response);
            return;
        }
        AntPathRequestMatcher antPathRequestMatcher =
                new AntPathRequestMatcher(TARGET_PATH, HttpMethod.GET.name());
        // "/login"かつGETでない場合は処理を抜ける
        if(!antPathRequestMatcher.matches(request)){
            filterChain.doFilter(request,response);
            return;
        }
        // リダイレクトする
        response.sendRedirect("/home");
        return;
    }
}
