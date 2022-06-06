package com.volkruss.security.config;

// 不要なインポートがありますので注意
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.access.intercept.RequestMatcherDelegatingAuthorizationManager;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.sql.DataSource;

@Configuration
public class SecurityConfiguration {
    @Autowired
    private DataSource dataSource;
    // ログイン後は/homeに遷移させる
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers(header -> {
            header.frameOptions().disable();
        });
        http.authorizeHttpRequests(authorize -> {
            authorize.antMatchers("/h2-console/**").permitAll()
                    .anyRequest().authenticated();
        });
        http.formLogin(form -> {
            form.defaultSuccessUrl("/home");
        });
        // 追加
        http.addFilterAfter(new RedirectLoginUserFilter(),UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // 事前に用意したレコードでログインする
    // データベースに登録されているレコードのパスワードがハッシュ化されていること
    // また認可テーブルにも該当のレコードが存在すること
    /*
    @Bean
    public UserDetailsManager userDetailsService(){
        // 既存User : misaka/mikoto
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(this.dataSource);
        return users;
    }
     */


    // 以下はコメントアウトされていますが全て動きますので
    // 必要なケースに応じて利用できます。


    /*
    // JDBC認証
    @Bean
    public UserDetailsManager userDetailsService(){
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(this.dataSource);
        // テーブルusersとauthoritiesにshokuhou/misakiというレコードがROLE_USERで追加されます
        UserDetails user = User.withUsername("shokuhou")
                .password(
                        PasswordEncoderFactories
                                .createDelegatingPasswordEncoder()
                                .encode("mikoto"))
                .roles("USER")
                .build();
        users.createUser(user);
        return users;
    }
    */

    /*
    // 独自に定義したテーブルを使う
    // myusersテーブルとmyauthoritiesテーブルを利用してログインします
    // ルールは上と同じでパスワードがハッシュ化されていることと、認可にレコードがあること
    @Bean
    public UserDetailsManager userDetailsService(){

       String USERQUERY =
               "select username,password,enabled from myusers where username = ?";
       String AuthoritiesQuery =
               "select username,role from myauthorities where username = ?";

        JdbcUserDetailsManager users = new JdbcUserDetailsManager(this.dataSource);

        //  独自のテーブルやカラム名を使いたい時
        users.setUsersByUsernameQuery(USERQUERY);
        users.setAuthoritiesByUsernameQuery(AuthoritiesQuery);

        return users;
    }
     */


    // インメモリ認証
    @Bean
    public InMemoryUserDetailsManager userDetailsService(){
        UserDetails user = User.withUsername("misaka")
                .password(
                        PasswordEncoderFactories
                                .createDelegatingPasswordEncoder()
                                .encode("mikoto"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}