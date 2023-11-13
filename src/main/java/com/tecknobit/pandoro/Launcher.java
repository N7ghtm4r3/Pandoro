package com.tecknobit.pandoro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

/**
 * The {@code Launcher} class is useful to launch <b>Pandoro's backend service</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@SpringBootApplication
@EnableJpaRepositories("com.tecknobit.pandoro.services.repositories")
public class Launcher {

    /**
     * {@code IMAGES_PATH} path for the images folder
     */
    public static final String IMAGES_PATH = "images/";

    /**
     * Main method to start the backend
     *
     * @param args: CREATE THE MENU
     */
    public static void main(String[] args) {
        // TODO: 09/11/2023 TO REMOVE, TESTING PURPOSE
        System.setProperty("spring.main.allow-bean-definition-overriding", "true");
        SpringApplication.run(Launcher.class, args);
    }

    /**
     * The {@code CORSAdvice} class is useful to set the CORS policy
     *
     * @author N7ghtm4r3 - Tecknobit
     */
    @Configuration
    public static class CORSAdvice {

        /**
         * Method to set the CORS filter <br>
         * No any-params required
         */
        @Bean
        public FilterRegistrationBean corsFilter() {
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowCredentials(false);
            config.addAllowedOrigin("*");
            config.addAllowedHeader("*");
            config.addAllowedMethod("*");
            source.registerCorsConfiguration("/**", config);
            FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
            bean.setOrder(0);
            return bean;
        }

    }

    /**
     * The {@code ResourceConfigs} class is useful to set the configuration of the resources to correctly serve the
     * images by the server
     *
     * @author N7ghtm4r3 - Tecknobit
     * @see WebMvcConfigurer
     */
    @Configuration
    public class ResourcesConfigs implements WebMvcConfigurer {

        /**
         * Add handlers to serve static resources such as images, js, and, css
         * files from specific locations under web application root, the classpath,
         * and others.
         *
         * @see ResourceHandlerRegistry
         */
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/**")
                    .addResourceLocations("file:" + IMAGES_PATH)
                    .setCachePeriod(0)
                    .resourceChain(true)
                    .addResolver(new PathResourceResolver());
        }

    }

}
