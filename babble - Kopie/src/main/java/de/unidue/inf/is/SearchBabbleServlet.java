package de.unidue.inf.is;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.Babble;
import de.unidue.inf.is.utils.BabbleAssists;
import de.unidue.inf.is.utils.DBUtil;

/**
 * Servlet implementation class createBabble
 */

public class SearchBabbleServlet extends HttpServlet {
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
		
		request.setAttribute("pagetitle", "- Search Babbler");
		try {
			request.getRequestDispatcher("/searchBabble.ftl").forward(
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
		request.setAttribute("pagetitle", "- Search Results");
		ArrayList<Babble> babbles = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement("SELECT * FROM dbp51.Babble WHERE text LIKE ?")) {
			ps.setString(1, "%" + request.getParameter("searchText") + "%");
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					babbles.add(new Babble(rs.getInt(1), rs.getString(2), rs.getTimestamp(3), rs.getString(4),
							BabbleAssists.countInteractionsPerType(con, rs.getInt(1), "like", "LikesBabble"),
							BabbleAssists.countInteractionsPerType(con, rs.getInt(1), "dislike", "LikesBabble"),
							BabbleAssists.countInteractionsPerType(con, rs.getInt(1), "rebabble", "Rebabble"),
							BabbleAssists.getNameFromUserName(con, rs.getString(4))));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch(Exception e) {
			request.setAttribute("pagetitle", "Unauthorized!");
			e.printStackTrace();
		}
		request.setAttribute("searchText", request.getParameter("searchText"));
		request.setAttribute("resultBabbles", babbles);
		try {
			request.getRequestDispatcher("/searchBabble.ftl").forward(
					request, response);
		} catch (ServletException | IOException e) {
			//			request.setAttribute("errormessage",
			//					"Template error: please contact the administrator");
			e.printStackTrace();
		}
	}
}
