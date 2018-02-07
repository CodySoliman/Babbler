package de.unidue.inf.is;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




/**
 * Einfaches Beispiel, das die Vewendung der Template-Engine zeigt.
 */
public final class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Put the user list in request and let freemarker paint it.
    	request.setAttribute("pagetitle", "- Login");
    	try {
			request.getRequestDispatcher("/login.ftl").forward(
					request, response);
		} catch (ServletException | IOException e) {
			//			request.setAttribute("errormessage",
			//					"Template error: please contact the administrator");
			e.printStackTrace();
		}
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                    IOException {
    	String loggedInUser = request.getParameter("login-username").toString();
    	String siteURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/";
    	request.getSession().setAttribute("loggedInUserName", loggedInUser);
        response.sendRedirect(siteURL + "user?id=" + loggedInUser);
    }
}
