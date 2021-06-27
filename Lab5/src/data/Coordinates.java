package data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "coordinates")
public class Coordinates {

    /** Поле не может быть null */
    @XmlAttribute(name = "x")
    private Long x;

    /** Максимальное значение поля: 248, Поле не может быть null */
    @XmlAttribute(name = "y")
    private Double y;

    public Coordinates(Long x, Double y){
        this.x = x;
        this.y = y;
    }

    public Coordinates(){

    }

    public Long getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    @Override
    public String toString(){
        return "(" + x + ", " + y + ")";
    }
}