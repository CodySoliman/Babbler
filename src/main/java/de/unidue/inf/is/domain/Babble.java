package de.unidue.inf.is.domain;

import java.sql.Timestamp;


public final class Babble {

    Integer id;
    String text;
    Timestamp creationTime;
    String creatorUsername;
    Integer likeCount;
    Integer dislikeCount;
    Integer rebabbleCount;
    Timestamp interactionTime;
    String interactionType = "optional";
//    ArrayList<LikeInfo> liked;
//    ArrayList<RebabbleInfo> rebabbles;

    public Babble(Integer id, String text, Timestamp creationTime, String creatorUsername) {
        this.id = id;
        this.text = text;
        this.creationTime = creationTime;
        this.creatorUsername = creatorUsername;
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
    }

    public String getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }


}
