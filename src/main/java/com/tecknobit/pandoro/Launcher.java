package com.tecknobit.pandoro;

import com.tecknobit.equinox.environment.controllers.EquinoxController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

import static com.tecknobit.equinox.resourcesutils.ResourcesProvider.CUSTOM_CONFIGURATION_FILE_PATH;
import static com.tecknobit.equinox.resourcesutils.ResourcesProvider.DEFAULT_CONFIGURATION_FILE_PATH;

/**
 * The {@code Launcher} class is useful to launch <b>Pandoro's backend service</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@EnableAutoConfiguration
@PropertySources({
        @PropertySource(value = "classpath:" + DEFAULT_CONFIGURATION_FILE_PATH),
        @PropertySource(value = "file:" + CUSTOM_CONFIGURATION_FILE_PATH, ignoreResourceNotFound = true)
})
@EnableJpaRepositories("com.tecknobit.*")
@EntityScan("com.tecknobit.*")
@ComponentScan("com.tecknobit.*")
@SpringBootApplication
public class Launcher {

    /**
     * Main method to start the backend
     *
     * @param args: custom arguments to share with {@link SpringApplication} and with the {@code EquinoxController.serverProtector}
     * @apiNote the arguments scheme:
     * <ul>
     *     <li>
     *         {@code EquinoxController.serverProtector} ->
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
    public static void main(String[] args) {
        EquinoxController.initEquinoxEnvironment(
                "tecknobit/pandoro/backend",
                " to correctly register a new user in the Pandoro system ",
                Launcher.class,
                args
        );
        SpringApplication.run(Launcher.class, args);
    }

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext
                .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        System.out.println("----------- CURRENT ENDPOINTS MAPPED -----------");
        map.forEach((key, value) -> System.out.println("| " + key + value));
        System.out.println("--------------------------------------------");
    }

}
