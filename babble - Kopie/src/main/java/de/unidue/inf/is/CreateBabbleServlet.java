package de.unidue.inf.is;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.utils.DBUtil;

/**
 * Servlet implementation class createBabble
 */

public class CreateBabbleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//		response.getWriter().append("Served at: ").append(request.getContextPath());
		try {
			request.getSession().getAttribute("loggedInUserName").toString();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			request.setAttribute("pagetitle", "- Not Logged In");
			request.getRequestDispatcher("notLoggedIn.ftl").forward(request, response);
		}
		request.setAttribute("pagetitle", "- Write your Babble");
		try {
			request.getRequestDispatcher("/createBabble.ftl").forward(
					request, response);
		} catch (ServletException | IOException e) {
			//			request.setAttribute("errormessage",
			//					"Template error: please contact the administrator");
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Connection con = null;
		try {
			con = DBUtil.getExternalConnection("babble");
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			if(request.getParameter("babble").length()<=280)
			{
				PreparedStatement ps = con.prepareStatement("INSERT INTO dbp51.Babble (text, creator) values (?, ?)");
				ps.setString(1, request.getParameter("babble"));
				ps.setString(2, request.getSession().getAttribute("loggedInUserName").toString());
				int row = ps.executeUpdate();
				if (row > 0)
				{
					request.setAttribute("pagetitle", "Babble has been created!");
					request.getRequestDispatcher("createBabbleOk.ftl").forward(request, response);
				}
				else
				{
					request.setAttribute("pagetitle", "An error has occured!");
					request.setAttribute("message", "Cannot create this babble!");
					request.getRequestDispatcher("createBabbleFail.ftl").forward(request, response);
				}
			} else
			{
				request.setAttribute("pagetitle", "An error has occured!");
				request.setAttribute("message", "Your message exceeds 280 character.");
				request.getRequestDispatcher("createBabbleFail.ftl").forward(request, response);
			}				
		} catch (Exception e) {
			request.setAttribute("pagetitle", "Unauthorized!");
			request.setAttribute("message", "An unknown error occured.");
			request.getRequestDispatcher("createBabbleFail.ftl").forward(request, response);
			e.printStackTrace();
		}
	}
}
