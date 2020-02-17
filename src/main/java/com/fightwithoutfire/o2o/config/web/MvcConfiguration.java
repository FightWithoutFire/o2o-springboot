package com.fightwithoutfire.o2o.config.web;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * @author xxx
 * @create 2020-02-15 14:36
 */
@Configuration
public class MvcConfiguration implements ApplicationContextAware, WebMvcConfigurer {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/");
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:D:/projectdev/image/upload/");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean(name = "viewResolver")
    public ViewResolver createViewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setApplicationContext(this.applicationContext);
        viewResolver.setCache(false);
        viewResolver.setPrefix("/WEB-INF/html/");
        viewResolver.setSuffix(".html");
        return viewResolver;
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver createCommonsMultipartResolver(){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("utf-8");
        multipartResolver.setMaxUploadSize(20971520);
        multipartResolver.setMaxInMemorySize(20971520);
        return multipartResolver;
    }

    @Value("${kaptcha.border}")
    private String border;

    @Value("${kaptcha.textproducer.font.color}")
    private String fcolor;

    @Value("${kaptcha.image.width}")
    private String width;

    @Value("${kaptcha.image.hight}")
    private String hight;

    @Value("${kaptcha.textproducer.char.string}")
    private String cString;

    @Value("${kaptcha.textproducer.front.size}")
    private String fszie;

    @Value("${kaptcha.noise.color}")
    private String nColor;

    @Value("${kaptcha.textproducer.char.length}")
    private String clength;

    @Value("${kaptcha.textproducer.font.names}")
    private String fnames;

    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        ServletRegistrationBean servlet = new ServletRegistrationBean(new KaptchaServlet(), "/Kaptcha");
        servlet.addInitParameter("kaptcha.border", border);
        servlet.addInitParameter("kaptcha.textproducer.font.color", fcolor);
        servlet.addInitParameter("kaptcha.image.width", width);
        servlet.addInitParameter("kaptcha.image.hight", hight);
        servlet.addInitParameter("kaptcha.textproducer.char.string", cString);
        servlet.addInitParameter("kaptcha.textproducer.front.size", fszie);
        servlet.addInitParameter("kaptcha.noise.color", nColor);
        servlet.addInitParameter("kaptcha.textproducer.char.length", clength);
        servlet.addInitParameter("kaptcha.textproducer.font.names", fnames);
        return servlet;
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        String interceptPath = "/shopadmin/**";
//        InterceptorRegistration loginIR = registry.addInterceptor(new ShopLoginInterceptor());
//        loginIR.addPathPatterns(interceptPath);
//        InterceptorRegistration permissionIR = registry.addInterceptor(new ShopPermissionInterceptor());
//        permissionIR.addPathPatterns(interceptPath);
//        permissionIR.excludePathPatterns("/shopadmin/shoplist");
//        permissionIR.excludePathPatterns("/shopadmin/getshoplist");
//        permissionIR.excludePathPatterns("/shopadmin/registershop");
//        permissionIR.excludePathPatterns("/shopadmin/registershop");
//        permissionIR.excludePathPatterns("/shopadmin/shopoperation");
//        permissionIR.excludePathPatterns("/shopadmin/shopmanagement");
//        permissionIR.excludePathPatterns("/shopadmin/getshopmanagementinfo");
//    }
}
