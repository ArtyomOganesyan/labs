package data;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@XmlRootElement(name = "routes")
@XmlAccessorType(XmlAccessType.FIELD)
public class Routes implements Serializable {

    private static final long serialVersionUID = 1L;

    private volatile HashMap<Integer, Route> hashOfRoutes;

    protected LocalDateTime creationDateTime;

    public Routes(HashMap<Integer, Route> hashOfRoutes) {
        this.hashOfRoutes = hashOfRoutes;
    }

    public Routes(){
        setCreationDateTime();
    }

    public synchronized HashMap<Integer, Route> getHashOfRoutes() {
        return hashOfRoutes;
    }

    protected void setHashOfRoutes(HashMap<Integer, Route> hashOfRoutes){this.hashOfRoutes = hashOfRoutes;}

    public String getCreationDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");
        return creationDateTime.format(formatter);
    }

    protected void setCreationDateTime() {
        this.creationDateTime = LocalDateTime.now();
    }

}
