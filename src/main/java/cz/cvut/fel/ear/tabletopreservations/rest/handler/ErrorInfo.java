package cz.cvut.fel.ear.tabletopreservations.rest.handler;


public class ErrorInfo {

    private String message;
    private String uri;

    public ErrorInfo() {
    }

    public ErrorInfo(String message, String requestUri) {
        this.message = message;
        this.uri = requestUri;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "ErrorInfo{" + uri + ", message = " + message + "}";
    }
}