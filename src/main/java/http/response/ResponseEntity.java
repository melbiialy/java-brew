package http.response;

public class ResponseEntity <T>{
    private T body;
    private int status;
    public ResponseEntity(T body,int status) {
        this.body = body;
        this.status = status;
    }
    public T getBody() {
        return body;
    }
    public int getStatus() {
        return status;
    }
    public ResponseEntity<T> withStatus(int status) {
        this.status = status;
        return this;
    }
    public ResponseEntity<T> withBody(T body) {
        this.body = body;
        return this;
    }

}
