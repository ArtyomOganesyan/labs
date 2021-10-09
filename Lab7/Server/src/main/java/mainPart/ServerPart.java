package mainPart;

import commands.*;
import collection.*;
import exceptions.ConnectionException;
import exceptions.IdNotFoundException;
import exceptions.IncorrectInputDataException;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.channels.SocketChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class ServerPart {

    private final ServerMaker serverMaker;
    private final DataBaseWorker dataBaseWorker;
    private static boolean isClientConnected = true;
    public boolean isItWaiting = false;
    //public Set<String> waitingUsers = new HashSet<>();

    public ServerPart (ServerMaker serverMaker) {
        dataBaseWorker = new DataBaseWorker();
        this.serverMaker = serverMaker;
    }

    public Serializable waitForRead(String userName, Serializable classToRead) throws  ConnectionException {
        while(true) {
            for (Serializable serializable : serverMaker.getResponses().keySet()) {
                if (serializable != null) {
                    try {
                        if (serverMaker.getResponses().get(serializable).equals(userName) && serializable.getClass() == classToRead.getClass()) {
                            serverMaker.removeResponse(serializable);
                            isItWaiting = false;
                            return serializable;
                        }
                    } catch (NullPointerException e) {e.printStackTrace();}
                }

            }
        }
    }

    public void waitForWrite(Serializable request, String userName) throws ConnectionException {
        serverMaker.addRequest(userName, request);
        while(serverMaker.getRequests().contains(request)) {};
        isItWaiting = false;
    }

    private void readCommand(Command command, String userName) {
        try {
            String s = check(command, userName);
            serverMaker.addRequest(userName, s);
        } catch (ConnectionException e) {
            System.out.println(e.getMessage());
            isClientConnected = false;
        }
    }

    public void readCommands() {
        ExecutorService service = Executors.newFixedThreadPool(4);
        while (isClientConnected) {
            try {
                try {
                for (Serializable serializable : serverMaker.getResponses().keySet()) {
                    Command command = (Command) serializable;
                        service.execute(() -> {
                            if (command != null) {
                                String user = serverMaker.getResponses().get(serializable);
                                serverMaker.removeResponse(serializable);
                                if (user != null) {
                                    readCommand(command, user);
                                }
                            }
                        });
                }
                } catch (ClassCastException e) {
                    //System.out.println(serverMaker.getResponses());
                }
            } catch(ConnectionException e) {
                System.out.println(e.getMessage());
                isClientConnected = false;
            }
        }
        service.shutdown();

    }

    private String check(Command command, String userName) {
        try {
            String s = "";
            try {
            if (command != null) {
                ((CommandWithoutAdditionalArgument) command).updateCollection(dataBaseWorker.getTickets());
                Ticket newTicket = null;

                if (command.getClass() == AddCommand.class) {
                    newTicket = ((AddCommand) command).ticket;
                }
                if (command.getClass() == AddIfMaxCommand.class && ((AddIfMaxCommand) command).canNewTicketBeAdded()) {
                    newTicket = ((AddIfMaxCommand) command).ticket;
                }
                if (command.getClass() == UpdateCommand.class) {
                    s = command.execute();
                    newTicket = ((UpdateCommand) command).ticket;
                }
                if (newTicket != null) {
                    updateTicketFields(newTicket, userName);
                    dataBaseWorker.addTicketToDB(newTicket, userName);
                    s += "Ticket created";
                }
                if (command.getClass() != UpdateCommand.class) s += command.execute(userName);
            }
            if (((CommandWithoutAdditionalArgument)command).isCollectionChanged()) dataBaseWorker.updateDB();
            return s;
        } catch (IdNotFoundException e) {return e.getMessage();}
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return "Не удалось это сделать Ticket created";
        }
    }

    private void updateTicketFields(Ticket ticket, String userName) throws SQLException {
        String s;
        String response = "";
        ticket.setCoordinates(null);
        ticket.setType(null);
        if (ticket.getOwner() != null && !ticket.getOwner().equals(userName)) throw new SQLException();
        ticket.setOwner(userName);
        do {
            waitForWrite("Введите название билета: ", userName);
            s = (String) waitForRead(userName, "");
            ticket.setName(s);
        } while(s.equals(""));

        do {
            waitForWrite(response + "Введите координаты: (в формате x y)", userName);
            response = "";
            String p = (String) waitForRead(userName, "");
            String[] j = p.split(" ");
            String[] jj = p.split("\t");
            if (jj.length > j.length) j = jj;
            try {
                if (j.length != 2) response += "Введите корректное число аргументов \n";
                else if (Double.parseDouble(j[0]) > -48 && Double.parseDouble(j[1]) > -48) ticket.setCoordinates(new Coordinates(Double.parseDouble(j[0]), Double.parseDouble(j[1])));
                else response += "Введите корректные значения x и y (они должны быть больше -48) \n";
            } catch (NumberFormatException e) {response += "Введите корректные значения x и y (они должны быть больше -48) \n";}

        } while(ticket.getCoordinates() == null);

        if (ticket.getId() != 0 || ticket.getPrice() == null) {
            ticket.setPrice(-1.0);
            do {
                waitForWrite(response + "Введите стоимость билета: (она должна быть больше 0)", userName);
                response = "";
                String[] j = ((String) waitForRead(userName, "")).split(" ");
                try {
                    if (j.length == 1) ticket.setPrice(Double.parseDouble(j[0]));
                    else {
                        response += "Введите корректное число \n";
                    }
                } catch (NumberFormatException e) {
                    response += "Введите корректную стоимость \n";
                }
            } while (ticket.getPrice() <= 0);
        }
        do {
            waitForWrite(response + "Введите тип билета: (оставьте поле пустым, если хотите) \nСписок возможным типов: VIP, USUAL, BUDGETARY, CHEAP", userName);
            response = "";
            s = (String) waitForRead(userName, "");
            if (!s.equals("")) {
                try {
                    ticket.setType(TicketType.valueOf(s.toUpperCase()));
                } catch (IllegalArgumentException e) { response += "Введите корректное название типа \n";}
            }
        } while (ticket.getType() == null && !s.equals(""));

        waitForWrite("Куда билет? (если не хотите вводить, оставьте поле пустым, для продолжения напишите название места)", userName);
        s = (String) waitForRead(userName, "");

        if (!s.equals("")) {
            ticket.setVenue(new Venue(s));
            s = "";
            do {
                waitForWrite(response + "Введите вместимость аудитории: (оставтье пустым, если она неизвестна)", userName);
                response = "";
                try {
                    s = (String) waitForRead(userName, "");
                    if (s != null && !s.equals("") && Integer.parseInt(s) > 0) ticket.getVenue().setCapacity(Integer.parseInt(s));
                } catch (NumberFormatException | NullPointerException e) {response += "Введите корректное значение \n";}
            } while (ticket.getVenue().getCapacity() == null && s != null && !s.equals(""));

            do {
                waitForWrite(response + "Введите тип аудитории: (оставьте поле пустым, если хотите) \nСписок возможным типов:   BAR, LOFT, THEATRE, MALL, STADIUM", userName);
                response = "";
                s = (String) waitForRead(userName, "");
                if (!s.equals("")) {
                    try {
                        ticket.getVenue().setType(VenueType.valueOf(s.toUpperCase()));
                    } catch (IllegalArgumentException e) {response += "Введите корректное название типа \n";}
                }
            } while (ticket.getVenue().getType() == null && !s.equals(""));
        }
    }

    public DataBaseWorker getDataBaseWorker() {return dataBaseWorker;}
    public boolean isIsClientConnected() {return isClientConnected;}


}
