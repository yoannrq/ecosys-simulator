package org.ecosyssimulator.view;

import org.ecosyssimulator.config.ThymeleafConfig;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

public class ViewRenderer {
    private final TemplateEngine templateEngine;

    public ViewRenderer() {
        this.templateEngine = ThymeleafConfig.getTemplateEngine();
    }

    public String render(String templateName, Object... keyValuePairs) {
        Context context = new Context();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            context.setVariable(keyValuePairs[i].toString(), keyValuePairs[i + 1]);
        }
        return templateEngine.process(templateName, context);
    }
}
