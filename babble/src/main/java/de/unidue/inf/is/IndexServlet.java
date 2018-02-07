package de.unidue.inf.is;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




/**
 * Einfaches Beispiel, das die Vewendung der Template-Engine zeigt.
 */
public final class IndexServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static String loggedInUser;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Put the user list in request and let freemarker paint it.
    	try {
    		loggedInUser = request.getSession().getAttribute("loggedInUserName").toString();
    		StringBuffer requestURL = request.getRequestURL();
        	if (request.getQueryString() != null) {
        	    requestURL.append("?").append(request.getQueryString());
        	}
        	String siteURL = requestURL.toString();
            response.sendRedirect(siteURL + "user?id=" + loggedInUser);
		} catch (Exception e) {
			try {
				StringBuffer requestURL = request.getRequestURL();
	        	if (request.getQueryString() != null) {
	        	    requestURL.append("?").append(request.getQueryString());
	        	}
	        	String siteURL = requestURL.toString();
	            response.sendRedirect(siteURL + "login");
			} catch (Exception e1) {
				//			request.setAttribute("errormessage",
				//					"Template error: please contact the administrator");
				e1.printStackTrace();
			}
		}
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                    IOException {
    }
}
