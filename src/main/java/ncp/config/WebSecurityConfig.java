package ncp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource  dataSource;
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/administration/**", "/address/**", "/transmitter/**")
                        .hasAnyAuthority("3")
                    .antMatchers("/contract/**")
                        .hasAnyAuthority("1", "3")
                    .antMatchers("/tariff/**")
                        .hasAnyAuthority("2", "3")
                    .antMatchers("/profile/**")
                        .fullyAuthenticated()
                    .antMatchers("/", "/home", "/sign_up")
                        .permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/sign_in")
                    .permitAll()
                .and()
                    .logout()
                    .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(encoder())
                .usersByUsernameQuery("select username, password, 'true' from user where username=?")
                .authoritiesByUsernameQuery("select u.username, ur.role_set_id from user u inner join user_role_set ur on " +
                        "u.id = ur.user_id where u.username=?");
    }
}
