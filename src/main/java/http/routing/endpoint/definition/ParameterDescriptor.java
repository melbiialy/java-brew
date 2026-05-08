package http.routing.endpoint.definition;

public record ParameterDescriptor(int index, Class<?> type, ParamSource source, String name, boolean required) {
}
