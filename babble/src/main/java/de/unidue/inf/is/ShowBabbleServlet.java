
package de.unidue.inf.is;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.utils.BabbleAssists;
import de.unidue.inf.is.utils.DBUtil;

/**
 * Servlet implementation class createBabble
 */

public class ShowBabbleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Integer babbleId;
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
		request.setAttribute("pagetitle", "- Show Babble Details");
		Connection con = null;
		String action = null;
		try {
			babbleId = Integer.parseInt(request.getParameter("id"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			con = DBUtil.getExternalConnection("babble");
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {			
			action = request.getParameter("action");
			if(action.equals("like") || action.equals("dislike"))
			{
				if(action.equals("like"))
					action = String.format("%1$-" + 7 + "s", action);
				try (PreparedStatement ps = con.prepareStatement("SELECT * FROM dbp51.likesBabble WHERE babble = ? AND username = ?")) {
					ps.setInt(1, babbleId);
					ps.setString(2, request.getSession().getAttribute("loggedInUserName").toString());
					try (ResultSet rs = ps.executeQuery()) {
						if (rs.next()) {
							if(rs.getString("type").equals(action))
							{
								try (PreparedStatement ps_deleteLike = con.prepareStatement("DELETE FROM dbp51.LikesBabble WHERE type = ? AND babble = ? AND username = ?")) {
									ps_deleteLike.setString(1, action);
									ps_deleteLike.setInt(2, babbleId);
									ps_deleteLike.setString(3, request.getSession().getAttribute("loggedInUserName").toString());
									int rows = ps_deleteLike.executeUpdate();
									if (rows>0)
										request.setAttribute("submitMessage", "Your " + action.replaceAll(" ", "") + " has been undone!");
									else
										request.setAttribute("submitMessage", "Something went wrong.");

								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							else
							{
								try (PreparedStatement ps_changeType = con.prepareStatement("UPDATE dbp51.LikesBabble SET type = ?, created = CURRENT_TIMESTAMP WHERE babble = ? AND username = ?")) {
									ps_changeType.setString(1, action);
									ps_changeType.setInt(2, babbleId);
									ps_changeType.setString(3, request.getSession().getAttribute("loggedInUserName").toString());
									int rows = ps_changeType.executeUpdate();
									if (rows>0)
										request.setAttribute("submitMessage", "Successfully " + action.replaceAll(" ", "") + "d this babble!");
									else
										request.setAttribute("submitMessage", "Something went wrong.");

								} catch (Exception e) {
									e.printStackTrace();
								}
							} 
						} else {
							try (PreparedStatement ps_insert = con.prepareStatement("INSERT INTO dbp51.likesBabble (username, babble, type) values (?, ?, ?)")) {
								ps_insert.setString(1, request.getSession().getAttribute("loggedInUserName").toString());
								ps_insert.setInt(2, babbleId);
								ps_insert.setString(3, action);
								int rows = ps_insert.executeUpdate();
								if (rows>0)
									request.setAttribute("submitMessage", "Successfully " + action.replaceAll(" ", "") + "d this babble!");
								else
									request.setAttribute("submitMessage", "Something went wrong.");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			} else if(action.equals("rebabble"))
			{
				try (PreparedStatement ps = con.prepareStatement("SELECT * FROM dbp51.ReBabble WHERE babble = ? AND username = ?")) {
					ps.setInt(1, babbleId);
					ps.setString(2, request.getSession().getAttribute("loggedInUserName").toString());
					try (ResultSet rs = ps.executeQuery()) {
						if (rs.next()) {
							try (PreparedStatement ps_deleteRebabble = con.prepareStatement("DELETE FROM dbp51.ReBabble WHERE babble = ? AND username = ?")) {
								ps_deleteRebabble.setInt(1, babbleId);
								ps_deleteRebabble.setString(2, request.getSession().getAttribute("loggedInUserName").toString());
								int rows = ps_deleteRebabble.executeUpdate();
								if (rows>0)
									request.setAttribute("submitMessage", "Your " + action.replaceAll(" ", "") + " has been undone!");
								else
									request.setAttribute("submitMessage", "Something went wrong.");
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							try (PreparedStatement ps_insert = con.prepareStatement("INSERT INTO dbp51.ReBabble (username, babble) values (?, ?)")) {
								ps_insert.setString(1, request.getSession().getAttribute("loggedInUserName").toString());
								ps_insert.setInt(2, babbleId);
								int rows = ps_insert.executeUpdate();
								if (rows>0)
									request.setAttribute("submitMessage", "Successfully rebabbled this babble!");
								else
								{
									request.setAttribute("submitMessage", "Something went wrong.");									
								}

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			} else if(action.equals("delete"))
			{
				// TODO: Security check
				try (PreparedStatement ps_deleteBabble = con.prepareStatement("DELETE FROM dbp51.Babble WHERE id = ?")) {
					ps_deleteBabble.setInt(1, babbleId);
					int rows = ps_deleteBabble.executeUpdate();
					if (rows>0)
						request.setAttribute("submitMessage", "Your babble has been successfully deleted!");
					else
						request.setAttribute("submitMessage", "Something went wrong.");
					try {
						request.getRequestDispatcher("/showBabbleDelete.ftl").forward(
								request, response);
					} catch (ServletException | IOException e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				request.setAttribute("submitMessage", "Something went wrong.");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		try (PreparedStatement ps = con.prepareStatement("SELECT * FROM dbp51.LikesBabble WHERE babble = ? AND username = ?")) {
			ps.setInt(1, babbleId);
			ps.setString(2, request.getSession().getAttribute("loggedInUserName").toString());

			try (ResultSet rs = ps.executeQuery()) {
				if(!rs.next())
					request.setAttribute("liked", "no");
				else
				{
					if(rs.getString("type").equals("like   "))
						request.setAttribute("liked", "liked");
					else
						request.setAttribute("liked", "disliked");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try (PreparedStatement ps = con.prepareStatement("SELECT * FROM dbp51.ReBabble WHERE babble = ? AND username = ?")) {
			ps.setInt(1, babbleId);
			ps.setString(2, request.getSession().getAttribute("loggedInUserName").toString());
			try (ResultSet rs = ps.executeQuery()) {
				if(!rs.next())
					request.setAttribute("rebabbled", "no");
				else
					request.setAttribute("rebabbled", "rebabbled");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		

		showPage(con, request, response, babbleId);
	}


	private void showPage(Connection con, HttpServletRequest request, HttpServletResponse response, Integer babbleId) {
		try (PreparedStatement ps = con.prepareStatement("SELECT * FROM dbp51.babble WHERE id = ?")) {
			ps.setInt(1, babbleId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					request.setAttribute("creator", rs.getString(4));
					request.setAttribute("creatorName", BabbleAssists.getNameFromUserName(con, rs.getString(4)));
					request.setAttribute("text", rs.getString(2));
					request.setAttribute("likes", BabbleAssists.countInteractionsPerType(con, rs.getInt(1), "like", "LikesBabble"));
					request.setAttribute("dislikes", BabbleAssists.countInteractionsPerType(con, rs.getInt(1), "dislike", "LikesBabble"));
					request.setAttribute("rebabbles", BabbleAssists.countInteractionsPerType(con, rs.getInt(1), "rebabble", "ReBabble"));
					request.setAttribute("creationTime", BabbleAssists.getFormattedTime(rs.getTimestamp(3)));
					request.setAttribute("babbleOwner", request.getSession().getAttribute("loggedInUserName").toString().equals(rs.getString(4)));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			request.getRequestDispatcher("/showBabble.ftl").forward(
					request, response);
		} catch (ServletException | IOException e) {
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