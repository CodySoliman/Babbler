package de.unidue.inf.is;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import de.unidue.inf.is.domain.Babble;
import de.unidue.inf.is.utils.DBUtil;
import de.unidue.inf.is.utils.BabbleAssists;

/**
 * Einfaches Beispiel, das die Verwendung des {@link UserStore}s zeigt.
 */
public final class UserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	String loggedInUser = null;
	private static String id;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Connection con = null;
		try {
			loggedInUser = request.getSession().getAttribute("loggedInUserName").toString();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			request.setAttribute("pagetitle", "- Not Logged In");
			request.getRequestDispatcher("notLoggedIn.ftl").forward(request, response);
		}
		request.setAttribute("found", true);
		request.setAttribute("loggedInUser", loggedInUser);

		try {
			con = DBUtil.getExternalConnection("babble");

		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		PreparedStatement ps;
		try {
			String action = request.getParameter("action").toString();
			Integer rows = 0;
			ps = null;
			if(action.equals("follow") & !(loggedInUser.equals(id)))
			{
				try {
					ps = con.prepareStatement("INSERT INTO dbp51.follows (follower, followee) values(?, ?)");
					ps.setString(1, loggedInUser);
					ps.setString(2, id);
					rows = ps.executeUpdate();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					if (ps != null) try { ps.close(); } catch (SQLException ignore) {}
				}
			}
			else if(action.equals("unfollow") && !(loggedInUser.equals(id)))
			{
				try {
					ps = con.prepareStatement("DELETE FROM dbp51.follows WHERE follower = ? AND followee = ?");
					ps.setString(1, loggedInUser);
					ps.setString(2, id);
					rows = ps.executeUpdate();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					if (ps != null) try { ps.close(); } catch (SQLException ignore) {}
				}
			}
			else  if(action.equals("block") && !(loggedInUser.equals(id)))
			{
				try {
					ps = con.prepareStatement("INSERT INTO dbp51.blocks (blocker, blockee) values(?, ?)");
					ps.setString(1, loggedInUser);
					ps.setString(2, id);
					rows = ps.executeUpdate();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					if (ps != null) try { ps.close(); } catch (SQLException ignore) {}
				}
			}
			else if(action.equals("unblock") && !(loggedInUser.equals(id)))
			{
				try {
					ps = con.prepareStatement("DELETE FROM dbp51.blocks WHERE blocker = ? AND blockee = ?");
					ps.setString(1, loggedInUser);
					ps.setString(2, id);
					rows = ps.executeUpdate();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					if (ps != null) try { ps.close(); } catch (SQLException ignore) {}
				}
			}
			if (rows>0) {
				request.setAttribute("message", "Your request to " + action + " " + id + " has been carried out successfully");
			} else {
				request.setAttribute("message", "There was an error carrying out your " + action + " request");
			}
		} catch(Exception e) {
			//			System.out.println("No Block or unfollow action.");
			id = (request.getParameter("id") == null) ? "" : request.getParameter("id");
		}
		request.setAttribute("userId", id);
		if (!id.equals("")) {
			ps = null;
			try {
				ps = con.prepareStatement("SELECT * FROM dbp51.BabbleUser WHERE username = ?");
				ps.setString(1, id);
				ResultSet rs = ps.executeQuery();
				try {
					if (rs.next()) {
						request = createPage(id, request, rs);
						if (con != null) try { con.close(); } catch (SQLException ignore) {}
						request.getRequestDispatcher("user.ftl").forward(request, response);
					} else {
						request.setAttribute("found", false);
						request.setAttribute("pagetitle", "- User Not Found!");
						if (con != null) try { con.close(); } catch (SQLException ignore) {}
						request.getRequestDispatcher("user.ftl").forward(request, response);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (ps != null) try { ps.close(); } catch (SQLException ignore) {}
			}
		} else {
			request.setAttribute("pagetitle", "- User Not Found!");
			request.setAttribute("found", false);
			try {
				if (con != null) try { con.close(); } catch (SQLException ignore) {}
				request.getRequestDispatcher("user.ftl").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


	}

	private HttpServletRequest createPage(String id, HttpServletRequest request, ResultSet rs_user) {
		request.setAttribute("pagetitle", "- " + id + " Profile");
		Connection con = null;
		try {
			con = DBUtil.getExternalConnection("babble");
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			PreparedStatement ps = con
					.prepareStatement("SELECT * FROM dbp51.follows WHERE follower = ? AND followee = ?");
			ps.setString(1, loggedInUser);
			ps.setString(2, id);
			try {
				ResultSet rs = ps.executeQuery();
				try {
					if (rs.next()) {
						request.setAttribute("follow", "unfollow");
					} else {
						request.setAttribute("follow", "follow");
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				if (ps != null) try { ps.close(); } catch (SQLException ignore) {}
			}
			ps = con.prepareStatement("SELECT * FROM dbp51.blocks WHERE blocker = ? AND blockee = ?");
			ps.setString(1, loggedInUser);
			ps.setString(2, id);
			try {
				ResultSet rs = ps.executeQuery();
				try {
					if (rs.next()) {
						request.setAttribute("block", "unblock");
					} else {
						request.setAttribute("block", "block");
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				if (ps != null) try { ps.close(); } catch (SQLException ignore) {}
			}

			try {
				request.setAttribute("user_username", rs_user.getString(1));
				request.setAttribute("user_name", rs_user.getString(2));
				request.setAttribute("user_status", rs_user.getString(3));
				request.setAttribute("user_pic", rs_user.getString(4));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			checkIfBlocked(con, request, loggedInUser, id);
			if(request.getAttribute("blocked").equals(false))
			{
				ArrayList<Babble> babbles = new ArrayList<>();
				babbles = retrieveBabbles(con, id);
				babbles.addAll(retrieveFollowedUsersBabbles(con, id));
				babbles.addAll(retrieveLikedBabbles(con, id));
				babbles.addAll(retrieveRebabbledBabbles(con, id));
				BabbleAssists.sortBabbles(babbles);
				request.setAttribute("babbles", babbles);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			if (con != null) try { con.close(); } catch (SQLException ignore) {}
		}
		return request;
	}

	private ArrayList<Babble> retrieveBabbles(Connection con, String userId)
	{
		ArrayList<Babble> babbles = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement("SELECT * FROM dbp51.babble WHERE creator = ?")) {
			ps.setString(1, userId);
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
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return babbles;
	}

	private ArrayList<Babble> retrieveFollowedUsersBabbles(Connection con, String userId)
	{
		ArrayList<Babble> babbles = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement("SELECT * FROM dbp51.babble a INNER JOIN (SELECT * FROM dbp51.follows WHERE follower = ?) b ON a.creator = b.followee")) {
			ps.setString(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					babbles.add(new Babble(rs.getInt(1), rs.getString(2), rs.getTimestamp(3),
							rs.getString(4), BabbleAssists.countInteractionsPerType(con, rs.getInt(1), "like", "LikesBabble"),
							BabbleAssists.countInteractionsPerType(con, rs.getInt(1), "dislike", "LikesBabble"),
							BabbleAssists.countInteractionsPerType(con, rs.getInt(1), "rebabble", "Rebabble"),
							BabbleAssists.getNameFromUserName(con, rs.getString(4))));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return babbles;
	}

	private ArrayList<Babble> retrieveLikedBabbles(Connection con, String userId)
	{
		ArrayList<Babble> babbles = new ArrayList<>();
		Babble newBabble;
		try (PreparedStatement ps = con.prepareStatement("SELECT * FROM dbp51.likesbabble WHERE username = ?")) {
			ps.setString(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					try (PreparedStatement ps_babbleInfo = con.prepareStatement("SELECT * FROM dbp51.babble WHERE id=?")) {
						ps_babbleInfo.setInt(1, rs.getInt(2));
						try (ResultSet rs_babbleInfo = ps_babbleInfo.executeQuery()) {
							if (rs_babbleInfo.next()) {
								babbles.add(newBabble = new Babble(rs_babbleInfo.getInt(1), rs_babbleInfo.getString(2), rs_babbleInfo.getTimestamp(3),
										rs_babbleInfo.getString(4), BabbleAssists.countInteractionsPerType(con, rs_babbleInfo.getInt(1), "like", "LikesBabble"),
										BabbleAssists.countInteractionsPerType(con, rs_babbleInfo.getInt(1), "dislike", "LikesBabble"),
										BabbleAssists.countInteractionsPerType(con, rs_babbleInfo.getInt(1), "rebabble", "Rebabble"),
										BabbleAssists.getNameFromUserName(con, rs_babbleInfo.getString(4))));
								if(rs.getString(3).replaceAll("\\s+","").equals("like"))
									newBabble.setInteractionType("geliked");
								else if(rs.getString(3).replaceAll("\\s+","").equals("dislike"))
									newBabble.setInteractionType("disliked");
								newBabble.setInteractionTime(rs.getTimestamp(4));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return babbles;
	}

	private ArrayList<Babble> retrieveRebabbledBabbles(Connection con, String userId)
	{
		ArrayList<Babble> babbles = new ArrayList<>();
		Babble newBabble;
		try (PreparedStatement ps = con.prepareStatement("SELECT * FROM dbp51.babble a INNER JOIN (SELECT * FROM dbp51.rebabble WHERE username = ?) b ON a.id = b.babble")) {
			ps.setString(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					babbles.add(newBabble = new Babble(rs.getInt(1), rs.getString(2), rs.getTimestamp(3), rs.getString(4), BabbleAssists.countInteractionsPerType(con, rs.getInt(1), "like", "LikesBabble"),
							BabbleAssists.countInteractionsPerType(con, rs.getInt(1), "dislike", "LikesBabble"),
							BabbleAssists.countInteractionsPerType(con, rs.getInt(1), "rebabble", "Rebabble"),
							BabbleAssists.getNameFromUserName(con, rs.getString(4))));
					newBabble.setInteractionTime(rs.getTimestamp(7));
					newBabble.setInteractionType("rebabbled");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return babbles;
	}

	private HttpServletRequest checkIfBlocked(Connection con, HttpServletRequest request, String loggedInUser, String userId)
	{
		try (PreparedStatement ps = con
				.prepareStatement("SELECT * FROM dbp51.blocks WHERE blocker = ? AND blockee = ?")) {
			ps.setString(1, userId);
			ps.setString(2, loggedInUser);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					request.setAttribute("blocked", true);
					request.setAttribute("block_reason", rs.getString(3) != null ? rs.getString(3) : "");
				} else {
					request.setAttribute("blocked", false);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return request;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

}
