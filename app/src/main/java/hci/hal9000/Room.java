package hci.hal9000;

public class Room {
    private String name;
    private int idDrawable;
    private String id;

    public Room(String name, int idDrawable) {
        this.name = name;
        this.idDrawable = idDrawable;
    }

    public String getName() {
        return name;
    }

    public int getIdDrawable() {
        return idDrawable;
    }

    public String getId() {
        return id;
    }
}
