package server;

import exceptions.ClosingSocketException;
import exceptions.ConnectionErrorException;
import exceptions.NoSuchCommandException;
import exceptions.OpeningServerSocketException;
import interaction.ResponseCode;
import interaction.Request;
import interaction.Response;
import server.utility.RequestHandler;
import utility.Outputer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server {
    private final int port;
    private final int soTimeout;
    private ServerSocket serverSocket;
    private RequestHandler requestHandler;

    public Server (int port, int soTimeout, RequestHandler requestHandler) {
        this.port = port;
        this.soTimeout = soTimeout;
        this.requestHandler = requestHandler;
    }

    public void run () {
        try {
            openServerSocket();
            boolean isProcessing = true;
            while (isProcessing) {
                try (Socket clientSocket = connectToClient()){
                    isProcessing = processClientRequest(clientSocket);
                } catch (ConnectionErrorException | SocketTimeoutException e) {
                    break;
                } catch (IOException e) {
                    Outputer.printerr("Произошла ошибка при попытке завершить соединение с клиентом!");
                }
            }
            stop();
        } catch (OpeningServerSocketException e){
            Outputer.printerr("Сервер не может быть запущен!");
        }
    }

    private void openServerSocket() throws OpeningServerSocketException {
        try {
            Outputer.println("Запуск сервера...");
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(soTimeout);
            Outputer.println("Сервер успешно запущен.");
        } catch (IllegalArgumentException e){
            Outputer.printerr("Port '" + port + "' is out of range");
            throw new OpeningServerSocketException();
        } catch (IOException e) {
            Outputer.printerr("An error has occurred while trying to use port '" + port +"'!");
            throw new OpeningServerSocketException();
        }
    }

    private Socket connectToClient() throws ConnectionErrorException, SocketTimeoutException {
        try {
            Outputer.println("Listening to port '" + port + "'...");
            Socket clientSocket = serverSocket.accept();
            Outputer.println("Connection with client successfully established!");
            return clientSocket;
        } catch (SocketTimeoutException e) {
            Outputer.printerr("Connection timeout exceeded");
            throw new SocketTimeoutException();
        } catch (IOException e) {
            Outputer.printerr("An error has occurred when connecting with client");
            throw new ConnectionErrorException();
        }
    }

    private void stop() {
        try {
            Outputer.println("Завершение работы сервера...");
            if (serverSocket == null) throw new ClosingSocketException();
            serverSocket.close();
            Outputer.println("Server processing completed successfully!");
        } catch (ClosingSocketException e) {
            Outputer.printerr("Невозможно завершить работу еще не запущенного сервера!");
        } catch (IOException e) {
            Outputer.printerr("Произошла ошибка при завершении работы сервера!");
        }
    }

    private boolean processClientRequest(Socket clientSocket) {
        Request userRequest = null;
        Request routeRequest = null;
        Response responseToUser = null;
        try (ObjectInputStream clientReader = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.getOutputStream())) {
            do {
                userRequest = (Request) clientReader.readObject();

                responseToUser = requestHandler.handle(userRequest);
                Outputer.println("Запрос '" + userRequest.getInputLine() + "' успешно обработан.");
                clientWriter.writeObject(responseToUser);
                clientWriter.flush();
            } while (responseToUser.getResponseCode() != ResponseCode.SERVER_EXIT);
            return false;
        } catch (NoSuchCommandException e) {
            Outputer.printerr("Такой команды нет");
            // FIXME: 26.06.2021
        } catch (ClassNotFoundException e) {
            Outputer.printerr("An error has occurred while reading gotten files");
        } catch (IOException e) {
            if (userRequest == null) {
                Outputer.printerr("Непредвиденный разрыв соединения с клиентом!");
            } else {
                Outputer.println("Клиент успешно отключен от сервера!");
            }
        }
        return true;
    }
}
