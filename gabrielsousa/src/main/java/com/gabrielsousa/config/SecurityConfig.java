package com.gabrielsousa.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.gabrielsousa.security.JWTAuthenticationFilter;
import com.gabrielsousa.security.JWTAuthorizationFilter;
import com.gabrielsousa.security.JWTUtil;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private JWTUtil jWTUtil;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationConfiguration authenticationConfiguration;

	private static final String[] PUBLIC_MATCHERS = { 
			"/h2-console/**",
			"/requests/**",
			"/clients/picture",
	};

	private static final String[] PUBLIC_MATCHERS_GET = {
			"/products/**",
			"/categories/**", 
			"/states/**"
	};

	private static final String[] PUBLIC_MATCHERS_POST = {
			"/clients/**",			
			"/auth/forgot/**"
			
	};
 
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
 
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
     
    	if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
    		http.headers().frameOptions().disable();
    	}
    	http.cors().and().csrf().disable();
        http.authorizeRequests()
        		.antMatchers(PUBLIC_MATCHERS).permitAll()
        		.antMatchers(HttpMethod.GET,PUBLIC_MATCHERS_GET).permitAll()
        		.antMatchers(HttpMethod.POST,PUBLIC_MATCHERS_POST).permitAll()
//                .antMatchers("/requests/**", "/products/**").hasAnyAuthority("Admin")
                //.hasAnyRole("Admin")
                //.hasAuthority("Admin")
//                .antMatchers("/requests/**").hasAnyAuthority("Admin")
//                .hasAnyAuthority("Admin", "Editor", "Salesperson")
//                .hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
                .anyRequest().authenticated()
//                .and().formLogin()
//                .loginPage("/login")
//                    .usernameParameter("email")
//                    .permitAll()
//                .and()
//                .rememberMe().key("AbcdEfghIjklmNopQrsTuvXyz_0123456789")
//                .and()
//                .logout().permitAll()
                ;
        http.addFilter(new JWTAuthenticationFilter(authenticationManager(authenticationConfiguration), jWTUtil));
        //TODO-Aten????o JWT n??o est?? funcionando
//        http.addFilterBefore(new JWTAuthenticationFilter(jWTUtil), UsernamePasswordAuthenticationFilter.class);
        http.addFilter(new JWTAuthorizationFilter(authenticationManager(authenticationConfiguration), jWTUtil, userDetailsService));
//        http.authenticationManager(new JWTAuthenticationFilter(authenticationManager(null),jWTUtil));
        http.headers().frameOptions().sameOrigin();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
       
//        http.apply(customDsl());
        
        return http.build();
    }
    
	//TODO-forma nova
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
    }
	
//	@Bean
//    public EmbeddedLdapServerContextSourceFactoryBean contextSourceFactoryBean() {
//        EmbeddedLdapServerContextSourceFactoryBean contextSourceFactoryBean =
//            EmbeddedLdapServerContextSourceFactoryBean.fromEmbeddedLdapServer();
//        contextSourceFactoryBean.setPort(0);
//        return contextSourceFactoryBean;
//    }
//
//    @Bean
//    AuthenticationManager ldapAuthenticationManager(
//            BaseLdapPathContextSource contextSource) {
//        LdapBindAuthenticationManagerFactory factory = 
//            new LdapBindAuthenticationManagerFactory(contextSource);
//        factory.setUserDnPatterns("uid={0},ou=people");
//        factory.setUserDetailsContextMapper(new PersonContextMapper());
//        return factory.createAuthenticationManager();
//    }
    
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        UserDetails user = User.withDefaultPasswordEncoder()
//            .username("user")
//            .password("password")
//            .roles("USER")
//            .build();
//        return new InMemoryUserDetailsManager(user);
//    }
    
//    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
//        @Override
//        public void configure(HttpSecurity http) throws Exception {
//            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
//            http.addFilter(new JWTAuthenticationFilter(authenticationManager, jWTUtil));
//        }
//
//        public static MyCustomDsl customDsl() {
//            return new MyCustomDsl();
//        }
//    }
	
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
    }
 
    @Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
    
    @Bean
    public WebMvcConfigurer corsConfigurer() 
    {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:8000");
                registry.addMapping("/**").allowedOrigins("http://localhost:4200");
            	registry.addMapping("/**").allowedOrigins("https://gabrielsousa-spring-ionic-back.herokuapp.com/");
            }
        };
    }
    
}
