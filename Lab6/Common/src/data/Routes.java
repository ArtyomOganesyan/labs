package data;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@XmlRootElement(name = "routes")
@XmlAccessorType(XmlAccessType.FIELD)
public class Routes implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "route")
    private List<Route> listOfRoutes = null;
    @XmlTransient
    private HashMap<Integer, Route> hashOfRoutes = new HashMap<>();

    @XmlTransient
    protected LocalDateTime creationDateTime;

    public Routes(){
        setCreationDateTime();
    }

    public List<Route> getListOfRoutes(){
        return listOfRoutes;
    }

    protected void setListOfRoutes(List<Route> listOfRoutes) {
        this.listOfRoutes = listOfRoutes;
    }

    public HashMap<Integer, Route> getHashOfRoutes() {
        return hashOfRoutes;
    }

    public String getCreationDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");
        return creationDateTime.format(formatter);
    }

    protected void setCreationDateTime() {
        this.creationDateTime = LocalDateTime.now();
    }

}
