package listeners;

import common.Constantes;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebApplication;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;


@WebListener
public class ThymeLeafListener implements ServletContextListener {

    private ITemplateEngine templateEngine;
    private JakartaServletWebApplication application;

    public ThymeLeafListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.application = JakartaServletWebApplication.buildApplication(sce.getServletContext());
        this.templateEngine = templateEngine(this.application);

        sce.getServletContext().setAttribute(Constantes.TEMPLATE_ENGINE_ATTR, templateEngine);

    }

    private ITemplateEngine templateEngine(IWebApplication application) {
        TemplateEngine templateEngine = new TemplateEngine();

        WebApplicationTemplateResolver templateResolver = templateResolver(application);
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine;
    }

    private WebApplicationTemplateResolver templateResolver(IWebApplication application) {
        WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(application);

        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix(Constantes.WEB_INF_TEMPLATES);
        templateResolver.setSuffix(Constantes.EXTENSION_HTML);
        templateResolver.setCacheTTLMs(3600000L);
        templateResolver.setCacheable(true);
        return templateResolver;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // NOP
    }
}

