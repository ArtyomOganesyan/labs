package data;

import javax.xml.bind.annotation.XmlAttribute;

public class Location {

    /** Поле не может быть null */
    @XmlAttribute
    private float x;

    /** Поле не может быть null */
    @XmlAttribute
    private long y;

    @XmlAttribute
    private Double z;

    /** Поле может быть null */
    @XmlAttribute
    private String name;

    public Location(String name, float x, long y, Double z){
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean equals(Location o) {
        if (x == o.x && y == o.y && z == o.z && name.equals(o.name)) return true;
        else return false;
    }

    @Override
    public String toString(){
        return name + " (" + x + ", " + y + ", " + z + ")";
    }

    public Location(){

    }

    public float getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    public Double getZ() {
        return z;
    }

    public String getName() {
        return name;
    }
}