package interaction;

import data.Routes;

import java.io.Serializable;

/**
 * Class for get response value.
 */
public class Response implements Serializable {
    private Routes collection;
    private ResponseCode responseCode;

    private String responseBody;

    public Response(Routes collection, ResponseCode code, String responseBody) {
        this.collection = collection;
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
    public Routes getCollection() {
        return collection;
    }

    @Override
    public String toString() {
        return "Response[" + responseCode + ", " + responseBody + "]";
    }
}