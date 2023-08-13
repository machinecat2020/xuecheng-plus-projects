package com.xuecheng.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Mr.M
 * @version 1.0
 * @description 安全管理配置
 * @date 2022/9/26 20:53
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    /*
    *
    * 因为我们可能需要在其他地方引用AuthenticationManager。
    * 通过在配置类中使用@Bean注解，Spring将会将该方法返回的AuthenticationManager实例纳入到应用程序上下文中，
    * 并使其在其他地方可以通过依赖注入来使用。
    * */
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
// 通过调用父类的方法，可以获取到Spring Security已经创建和配置好的AuthenticationManager实例，然后将其注入到当前的配置类中。
        return super.authenticationManagerBean();
    }

    //配置用户信息服务
    /*
    * InMemoryUserDetailsManager 是 Spring Security 提供的一个简单的用户管理器实现，
    * 它将用户信息存储在内存中而不是持久化到数据库。这个类可以用于开发和测试阶段，
    * 或者对于只有少量用户需要进行身份验证和授权的简单应用程序。
    * */
//    @Bean
//    public UserDetailsService userDetailsService() {
//        //这里配置用户信息,这里暂时使用这种方式将用户存储在内存中
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withUsername("zhangsan").password("123").authorities("p1").build());
//        manager.createUser(User.withUsername("lisi").password("456").authorities("p2").build());
//        return manager;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
//        //密码为明文方式
//        return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

    @Autowired
    DaoAuthenticationProviderCustom daoAuthenticationProviderCustom;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProviderCustom);
    }

    //配置安全拦截机制
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()//看评论区自己加上的，不一定正确，需要后期测试
                .authorizeRequests()
                .antMatchers("/r/**").authenticated()//访问/r开始的请求需要认证通过
                .anyRequest().permitAll()//其它请求全部放行
                .and()
                .formLogin().successForwardUrl("/login-success");//登录成功跳转到/login-success
    }

// 验证BCryptPasswordEncoder每次生产的密码都不一样，没什么用，应该可以删除
    public static void main(String[] args) {
        String password = "111111";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        for (int i = 0; i < 5; i++) {
            //生成密码
            String encode = passwordEncoder.encode(password);
            System.out.println(encode);
            //校验密码,参数1是输入的明文 ，参数2是正确密码加密后的串
            boolean matches = passwordEncoder.matches(password, encode);
            System.out.println(matches);
        }

        boolean matches = passwordEncoder.matches("1234", "$2a$10$fb2RlvFwr9HsRu9vH1OxCu/YiMRw6wy5UI6u3s0A.0bVSuR1UqdHK");
        System.out.println(matches);
    }



}
