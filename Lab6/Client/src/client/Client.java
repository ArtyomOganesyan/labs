package client;

import client.utility.UserHandler;
import exceptions.ConnectionErrorException;
import exceptions.NotInDeclaredLimitsException;
import interaction.ResponseCode;
import interaction.Request;
import interaction.Response;
import utility.Outputer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Client {
    private String host;

    private int port;
    private int reconnectionTimeout;
    private int reconnectionAttempts;
    private int maxReconnectionAttempts;

    private UserHandler userHandler;
    private SocketChannel socketChannel;
    private ObjectOutputStream serverWriter;
    private ObjectInputStream serverReader;

    public Client(String host, int port, int reconnectionTimeout, int maxReconnectionAttempts, UserHandler userHandler) {
        this.host = host;
        this.port = port;
        this.reconnectionTimeout = reconnectionTimeout;
        this.maxReconnectionAttempts = maxReconnectionAttempts;
        this.userHandler = userHandler;
    }

    public void run() {
        try {
            boolean processingStatus = true;
            while (processingStatus) {
                try {
                    connectToServer();
                    processingStatus = processRequestToServer();
                } catch (ConnectionErrorException exception) {
                    if (reconnectionAttempts >= maxReconnectionAttempts) {
                        Outputer.printerr("Превышено количество попыток подключения!");
                        break;
                    }
                    try {
                        Thread.sleep(reconnectionTimeout);
                    } catch (IllegalArgumentException timeoutException) {
                        Outputer.printerr("Время ожидания подключения '" + reconnectionTimeout +
                                "' находится за пределами возможных значений!");
                        Outputer.println("Повторное подключение будет произведено немедленно.");
                    } catch (Exception timeoutException) {
                        Outputer.printerr("Произошла ошибка при попытке ожидания подключения!");
                        Outputer.println("Повторное подключение будет произведено немедленно.");
                    }
                }
                reconnectionAttempts++;
            }
            if (socketChannel != null) socketChannel.close();
            Outputer.println("Работа клиента успешно завершена.");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printerr("Клиент не может быть запущен!");
        } catch (IOException exception) {
            Outputer.printerr("Произошла ошибка при попытке завершить соединение с сервером!");
        }
    }

    private void connectToServer() throws ConnectionErrorException, NotInDeclaredLimitsException {
        try {
            if (reconnectionAttempts >= 1) Outputer.println("Повторное соединение с сервером...");
            socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            Outputer.println("Соединение с сервером успешно установлено.");
            Outputer.println("Ожидание разрешения на обмен данными...");
            serverWriter = new ObjectOutputStream(socketChannel.socket().getOutputStream());
            serverReader = new ObjectInputStream(socketChannel.socket().getInputStream());
            Outputer.println("Разрешение на обмен данными получено.");
        } catch (IllegalArgumentException exception) {
            Outputer.printerr("Адрес сервера введен некорректно!");
            throw new NotInDeclaredLimitsException();
        } catch (IOException exception) {
            Outputer.printerr("Произошла ошибка при соединении с сервером!");
            throw new ConnectionErrorException();
        }
    }

    private boolean processRequestToServer() {
        Request requestToServer = null;
        Response serverResponse = null;
        do {
            try {
                if (serverResponse != null) {
                    requestToServer = userHandler.handle(serverResponse);
                } else requestToServer = userHandler.handle(new Response(null, ResponseCode.OK, ""));
                if (requestToServer.isEmpty()) {
                    continue;
                } else {
                    serverWriter.writeObject(requestToServer);
                    serverResponse = (Response) serverReader.readObject();
                    Outputer.print(serverResponse.getResponseBody());
                }
            } catch (InvalidClassException | NotSerializableException exception) {
                Outputer.printerr("Произошла ошибка при отправке данных на сервер!");
            } catch (ClassNotFoundException exception) {
                Outputer.printerr("Произошла ошибка при чтении полученных данных!");
            } catch (IOException exception) {
                Outputer.printerr("Соединение с сервером разорвано!");
                try {
                    reconnectionAttempts++;
                    connectToServer();
                } catch (ConnectionErrorException | NotInDeclaredLimitsException reconnectionException) {
                    if (requestToServer.getInputLine().equals("exit"))
                        Outputer.println("Команда не будет зарегистрирована на сервере.");
                    else Outputer.println("Попробуйте повторить команду позднее.");
                }
            } catch (Exception e) {
                Outputer.printerr("Непредвиденная ошибка!");
                e.printStackTrace();
            }
        } while (!requestToServer.getInputLine().equals("exit"));
        return false;
    }
}