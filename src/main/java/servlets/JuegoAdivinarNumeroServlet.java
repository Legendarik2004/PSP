package servlets;

import common.Constantes;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.Random;

//@Log4j2

@WebServlet(name = Constantes.JUEGO, value = {Constantes.JUEGO_ADIVINAR_NUMERO})
public class JuegoAdivinarNumeroServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(Constantes.TEMPLATE_ENGINE_ATTR);
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext context = new WebContext(webExchange);

        String template;

        String numParameter = req.getParameter(Constantes.NUM);
        Integer randomNum = (Integer) req.getSession().getAttribute(Constantes.RANDOM_NUM);
        Integer contador = (Integer) req.getSession().getAttribute(Constantes.CONTADOR);

        if (randomNum == null) {
            randomNum = new Random().nextInt(10) + 1;
        }
        if (contador == null) {
            contador = 5;
        }
        if (numParameter != null && !numParameter.isEmpty()) {

            int numero = Integer.parseInt(numParameter);

            String mensajeIntentos;
            String mensajeRespuesta;

            if (contador <= 1) {
                contador = 5;
                randomNum = new Random().nextInt(10) + 1;
                mensajeRespuesta = Constantes.SIN_INTENTOS;
            } else {
                if (numero == randomNum) {
                    contador = 5;
                    randomNum = new Random().nextInt(10) + 1;
                    mensajeRespuesta = Constantes.ACIERTO;
                } else {
                    contador--;
                    if (numero > randomNum) {
                        mensajeRespuesta = Constantes.NUMERO_MUY_ALTO + randomNum;
                    } else {
                        mensajeRespuesta = Constantes.NUMERO_MUY_BAJO + randomNum;
                    }
                    mensajeIntentos =  contador + Constantes.INTENTOS_RESTANTES;
                    context.setVariable(Constantes.INTENTOS, mensajeIntentos);
                }
            }
            context.setVariable(Constantes.RESPUESTA, mensajeRespuesta);

            req.getSession().setAttribute(Constantes.CONTADOR, contador);
            req.getSession().setAttribute(Constantes.RANDOM_NUM, randomNum);
            template = Constantes.PARAM;

        } else {
            template = Constantes.ERROR;
            context.setVariable(Constantes.ERROR, Constantes.CASILLA_NUM_VACIA);
        }

        templateEngine.process(template, context, resp.getWriter());
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }


}
