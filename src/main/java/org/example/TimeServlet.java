package org.example;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@WebServlet("/time")
public class TimeServlet extends HttpServlet {

    private TemplateEngine templateEngine;

    @Override
    public void init() {
        initializeTemplateEngine(getServletContext());
    }

    private void initializeTemplateEngine(javax.servlet.ServletContext servletContext) {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");


        String timezoneParam = req.getParameter("timezone");


        String lastTimezone = getLastTimezoneFromCookie(req);


        TimeZone timeZone;
        if (timezoneParam != null && !timezoneParam.isEmpty()) {
            timeZone = TimeZone.getTimeZone(timezoneParam);
            // Зберігаємо часовий пояс у Cookie
            saveLastTimezoneToCookie(resp, timezoneParam);
        } else if (lastTimezone != null && !lastTimezone.isEmpty()) {
            timeZone = TimeZone.getTimeZone(lastTimezone);
        } else {
            timeZone = TimeZone.getTimeZone("UTC");
        }


        Date currentTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        sdf.setTimeZone(timeZone);
        String currentTimeFormatted = sdf.format(currentTime);


        WebContext context = new WebContext(req, resp, getServletContext());
        context.setVariable("currentTime", currentTimeFormatted);
        context.setVariable("timezone", timeZone.getID());


        templateEngine.process("time-template", context, resp.getWriter());
    }


    private String getLastTimezoneFromCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("lastTimezone")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


    private void saveLastTimezoneToCookie(HttpServletResponse resp, String timezone) {
        Cookie cookie = new Cookie("lastTimezone", timezone);
        cookie.setMaxAge(24 * 60 * 60);
        resp.addCookie(cookie);
    }
}