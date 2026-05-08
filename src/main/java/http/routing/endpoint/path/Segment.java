package http.routing.endpoint.path;

public class Segment {
    private final SegmentType type;
    private final String      value;

    private Segment(SegmentType type, String value) {
        this.type  = type;
        this.value = value;
    }

    public static Segment parse(String raw) {
        if (raw.equals("*"))          return new Segment(SegmentType.WILDCARD,  "*");
        if (raw.startsWith(":"))      return new Segment(SegmentType.VARIABLE,  raw.substring(1));
        return                               new Segment(SegmentType.LITERAL,   raw);
    }

    public SegmentType getType()  { return type; }
    public String      getValue() { return value; }
}
