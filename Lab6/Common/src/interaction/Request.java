package interaction;

import data.Routes;

import java.io.Serializable;

/**
 * Class for get request value.
 */
public class Request implements Serializable {
    private Routes collectionManager;
    private String inputLine;
    private Serializable commandObjectArgument;

    public Request(Routes collectionManager, String inputLine, Serializable commandObjectArgument) {
        this.collectionManager = collectionManager;
        this.inputLine = inputLine;
        this.commandObjectArgument = commandObjectArgument;
    }

    public Request(Routes collectionManager, String inputLine) {
        this(collectionManager, inputLine, null);
    }

    public Request(Routes collectionManager) {
        this(collectionManager,"");
    }

    public Routes getCollectionManager() {
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