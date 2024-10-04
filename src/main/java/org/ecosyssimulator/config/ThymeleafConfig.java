package org.ecosyssimulator.config;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * Configuration class for Thymeleaf template engine.
 */
public class ThymeleafConfig {

    /**
     * Creates and configures a Thymeleaf TemplateEngine.
     *
     * @return A configured TemplateEngine instance.
     */
    public static TemplateEngine getTemplateEngine() {
        // Create a new instance of TemplateEngine
        TemplateEngine templateEngine = new TemplateEngine();

        // Create a ClassLoaderTemplateResolver to load templates from the classpath
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

        // Set the prefix for template names
        // This means templates will be looked for in the "templates/" directory
        templateResolver.setPrefix("templates/");

        // Set the suffix for template files
        // This means we'll be looking for .html files
        templateResolver.setSuffix(".html");

        // Set the template mode to HTML
        templateResolver.setTemplateMode("HTML");

        // Set the character encoding to UTF-8
        templateResolver.setCharacterEncoding("UTF-8");

        // Set the configured templateResolver for the templateEngine
        templateEngine.setTemplateResolver(templateResolver);

        // Return the fully configured TemplateEngine
        return templateEngine;
    }
}
