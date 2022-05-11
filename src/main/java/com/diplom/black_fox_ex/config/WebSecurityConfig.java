package com.diplom.black_fox_ex.config;
/**-----------------------------------------------------------------------------------**/
import com.diplom.black_fox_ex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
/**-----------------------------------------------------------------------------------**/
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;
    /**-----------------------------------------------------------------------------------**/
    @Value("${upload.path}")
    private String uploadPath;// Вытаскиваем путь к файлу
    /**-----------------------------------------------------------------------------------**/
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(uploadPath + "**");
    }
    /**-----------------------------------------------------------------------------------**/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()//Первый раз зашел
                .antMatchers("/", "/registration", "/registration/add", "/about", "/history/**/look", "/histories/**").permitAll()//К этой странице есть доступ при старте
                .anyRequest().authenticated()//Для остальных запросов требуем авторизацию
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()//разрешаем пользоваться всем для login
                .and()
                .logout()
                .permitAll();//разрешаем пользоваться всем для logout
    }
    /**-----------------------------------------------------------------------------------**/
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
}