package hr.fer.zemris.java.model;

public class Band {

    private String ID;
    private String name;
    private String songLink;

    public Band(String ID, String name, String songLink) {
        this.ID = ID;
        this.name = name;
        this.songLink = songLink;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getSongLink() {
        return songLink;
    }
}
