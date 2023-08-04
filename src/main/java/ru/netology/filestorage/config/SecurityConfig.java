package ru.netology.filestorage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.security.SecureRandom;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final DataSource dataSource;

    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select login, password, 'true' from users where login = ?")
                .authoritiesByUsernameQuery("select u.login, r.name from users u join role r on r.id = u.role_id where login = ?");
                //.rolePrefix("ROLE_");
    }

    protected void configure(HttpSecurity http) throws Exception {
        // and - Объединяем настройки
        // authorizeRequests() - какие запросы надо авторизовывать
        // antMatchers() - для каких путей будет срабатывать та или иная настройка безопасности
        // метод hi доступен без регистрации
        // hasAuthority() - чтобы получить доступ к методу read, должно быть разрешение read
        // .anyRequest().authenticated() - все остальные запросы, которые остались, должны быть аутентифицированы

        http.formLogin()
                .and()
                .authorizeRequests().antMatchers("/login").permitAll()
                .and()
                .authorizeRequests().anyRequest().authenticated();
       /* http.formLogin()
                .and()
                .authorizeRequests().antMatchers("/hi").permitAll()
                .and()
                .authorizeRequests().antMatchers("/read").hasAuthority("read")
                .and()
                .authorizeRequests().antMatchers("/write").hasAuthority("write")
                .and()
                .authorizeRequests().anyRequest().authenticated();*/
    }

    // сконфигурировать пользователей нашего приложения
    // сейчас юзеров нет, Спринг секурити создаст одного единственного юзера, с помощью которого можно получить доступ
    // логин по умолчанию user и пароль авто сгенерен

    // открываем страницу в инкогнито
    // метод hi доступен без регистрации
    // дальше идем на страничку hello - здесь уже форма login и нам предлагают аутентифицироваться
    // есть еще logout
    // если юзер пытается получить доступ к методу read или write, то получим ошибку 403
    // 401 - я не знаю, кто ты
    // 403 - я тебя знаю, но тебе сюда нельзя

    /* Генерация дефолтных юзеров
     * application.properties
     * spring.security.user.name = login
     * spring.security.user.password = qwerty
     *
     * jdbc - хранение в бд
     * ldap - сторонний сервер авторизации
     *
     *
     * Пользователи хранятся в коде, на старте приложения они загружаются в память
     * */

    /*protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("Grigorii").password("{noop}password").authorities("read", "write")
                .and()
                .withUser("Ivan").password("{noop}password2").authorities("read")
                .and()
                .withUser("Vasily").password("{noop}password3").authorities("write");
    }*/


    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(12, new SecureRandom());
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}


