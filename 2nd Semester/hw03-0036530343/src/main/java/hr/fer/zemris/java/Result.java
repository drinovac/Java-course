package hr.fer.zemris.java;

public class Result {

    public Band band;

    public String numberOfVotes;

    public Result(String ID, String name, String songLink, String numberOfVotes) {
        this.band = new Band(ID, name, songLink);
        this.numberOfVotes = numberOfVotes;
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
}
