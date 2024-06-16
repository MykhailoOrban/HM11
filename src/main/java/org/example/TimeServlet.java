package org.example;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.ServerException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@WebServlet("/time")
public class TimeServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServerException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");

        String timezoneParam = req.getParameter("timezone");
         TimeZone timeZone;
         if(timezoneParam != null && !timezoneParam.isEmpty()){
             timeZone = TimeZone.getTimeZone(timezoneParam);
         }
         else{
             timeZone = TimeZone.getTimeZone("UTC");
         }

        Date currentTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        sdf.setTimeZone(timeZone);
        String currentTimeUTC = sdf.format(currentTime);

        PrintWriter out = resp.getWriter();
        out.println("<html><head><title>Current Time (UTC)</title></head><body>");
        out.println("<h1>Current Time (UTC)</h1>");
        out.println("<p>" + currentTimeUTC + "</p>");
        out.println("</body></html>");

    }
}
