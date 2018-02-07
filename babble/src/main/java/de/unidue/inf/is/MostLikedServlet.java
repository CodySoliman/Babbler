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

public class MostLikedServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
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
		Connection con = null;
		try {
			con = DBUtil.getExternalConnection("babble");
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		request.setAttribute("pagetitle", "- Search Results");
		ArrayList<Babble> babbles = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement("SELECT * FROM dbp51.babble b INNER JOIN (SELECT babble, count(*) as likes_count FROM dbp51.likesBabble WHERE type='like' group by babble FETCH FIRST 5 ROWS ONLY) top5 ON b.id = top5.babble ORDER BY top5.likes_count DESC")) {
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
		request.setAttribute("resultBabbles", babbles);
		try {
			request.getRequestDispatcher("/mostLiked.ftl").forward(
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
	}
}
