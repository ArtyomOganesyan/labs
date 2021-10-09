package mainPart;

import java.io.IOException;
import java.io.Serializable;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class Computer {

    private final ServerPart serverPart;
    private final ServerMaker serverMaker;
    private static final int numberOfForks = 4;

    public Computer(ServerPart serverPart, ServerMaker serverMaker) {
        this.serverPart = serverPart;
        this.serverMaker = serverMaker;
    }
    public void computingRead() {

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        while (serverPart.isIsClientConnected()) {
            final int[] currentNumberOfForks = {0};
            forkJoinPool.invoke(new RecursiveAction() {
                @Override
                protected void compute() {
                    try {
                        SocketChannel socketChannel;
                        socketChannel = serverMaker.acceptConnection();
                        UserData userData = (UserData) serverMaker.deserialize(serverMaker.readData(socketChannel));
                        if (!userData.getExistedAccount()) {
                            try {
                                serverPart.getDataBaseWorker().createNewAccountInDB(userData.getLogin(), userData.getPassword());
                                serverMaker.getRequests().put(userData.getLogin(), "Новый аккаунт был успешно создан");
                            } catch (SQLException | NoSuchAlgorithmException e) {
                                e.printStackTrace();
                                serverMaker.getRequests().put(userData.getLogin(), "Не удалось создать новый аккаунт, либо аккаунт с данным именем пользователя уже существует, повторите позже.");
                            }
                        } else {
                            try {
                                if (serverMaker.usersChannels.get(userData.getLogin()) != null)  {
                                    userData.setLogin("alreadyLoggedInUser");
                                    serverMaker.getRequests().put(userData.getLogin(), "Кто-то с данного аккаунта уже залогинен");
                                }
                                else {
                                    serverPart.getDataBaseWorker().checkAccountInDB(userData.getLogin(), userData.getPassword());
                                    if (userData.getData() != null)
                                        serverMaker.getResponses().put(userData.getData(), userData.getLogin());
                                    else serverMaker.getRequests().put(userData.getLogin(), "Вход успешно выполнен");
                                }

                            } catch (SQLException | NoSuchAlgorithmException e) {
                                serverMaker.getRequests().put(userData.getLogin(), e.getMessage());
                            }
                        }
                        serverMaker.usersChannels.put(userData.getLogin(), socketChannel);
                        if (currentNumberOfForks[0] == numberOfForks) {
                            currentNumberOfForks[0]++;
                            fork();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void computingWrite() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        while (serverPart.isIsClientConnected()) {
            forkJoinPool.invoke(new RecursiveAction() {
                @Override
                protected void compute() {
                    for (Iterator<String> it = serverMaker.getRequests().keySet().stream().iterator(); it.hasNext(); ) {
                        String user = it.next();
                        Serializable serializable = serverMaker.getRequests().get(user);
                        serverMaker.getRequests().remove(user);
                        fork();
                        serverMaker.writeData(serverMaker.serialize(serializable), serverMaker.usersChannels.get(user));
                        serverMaker.usersChannels.remove(user);
                    }
                }
            });
        }
    }
}
