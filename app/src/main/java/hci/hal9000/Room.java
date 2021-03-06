package hci.hal9000;

public class Room {
    private String name;
    private String id;
    private String meta;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
        return (this.id == null)?
                String.format("%s - %s",this.getName(),this.getMeta()) :
                String.format("%s - %s - %s", this.getId(),this.getName(),this.getMeta());
    }
}
