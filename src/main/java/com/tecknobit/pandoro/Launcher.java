package com.tecknobit.pandoro;

import com.tecknobit.apimanager.apis.ServerProtector;
import com.tecknobit.apimanager.exceptions.SaveData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.security.NoSuchAlgorithmException;

import static com.tecknobit.pandoro.helpers.ResourcesProvider.*;

/**
 * The {@code Launcher} class is useful to launch <b>Pandoro's backend service</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@SpringBootApplication
@PropertySources({
        @PropertySource(value = "classpath:" + DEFAULT_CONFIGURATION_FILE_PATH),
        @PropertySource(value = "file:" + CUSTOM_CONFIGURATION_FILE_PATH, ignoreResourceNotFound = true)
})
@EnableJpaRepositories("com.tecknobit.*")
@EntityScan("com.tecknobit.*")
public class Launcher {

    /**
     * {@code protector} the instance to launch the server protector to manage the server accesses
     *
     * @apiNote the commands scheme:
     * <ul>
     *     <li>
     *         <b>rss</b> -> launch your java application with "rss" to recreate the server secret <br>
     *                       e.g java -jar Pandoro.jar rss
     *     </li>
     *     <li>
     *         <b>dss</b> -> launch your java application with "dss" to delete the current server secret <br>
     *                       e.g java -jar Pandoro.jar dss
     *     </li>
     *     <li>
     *         <b>dssi</b> -> launch your java application with "dssi" to delete the current server secret and interrupt
     *                        the current workflow of the server <br>
     *                        e.g java -jar Pandoro.jar dssi
     *     </li>
     * </ul>
     */
    public static final ServerProtector protector = new ServerProtector("tecknobit/pandoro/backend",
            " to correctly register a new user in the Pandoro system ");

    /**
     * Main method to start the backend
     *
     * @param args: custom arguments to share with {@link SpringApplication} and with the {@link #protector}
     * @apiNote the arguments scheme:
     * <ul>
     *     <li>
     *         {@link #protector} ->
     *         <ul>
     *          <li>
     *             <b>rss</b> -> launch your java application with "rss" to recreate the server secret <br>
     *                       e.g java -jar Pandoro.jar rss
     *             </li>
     *              <li>
     *                  <b>dss</b> -> launch your java application with "dss" to delete the current server secret <br>
     *                       e.g java -jar Pandoro.jar dss
     *              </li>
     *              <li>
     *                  <b>dssi</b> -> launch your java application with "dssi" to delete the current server secret and interrupt
     *                        the current workflow of the server <br>
     *                        e.g java -jar Pandoro.jar dssi
     *              </li>
     *          </ul>
     *     </li>
     *     <li>
     *         {@link SpringApplication} -> see the allowed arguments <a href="https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html">here</a>
     *     </li>
     * </ul>
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, SaveData {
        createResourceDirectories();
        protector.launch(args);
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

}
