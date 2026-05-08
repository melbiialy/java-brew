package http.enums;

public enum ContentType {
    APPLICATION_JSON("application/json");
    public final String value;
    ContentType(String value) {
        this.value = value;
    }
}
