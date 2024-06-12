package me.jhchoi.ontrack.config;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.spring6.view.ThymeleafView;

import java.util.Locale;

//@Configuration
 @Slf4j
public class WebConfig implements WebMvcConfigurer {

    // 참고: https://www.baeldung.com/spring-boot-internationalization
    @Bean
    public LocaleResolver localeResolver(){
        // https://stackoverflow.com/questions/38803656/spring-thymeleaf-changing-locale-and-stay-on-the-current-page
//        Locale defaultLocale = new Locale("ko");
//        CookieLocaleResolver clr = new CookieLocaleResolver();
//        clr.setDefaultLocale(defaultLocale);
//        return clr;
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.KOREA);
        log.info("이게 불려지나? === localeResolver"); // 안 불려짐
        return localeResolver;
    }

    // https://stackoverflow.com/questions/78330023/springboot-thymeleaf-set-locale
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor(){
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        log.info("이게 불려지나? === localeChangeInterceptor"); // 안 불려짐
        return lci;
    }

}
