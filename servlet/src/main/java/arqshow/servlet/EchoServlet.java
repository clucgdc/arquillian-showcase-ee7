package arqshow.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(EchoServlet.URL_PATTERN)
public class EchoServlet extends HttpServlet {

    public static final String URL_PATTERN = "/echo";

    public static final String MESSAGE_PARAM = "message";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append(request.getParameter(MESSAGE_PARAM));
    }
}
