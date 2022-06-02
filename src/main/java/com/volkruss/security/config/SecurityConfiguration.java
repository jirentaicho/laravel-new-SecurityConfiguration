package com.volkruss.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfiguration {

    @Autowired
    private DataSource dataSource;

    // ログイン後は/homeに遷移させる
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                // authorizeRequests
                .authorizeHttpRequests()
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().defaultSuccessUrl("/home");
        return http.build();
    }

    // 事前に用意したレコードでログインする
    // データベースに登録されているレコードのパスワードがハッシュ化されていること
    // また認可テーブルにも該当のレコードが存在すること
    @Bean
    public UserDetailsManager userDetailsService(){
        // 既存User : misaka/mikoto
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(this.dataSource);
        return users;
    }


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


    /*
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
     */
}
