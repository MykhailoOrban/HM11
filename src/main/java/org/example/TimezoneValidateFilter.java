package org.example;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.TimeZone;

public class TimezoneValidateFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Перевіряємо наявність параметра timezone у запиті
        String timezoneParam = httpRequest.getParameter("timezone");

        if (timezoneParam != null && !timezoneParam.isEmpty()) {
            // Валідація часового поясу
            if (isValidTimezone(timezoneParam)) {
                // Продовжуємо ланцюг фільтрів та обробку запиту
                chain.doFilter(request, response);
            } else {
                // Некоректний часовий пояс - відправляємо відповідь з кодом 400
                httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                httpResponse.setContentType("text/html");
                httpResponse.setCharacterEncoding("UTF-8");

                PrintWriter out = httpResponse.getWriter();
                out.println("<html><head><title>Invalid Timezone</title></head><body>");
                out.println("<h1>Invalid Timezone</h1>");
                out.println("<p>Invalid timezone parameter: " + timezoneParam + "</p>");
                out.println("</body></html>");
            }
        } else {
            // Якщо параметр timezone відсутній, продовжуємо ланцюг фільтрів та обробку запиту
            chain.doFilter(request, response);
        }
    }

    private boolean isValidTimezone(String timezone) {
        try {
            TimeZone.getTimeZone(timezone);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
