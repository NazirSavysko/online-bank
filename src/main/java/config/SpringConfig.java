package config;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;


@Configuration
@ComponentScan(basePackages = {"controller", "dao.impl", "service.impl", "utils"})
@EnableWebMvc
public class SpringConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;

    @Autowired
    public SpringConfig(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        final SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();

        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");

        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();

        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);

        return templateEngine;
    }

    @Override
    public void configureViewResolvers(final @NotNull ViewResolverRegistry registry) {
        final ThymeleafViewResolver resolver = new ThymeleafViewResolver();

        resolver.setTemplateEngine(templateEngine());
        registry.viewResolver(resolver);
    }

    @Override
    public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/css/**")
                .addResourceLocations("/css/");
    }


}