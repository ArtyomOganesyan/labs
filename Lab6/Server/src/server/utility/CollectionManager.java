package server.utility;

import data.Route;
import data.Routes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class CollectionManager implements Serializable {

    private volatile HashMap<Integer, Route> hashOfRoutes;

    protected LocalDateTime creationDateTime;

    public CollectionManager(HashMap<Integer, Route> hashOfRoutes) {
        this.hashOfRoutes = hashOfRoutes;
        setCreationDateTime();
    }

    public synchronized HashMap<Integer, Route> getHashOfRoutes() {
        return hashOfRoutes;
    }

    public String getCreationDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");
        return creationDateTime.format(formatter);
    }

    protected void setCreationDateTime() {
        this.creationDateTime = LocalDateTime.now();
    }

    public Routes getRoutes() {
        Routes routes = new Routes(hashOfRoutes);
        return routes;
    }
}
