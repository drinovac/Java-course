package hr.fer.zemris.java.model;

public class Result {

    public Band band;

    public String numberOfVotes;

    public String numberOfLikes;
    public String numberOfDislikes;

    public Result(String ID, String name, String songLink, String numberOfVotes, String numberOfLikes, String numberOfDislikes) {
        this.band = new Band(ID, name, songLink);
        this.numberOfVotes = numberOfVotes;
        this.numberOfLikes = numberOfLikes;
        this.numberOfDislikes = numberOfDislikes;
    }


    public String getID() {
        return this.band.getID();
    }
    public String getName() {
        return this.band.getName();
    }
    public String getSongLink() {
        return this.band.getSongLink();
    }
    public String getNumberOfVotes() {
        return numberOfVotes;
    }

    public String getNumberOfLikes() {
        return numberOfLikes;
    }
    public String getNumberOfDislikes() {
        return numberOfDislikes;
    }
}
