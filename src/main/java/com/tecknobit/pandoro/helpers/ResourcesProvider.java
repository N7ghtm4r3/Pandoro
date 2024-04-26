package com.tecknobit.pandoro.helpers;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.File;

/**
 * The {@code ResourcesProvider} class is useful to create the resources directories and manage the main resources files
 * such as the properties for the server
 *
 * @author N7ghtm4r3 - Tecknobit
 */
public class ResourcesProvider {

    /**
     * {@code DEFAULT_CONFIGURATION_FILE_PATH} the default path where find the default server configuration
     */
    public static final String DEFAULT_CONFIGURATION_FILE_PATH = "app.properties";

    /**
     * {@code CUSTOM_CONFIGURATION_FILE_PATH} the path of the custom server configuration file
     *
     * @apiNote to use your custom configuration <b>you must save the file in the same folder where you placed the
     * server file (.jar) and call it "pandoro.properties"</b>
     * @implSpec take a look <a href="https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html">here</a>
     * to get more information about the custom configuration for properties that you can use
     */
    public static final String CUSTOM_CONFIGURATION_FILE_PATH = "pandoro.properties";

    /**
     * {@code IMAGES_PATH} path for the images folder
     */
    public static final String IMAGES_PATH = "images/";

    /**
     * {@code RESOURCES_DIRECTORIES} the list of the resources directories
     */
    public static final String[] RESOURCES_DIRECTORIES = {"profiles"};

    /**
     * {@code PROFILES_DIRECTORY} the profiles directory where are stored the pics of the users
     */
    public static final String PROFILES_DIRECTORY = RESOURCES_DIRECTORIES[0];

    /**
     * {@code PROFILE_PICS_FOLDER} the path of the folder where store the profile pics
     */
    public static final String PROFILE_PICS_PATH = IMAGES_PATH + PROFILES_DIRECTORY + "/";

    /**
     * Constructor to init the {@link ResourcesProvider} controller <br>
     * No-any params required
     */
    private ResourcesProvider() {
    }

    /**
     * Method to create all the resources directories <br>
     * No-any params required
     */
    public static void createResourceDirectories() {
        createResourceDirectory(IMAGES_PATH);
        for (String directory : RESOURCES_DIRECTORIES)
            createResourceDirectory(IMAGES_PATH + directory);
    }

    /**
     * Method to create a specific resources directory from {@link #RESOURCES_DIRECTORIES} list
     *
     * @param resDirectory: the specific resources directory to create
     */
    private static void createResourceDirectory(String resDirectory) {
        File directory = new File(resDirectory);
        if (!directory.exists())
            if (!directory.mkdir())
                printError(resDirectory.replaceAll("/", ""));
    }

    /**
     * Method to print the error occurred during the creation of a resources directory
     *
     * @param directory: the directory when, during the creation, occurred an error
     */
    private static void printError(String directory) {
        System.err.println("Error during the creation of the \"" + directory + "\" folder");
        System.exit(-1);
    }

    /**
     * The {@code ResourceConfigs} class is useful to set the configuration of the resources to correctly serve the
     * images by the server
     *
     * @author N7ghtm4r3 - Tecknobit
     * @see WebMvcConfigurer
     */
    @Configuration
    public static class ResourcesConfigs implements WebMvcConfigurer {

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