package de.unidue.inf.is.domain;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;



public final class Babble {

    Integer id;
    String text;
    Timestamp creationTime;
    String formattedCreationTime;
    String creatorUsername;
    String creatorName;
    Integer likeCount;
	Integer dislikeCount;
	Integer rebabbleCount;
	Timestamp interactionTime;
	String formattedInteractionTime;
    String interactionType= "optional";
//    ArrayList<LikeInfo> liked;
//    ArrayList<RebabbleInfo> rebabbles;
    
    public Babble(Integer id, String text, Timestamp creationTime, String creatorUsername, Integer likeCount, Integer dislikeCount, Integer rebabbleCount, String creatorName) {
        this.id = id;
        this.text = text;
        this.creationTime = creationTime;
        this.creatorUsername = creatorUsername;
        this.creatorName = creatorName;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.rebabbleCount = rebabbleCount;
        this.formattedCreationTime = getFormattedTime(creationTime);
    }
    
    public Babble(Integer id, String text, Timestamp creationTime, String creatorUsername, String creatorName) {
        this.id = id;
        this.text = text;
        this.creationTime = creationTime;
        this.creatorUsername = creatorUsername;
        this.creatorName = creatorName;
        this.formattedCreationTime = getFormattedTime(creationTime);
    }

//    public Babble(Integer id, String text, Timestamp creationTime, String creatorUsername, ArrayList<LikeInfo> liked, ArrayList<RebabbleInfo> rebabbles) {
//        this.id = id;
//        this.text = text;
//        this.creationTime = creationTime;
//        this.creatorUsername = creatorUsername;
//        this.liked = liked;
//        this.rebabbles = rebabbles;
//    }
//    
//    public ArrayList<RebabbleInfo> getRebabbles() {
//		return rebabbles;
//	}
//
//	public ArrayList<LikeInfo> getLiked() {
//		return liked;
//	}

	public Integer getId() {
        return id;
    }

	public String getText() {
        return text;
    }
    
    public Timestamp getCreationTime() {
        return creationTime;
    }
    
    public String getCreatorUsername() {
        return creatorUsername;
    }
    
    public String getCreatorName() {
		return creatorName;
	}
    
    public Integer getLikeCount() {
		return likeCount;
	}

	public Integer getDislikeCount() {
		return dislikeCount;
	}

	public Integer getRebabbleCount() {
		return rebabbleCount;
	}
	
	public Timestamp getInteractionTime() {
		return interactionTime;
	}

	public void setInteractionTime(Timestamp interactionTime) {
		this.interactionTime = interactionTime;
		this.formattedInteractionTime = getFormattedTime(interactionTime);
	}
	
	private String getFormattedTime(Timestamp time)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd. MMMM yyyy HH:mm", Locale.GERMAN);
		return dateFormat.format(time) + " Uhr";
	}

	public String getFormattedCreationTime() {
		return formattedCreationTime;
	}

	public String getFormattedInteractionTime() {
		return formattedInteractionTime;
	}

	public String getInteractionType() {
		return interactionType;
	}
	
	public void setInteractionType(String interactionType) {
		this.interactionType = interactionType;
	}
	
	

}
