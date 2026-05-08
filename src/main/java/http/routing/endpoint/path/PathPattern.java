package http.routing.endpoint.path;

import java.util.LinkedHashMap;
import java.util.Map;

public class PathPattern {
    private final String    raw;
    private final Segment[] segments;

    public PathPattern(String path) {
        this.raw      = path;
        this.segments = parse(path);
    }

    private static Segment[] parse(String path) {
        String   stripped = path.replaceAll("^/+|/+$", "");
        if (stripped.isEmpty()) return new Segment[0];

        String[] parts    = stripped.split("/");
        Segment[] parsedSegments    = new Segment[parts.length];
        for (int i = 0; i < parts.length; i++) {
            parsedSegments[i] = Segment.parse(parts[i]);
        }
        return parsedSegments;
    }

    public Map<String, String> match(String requestPath) {
        String stripped = requestPath.replaceAll("^/+|/+$", "")
                .split("\\?")[0];
        String[] parts  = stripped.isEmpty() ? new String[0] : stripped.split("/");

        if (parts.length != segments.length) return null;

        Map<String, String> vars = new LinkedHashMap<>();
        for (int i = 0; i < segments.length; i++) {
            Segment seg = segments[i];
            switch (seg.getType()) {
                case LITERAL  -> { if (!seg.getValue().equals(parts[i])) return null; }
                case VARIABLE -> vars.put(seg.getValue(), parts[i]);
                case WILDCARD -> { /* match anything, capture nothing */ }
            }
        }
        return vars;
    }



    public boolean matchesPath(String requestPath) {
        return match(requestPath) != null;
    }


    public int specificity() {
        int score = 0;
        for (Segment seg : segments) {
            score += switch (seg.getType()) {
                case LITERAL  -> 10;
                case VARIABLE -> 5;
                case WILDCARD -> 1;
            };
        }
        return score;
    }

    public String getRaw() { return raw; }

    @Override
    public String toString() { return raw; }
}
