package mainPart;

import collection.Coordinates;
import collection.Ticket;
import collection.TicketType;
import collection.VenueType;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DataBaseWorker {

    private final String url = "jdbc:postgresql://pg:5432/studs";
    private final String name = "s312631";
    private final String password = "piy876";
    private LinkedList<Ticket> tickets = new LinkedList<>();
    private Connection connection = null;

    public void createNewAccountInDB(String userName, String userPassword) throws NoSuchAlgorithmException, SQLException {
        String cryptedPassword = cryptData(userPassword);
        connection.prepareStatement(String.format("INSERT INTO users VALUES ('%s','%s');", userName, cryptedPassword)).executeUpdate();
    }
    public void checkAccountInDB(String userName, String password) throws SQLException, NoSuchAlgorithmException {
        if (userName.equals("alreadyLoggedInUser")) throw new SQLException("Недопустимое имя пользователя");
        ResultSet users = connection.prepareStatement(String.format("SELECT * FROM users WHERE login = '%s';", userName)).executeQuery();
        String cryptPassword = cryptData(password);
        int k = 0;
        while (users.next()) {
            k++;
            if (!users.getString("password").equals(cryptPassword)) throw new SQLException("Пароль не подходит");
        }
        if (k == 0) throw new SQLException("Пользователя с данным именем не найденно");
    }
    public String cryptData(Serializable data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-384");
        byte[] cryptPassword = md.digest(ServerMaker.serialize(data + "UniqueSalt"));
        //System.out.println(cryptPassword);
        BigInteger no = new BigInteger(1, cryptPassword);
        return no.toString(16);
    }
    public void connectToDB() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        System.out.println("Драйвер подключен");
        connection = DriverManager.getConnection(url, name, password);
        System.out.println("Соединение установлено");

        /*connection.prepareStatement("CREATE TABLE users\n" +
                "(\n" +
                "    login text COLLATE pg_catalog.\"default\" NOT NULL,\n" +
                "    password text COLLATE pg_catalog.\"default\" NOT NULL,\n" +
                "    CONSTRAINT users_pkey PRIMARY KEY (login)\n" +
                ")\n" +
                "\n" +
                "TABLESPACE pg_default;\n" +
                "\n" +
                "ALTER TABLE users\n" +
                "    OWNER to s312631;").executeUpdate();
        connection.prepareStatement("CREATE TABLE venues\n" +
                "(\n" +
                "    id serial NOT NULL ,\n" +
                "    name text NOT NULL,\n" +
                "    capacity integer,\n" +
                "    type text,\n" +
                "    CONSTRAINT venues_pkey PRIMARY KEY (id),\n" +
                "    CONSTRAINT venues_capacity_check CHECK (capacity > 0));").executeUpdate();
        connection.prepareStatement("CREATE TABLE tickets\n" +
                "(\n" +
                "    id serial NOT NULL,\n" +
                "    owner text NOT NULL,\n" +
                "    name text NOT NULL,\n" +
                "    \"coordinateX\" double precision NOT NULL,\n" +
                "    \"coordinateY\" double precision NOT NULL,\n" +
                "    price double precision NOT NULL,\n" +
                "    type text,\n" +
                "    \"creationDate\" timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "    \"venueId\" integer,\n" +
                "    CONSTRAINT tickets_pkey PRIMARY KEY (id),\n" +
                "    CONSTRAINT \"ticket-venueLink\" FOREIGN KEY (\"venueId\")\n" +
                "        REFERENCES venues (id) MATCH SIMPLE\n" +
                "        ON UPDATE NO ACTION\n" +
                "        ON DELETE NO ACTION\n" +
                "        NOT VALID,\n" +
                "    CONSTRAINT \"tickets_coordinateX_check\" CHECK (\"coordinateX\" > '-48'::integer::double precision),\n" +
                "    CONSTRAINT tickets_price_check CHECK (price > 0::double precision)\n" +
                ");").executeUpdate();*/
    }

    public void getCollectionFromDB() throws SQLException {
        ResultSet resultTicketSet = connection.prepareStatement("SELECT * FROM tickets;").executeQuery();
        while (resultTicketSet.next()) {
            int ticketId = resultTicketSet.getInt("id");
            String ticketOwner = resultTicketSet.getString("owner");
            String ticketName = resultTicketSet.getString("name");
            Double ticketCoordinateX = resultTicketSet.getDouble("coordinateX");
            double ticketCoordinateY = resultTicketSet.getDouble("coordinateY");
            Double ticketPrice = resultTicketSet.getDouble("price");
            TicketType ticketType = null;
            try {
                ticketType = TicketType.valueOf(resultTicketSet.getString("type"));
            } catch (NullPointerException e) {}
            Integer venueId = resultTicketSet.getInt("venueId");
            String venueName = null;
            Integer venueCapacity = null;
            VenueType venueType = null;
            if (venueId != 0) {
                ResultSet resultVenueSet = connection.prepareStatement(String.format("SELECT * FROM venues WHERE id = '%s';", venueId)).executeQuery();
                resultVenueSet.next();
                venueName = resultVenueSet.getString("name");
                venueCapacity = resultVenueSet.getInt("capacity");
                venueType = VenueType.valueOf(resultVenueSet.getString("type"));
            }
            Ticket ticket = new Ticket(ticketId, ticketOwner, ticketName, new Coordinates(ticketCoordinateX, ticketCoordinateY), ticketPrice, ticketType, venueId, venueName, venueCapacity, venueType);
            tickets.add(ticket);
        }
        CommandDecoder.sort(tickets);
    }
    public void addTicketToDB(Ticket ticket, String userName) throws SQLException {
        if (ticket.getId() == 0) {
            Integer venueId = null;
            if (ticket.getVenue() != null) {
                connection.prepareStatement(String.format("INSERT INTO venues (name, capacity, type) VALUES ('%s', '%s', '%s');", ticket.getVenue().getName(), ticket.getVenue().getCapacity().toString().replace(',', '.'), ticket.getVenue().getType()).replace("'null'", "NULL")).executeUpdate();
                ResultSet resultSet = connection.prepareStatement(String.format("SELECT id FROM venues WHERE name = '%s'", ticket.getVenue().getName())).executeQuery();
                while (resultSet.next()) {
                    venueId = resultSet.getInt("id");
                };

            }
            connection.prepareStatement(String.format("INSERT INTO tickets (owner, name, \"coordinateX\", \"coordinateY\", price, type, \"venueId\") VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s');",
                    ticket.getOwner(), ticket.getName(), ticket.getCoordinates().getX().toString().replace(',','.'),
                    ticket.getCoordinates().getY().toString().replace(',','.'),
                    ticket.getPrice().toString().replace(',','.'), ticket.getType(),
                    venueId).replace("'null'", "NULL")).executeUpdate();
            setTickets(new LinkedList<>());
            getCollectionFromDB();

        } else updateTicketInDB(ticket, userName);
    }

    public void updateTicketInDB(Ticket ticket, String userName) throws SQLException {
        if (!ticket.getOwner().equals(userName)) throw new SQLException();
        if (ticket.getVenue() != null) {
            connection.prepareStatement(String.format("UPDATE venues SET name = '%s', capacity = '%s', type = '%s' WHERE id = '%s';", ticket.getVenue().getName(), ticket.getVenue().getCapacity().toString().replace(',', '.'), ticket.getVenue().getType(), ticket.getVenue().getId()).replace("'null'","NULL")).executeUpdate();
        }
            connection.prepareStatement(String.format("UPDATE tickets SET owner = '%s', name = '%s', \"coordinateX\" = '%s', \"coordinateY\" = '%s', price = '%s', type = '%s' WHERE id = '%s';",
                    ticket.getOwner(), ticket.getName(), ticket.getCoordinates().getX().toString().replace(',','.'),
                    ticket.getCoordinates().getY().toString().replace(',','.'),
                    ticket.getPrice().toString().replace(',','.'), ticket.getType(),
                    ticket.getId()).replace("'null'", "NULL")).executeUpdate();
    }

    public void removeTicketFromDB(Ticket ticket) throws SQLException {
        connection.prepareStatement(String.format("DELETE FROM tickets WHERE id = '%s';", ticket.getId())).executeUpdate();
        if (ticket.getVenue() != null) connection.prepareStatement(String.format("DELETE FROM venues WHERE id = '%s';", ticket.getVenue().getId())).executeUpdate();
    }

    public void updateDB() throws SQLException {
        LinkedList<Ticket> newTickets = getTickets();
        setTickets(new LinkedList<>());
        getCollectionFromDB();
        Set<Integer> ticketId = new HashSet<>();
        for (Ticket ticket : newTickets) {
            ticketId.add(ticket.getId());
        }
        for (Ticket ticket : getTickets()) {
            if (!ticketId.contains(ticket.getId())) removeTicketFromDB(ticket);
        }
        setTickets(newTickets);
    }

    public synchronized LinkedList<Ticket> getTickets() {return tickets;}
    public synchronized void setTickets(LinkedList<Ticket> tickets) {this.tickets = tickets;}
}
