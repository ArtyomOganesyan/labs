package data;

import javax.xml.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@XmlRootElement(name = "route")

@XmlAccessorType(XmlAccessType.FIELD)
public class Route implements Comparable<Route>{

    /** Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически*/
    @XmlTransient
    private Integer id;

    /** Поле не может быть null, Строка не может быть пустой */
    @XmlAttribute
    private String name;

    /** Поле не может быть null */
    private Coordinates coordinates;

    /** Поле не может быть null, Значение этого поля должно генерироваться автоматически */
    @XmlTransient
    private java.time.LocalDateTime creationDate;

    /** Поле не может быть null */
    @XmlElement(name = "location_from")
    private Location from;

    /** Поле не может быть null */
    @XmlElement(name = "location_to")
    private Location to;

    /** Поле не может быть null, Значение поля должно быть больше 1 */
    @XmlAttribute
    private Integer distance;

    public Route(String name, Coordinates coordinates, Location from, Location to, Integer distance) {
        this.name = name;
        this.coordinates = coordinates;
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public Route() {

    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");
        return "\n\t" + name +
                "\nIdentifier: \t" + id +
                "\nCoordinates: \t" + coordinates.toString() +
                "\nCreation Date: \t" + creationDate.format(formatter) +
                "\nLocation: \t\t" +
                "from " + from.toString() + " to " + to.toString();
    }

    public long getId(){
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Long getCoordinatesX() {
        return coordinates.getX();
    }

    public Double getCoordinatesY() {
        return coordinates.getY();
    }

    public String getCreationDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");
        return creationDate.format(formatter);
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    public Integer getDistance() {
        return distance;
    }

    @Override
    public int compareTo(Route o) {
        boolean a = this.from.equals(o.from);
        boolean b = this.to.equals(o.to);

        if (this.distance == o.distance && a && b) {
            return 0;
        } else
            return distance - o.distance;
    }
}

