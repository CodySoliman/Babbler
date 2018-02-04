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
import java.util.List;

import de.unidue.inf.is.domain.Babble;
import de.unidue.inf.is.utils.DBUtil;

/**
 * Einfaches Beispiel, das die Verwendung des {@link UserStore}s zeigt.
 */
public final class UserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // mach was

        Connection con = null;
        request.setAttribute("found", true);
        try {
            con = DBUtil.getExternalConnection("babble");
        } catch (SQLException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        String id = (request.getParameter("id") == null) ? "" : request.getParameter("id");
        if (!id.equals("")) {
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM dbp51.BabbleUser WHERE username = ?")) {
                ps.setString(1, id);
                System.out.println(ps);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("3");
                        System.out.println(rs.getString(2));
                        request = createPage(id, request, rs, con);
                        request.getRequestDispatcher("user.ftl").forward(request, response);
                    } else {
                        System.out.println("4");
                        request.setAttribute("found", false);
                        request.setAttribute("pagetitle", "User Not Found!");
                        request.getRequestDispatcher("user.ftl").forward(request, response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        } else {
            request.setAttribute("pagetitle", "User Not Found!");
            request.setAttribute("found", false);
            try {
                request.getRequestDispatcher("user.ftl").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // User userToAdd = new User("Manfred", "Mustermann");
        // try (UserStore userStore = new UserStore()) {
        // userStore.addUser(userToAdd);
        // // userStore.somethingElse();
        // userStore.complete();
        // }

        // mach noch mehr

    }

    private HttpServletRequest createPage(String id, HttpServletRequest request, ResultSet rs_user, Connection con) {
        request.setAttribute("pagetitle", id + " Profile");
        try (PreparedStatement ps = con
                .prepareStatement("SELECT * FROM dbp51.follows WHERE follower = ? AND followee = ?")) {
            ps.setString(1, "Cody");
            ps.setString(2, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    request.setAttribute("follow", "unfollow");
                } else {
                    request.setAttribute("follow", "follow");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try (PreparedStatement ps = con
                .prepareStatement("SELECT * FROM dbp51.blocks WHERE blocker = ? AND blockee = ?")) {
            ps.setString(1, "dbuser");
            ps.setString(2, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    request.setAttribute("block", "unblock");
                } else {
                    request.setAttribute("block", "block");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {
            request.setAttribute("user_username", rs_user.getString(1));
            request.setAttribute("user_name", rs_user.getString(2));
            request.setAttribute("user_status", rs_user.getString(3));
            System.out.println(rs_user.getString(4));
            System.out.println(rs_user.getObject(4));
            request.setAttribute("user_pic", rs_user.getString(4));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        List<Babble> babbles = new ArrayList<>();
        babbles = retrieveBabbles(con, id);
        babbles.addAll(retrieveLikedBabbles(con, id));
        babbles.addAll(retrieveRebabbledBabbles(con, id));
        System.out.println(babbles.size());
        request.setAttribute("babbles", babbles);

        return request;
    }

    private List<Babble> retrieveBabbles(Connection con, String userId) {
        List<Babble> babbles = new ArrayList<>();
//		List<LikeInfo> babbleLikeInfo = new ArrayList<>();
//		List<RebabbleInfo> babbleRebabbleInfo = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM dbp51.babble WHERE creator = ?")) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
//					try (PreparedStatement ps_like = con.prepareStatement("SELECT * FROM dbp51.likesbabble WHERE user = ?")) {
//						ps_like.setString(1, id);
//						try (ResultSet rs_liked = ps_like.executeQuery()) {
//							while (rs.next()) {
//								babbleLikeInfo.add(new LikeInfo(rs.getInt(1), rs.getString(2), rs.getTimestamp(3), rs.getString(4), new Array));
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					} catch (SQLException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
                    babbles.add(new Babble(rs.getInt(1), rs.getString(2), rs.getTimestamp(3), rs.getString(4)));
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

    private List<Babble> retrieveLikedBabbles(Connection con, String userId) {
        List<Babble> babbles = new ArrayList<>();
        Babble newBabble;
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM dbp51.likesbabble WHERE username = ?")) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                System.out.println(rs.getFetchSize());
                while (rs.next()) {
                    try (PreparedStatement ps_babbleInfo = con.prepareStatement("SELECT * FROM dbp51.babble WHERE id=?")) {
                        ps_babbleInfo.setInt(1, rs.getInt(2));
                        try (ResultSet rs_babbleInfo = ps_babbleInfo.executeQuery()) {
                            if (rs_babbleInfo.next()) {
                                babbles.add(newBabble = new Babble(rs_babbleInfo.getInt(1), rs_babbleInfo.getString(2), rs_babbleInfo.getTimestamp(3), rs_babbleInfo.getString(4)));
                                if (rs.getString(3).replaceAll("\\s+", "").equals("like"))
                                    newBabble.setInteractionType("geliked");
                                else if (rs.getString(3).replaceAll("\\s+", "").equals("dislike"))
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

    private List<Babble> retrieveRebabbledBabbles(Connection con, String userId) {
        List<Babble> babbles = new ArrayList<>();
        Babble newBabble;
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM dbp51.rebabble WHERE username = ?")) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    try (PreparedStatement ps_babbleInfo = con.prepareStatement("SELECT * FROM dbp51.babble WHERE id=?")) {
                        ps_babbleInfo.setInt(1, rs.getInt(2));
                        try (ResultSet rs_babbleInfo = ps_babbleInfo.executeQuery()) {
                            if (rs_babbleInfo.next()) {
                                babbles.add(newBabble = new Babble(rs_babbleInfo.getInt(1), rs_babbleInfo.getString(2), rs_babbleInfo.getTimestamp(3), rs_babbleInfo.getString(4)));
                                newBabble.setInteractionTime(rs.getTimestamp(3));
                                newBabble.setInteractionType("rebabbled");
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

}
