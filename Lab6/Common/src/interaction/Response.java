package interaction;

import server.utility.CollectionManager;

import java.io.Serializable;

/**
 * Class for get response value.
 */
public class Response implements Serializable {
    private CollectionManager collectionManager;
    private ResponseCode responseCode;

    private String responseBody;

    public Response(CollectionManager collectionManager, ResponseCode code, String responseBody) {
        this.collectionManager = collectionManager;
        this.responseCode = code;
        this.responseBody = responseBody;
    }

    public Response(ResponseCode code) {

    }

    /**
     * @return Response сode.
     */
    public ResponseCode getResponseCode() {
        return responseCode;
    }

    /**
     * @return Response body.
     */
    public String getResponseBody() {
        return responseBody;
    }

    /**
     * @return Response collection
     */
    public CollectionManager getCollectionManager() {
        return collectionManager;
    }

    @Override
    public String toString() {
        return "Response[" + responseCode + ", " + responseBody + "]";
    }
}