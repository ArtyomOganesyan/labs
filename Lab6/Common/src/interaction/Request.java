package interaction;

import server.utility.CollectionManager;

import java.io.Serializable;

/**
 * Class for get request value.
 */
public class Request implements Serializable {
    private CollectionManager collectionManager;
    private String inputLine;
    private Serializable commandObjectArgument;

    public Request(CollectionManager collectionManager, String inputLine, Serializable commandObjectArgument) {
        this.collectionManager = collectionManager;
        this.inputLine = inputLine;
        this.commandObjectArgument = commandObjectArgument;
    }

    public Request(CollectionManager collectionManager, String inputLine) {
        this(collectionManager, inputLine, null);
    }

    public Request(CollectionManager collectionManager) {
        this(collectionManager,"");
    }

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }

    /**
     * @return Command name.
     */
    public String getInputLine() {
        return inputLine;
    }

    /**
     * @return Command object argument.
     */
    public Object getCommandObjectArgument() {
        return commandObjectArgument;
    }

    /**
     * @return Is this request empty.
     */
    public boolean isEmpty() {
        return inputLine.isEmpty()  && commandObjectArgument == null;
    }

    @Override
    public String toString() {
        return "Request[" + inputLine  + ", " + commandObjectArgument + "]";
    }
}