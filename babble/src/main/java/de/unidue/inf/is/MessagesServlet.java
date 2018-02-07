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

public class MessagesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static String loggedInUserName, userId = null;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//		response.getWriter().append("Served at: ").append(request.getContextPath());
		try {
			loggedInUserName = request.getSession().getAttribute("loggedInUserName").toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			request.setAttribute("pagetitle", "- Not Logged In");
			request.getRequestDispatcher("notLoggedIn.ftl").forward(request, response);
		}
		
		try {
			userId = request.getParameter("id");
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			request.setAttribute("pagetitle", "- Not Logged In");
			System.out.println("No Id");
		}
		request.setAttribute("pagetitle", "- Conversation with @" + userId);
		ArrayList<Babble> babbles;
		babbles = getBabblesMessages();
		if(babbles != null)
		{
			request.setAttribute("success", true);
			request.setAttribute("resultBabbleMessages", babbles);
		}
		else
		{
			request.setAttribute("success", false);
			request.setAttribute("message", "Noch keine Konversation vorhanden");
		}
			
		try {
			request.getRequestDispatcher("/messages.ftl").forward(
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
		
		if(checkFriendShip())
		{
			try {
				PreparedStatement ps = con.prepareStatement("INSERT INTO dbp51.BabbleMessage (text, sender, recipient) values (?, ?, ?)");
				ps.setString(1, request.getParameter("babbleMessage"));
				ps.setString(2, loggedInUserName);
				ps.setString(3, userId);
				int row = ps.executeUpdate();
				if (row > 0)
				{
					request.setAttribute("success", true);
					request.setAttribute("message", "Message Babble sent");
//					request.getRequestDispatcher("createBabbleOk.ftl").forward(request, response);
				}
				else
				{
					request.setAttribute("success", false);
					request.setAttribute("pagetitle", "- An error has occured!");
					request.setAttribute("message", "Cannot create this babble message!");
//					request.getRequestDispatcher("createBabbleFail.ftl").forward(request, response);
				}
			
		} catch (Exception e) {
			request.setAttribute("success", false);
			request.setAttribute("pagetitle", "- Unauthorized!");
			request.setAttribute("message", "An unknown error occured.");
			request.getRequestDispatcher("/messages.ftl").forward(request, response);
			e.printStackTrace();
		}
		} else {
			request.setAttribute("success", false);
			request.setAttribute("message", "You are not allowed to send messages to @" + userId+"! \nYour're not friends (following each other).");
		}
		
		
		request.setAttribute("pagetitle", "- Conversation with @" + userId);		
		request.setAttribute("resultBabbleMessages", getBabblesMessages());
		try {
			request.getRequestDispatcher("/messages.ftl").forward(
					request, response);
		} catch (ServletException | IOException e) {
			//			request.setAttribute("errormessage",
			//					"Template error: please contact the administrator");
			e.printStackTrace();
		}
	}
	
	private ArrayList<Babble> getBabblesMessages ()
	{
		ArrayList<Babble> babbles = new ArrayList<>();
		Connection con = null;
		try {
			con = DBUtil.getExternalConnection("babble");
			try (PreparedStatement ps = con.prepareStatement("SELECT * FROM dbp51.BabbleMessage WHERE (sender = ? AND recipient = ?) OR (sender = ? AND recipient = ?)")) {
				ps.setString(1, loggedInUserName);
				ps.setString(2, userId);
				ps.setString(3, userId);
				ps.setString(4, loggedInUserName);
				try (ResultSet rs = ps.executeQuery()) {
					if(rs.next())
					{
						babbles.add(new Babble(rs.getInt(1), rs.getString(2), rs.getTimestamp(3), rs.getString(4),
								BabbleAssists.getNameFromUserName(con, rs.getString(4))));
						while (rs.next()) {
							babbles.add(new Babble(rs.getInt(1), rs.getString(2), rs.getTimestamp(3), rs.getString(4),
									BabbleAssists.getNameFromUserName(con, rs.getString(4))));
						}
					} else
						return null;					
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (con != null) try { con.close(); } catch (SQLException ignore) {}
		}
		return BabbleAssists.sortBabbles(babbles);
	}
	
	private Boolean checkFriendShip ()
	{
		Connection con = null;
		try {
			con = DBUtil.getExternalConnection("babble");
			try (PreparedStatement ps = con.prepareStatement("SELECT * FROM dbp51.Follows WHERE (follower = ? AND followee = ?) AND (follower = ? AND followee = ?)")) {
				ps.setString(1, loggedInUserName);
				ps.setString(2, userId);
				ps.setString(3, userId);
				ps.setString(4, loggedInUserName);
				try (ResultSet rs = ps.executeQuery()) {
					if(rs.next())
					{
						return true;
					} else
						return false;					
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (con != null) try { con.close(); } catch (SQLException ignore) {}
		}
		return false;
	}
}
