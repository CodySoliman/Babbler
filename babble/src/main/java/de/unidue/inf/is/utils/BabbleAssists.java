/**
 * 
 */
package de.unidue.inf.is.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import de.unidue.inf.is.domain.Babble;

/**
 * @author codys
 *
 */
public class BabbleAssists {

	public static ArrayList<Babble> sortBabbles(ArrayList<Babble> babbles){
		for(int i=0; i<babbles.size()-1;i++)
		{
			for(int j=i+1;j<babbles.size();j++)
			{
				if(!babbles.get(i).getInteractionType().equals("optional"))
				{
					if(!babbles.get(j).getInteractionType().equals("optional"))
					{
						if(!babbles.get(i).getInteractionTime().after(babbles.get(j).getInteractionTime()))
							Collections.swap(babbles, i, j);
					}
					else
					{
						if(!babbles.get(i).getInteractionTime().after(babbles.get(j).getCreationTime()))
							Collections.swap(babbles, i, j);
					}
				} else {
					if(!babbles.get(j).getInteractionType().equals("optional"))
					{
						if(!babbles.get(i).getCreationTime().after(babbles.get(j).getInteractionTime()))
							Collections.swap(babbles, i, j);
					} else {
						if(!babbles.get(i).getCreationTime().after(babbles.get(j).getCreationTime()))
							Collections.swap(babbles, i, j);
					}
				}
			}
		}
		return babbles;
	}

	public static Integer countInteractionsPerType(Connection con, Integer babbleId, String type, String tableName){
		PreparedStatement ps;
		try {
			if(type == "rebabble")
				ps = con.prepareStatement("SELECT count(*) FROM dbp51." + tableName +" WHERE babble = ?");
			else {
				ps = con.prepareStatement("SELECT count(*) FROM dbp51." + tableName +" WHERE babble = ? AND type = ?");
				ps.setString(2, type);
			}
			ps.setInt(1, babbleId);
			try (ResultSet rs = ps.executeQuery()) {
				if(rs.next())
					return rs.getInt(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return 0;
	}

	public static String getNameFromUserName(Connection con, String babbleUsername){
		try (PreparedStatement ps = con.prepareStatement("SELECT name FROM dbp51.BabbleUser WHERE username = ?")) {
			ps.setString(1, babbleUsername);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return rs.getString(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
	
	public static String getFormattedTime(Timestamp time)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd. MMMM yyyy HH:mm", Locale.GERMAN);
		return dateFormat.format(time) + " Uhr";
	}

}
