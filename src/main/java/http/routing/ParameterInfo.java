package http.routing;

import java.lang.reflect.Type;

/**
 * Represents information about a method parameter, including its name and type.
 * This class is used to encapsulate metadata for method parameters,
 * enabling dynamic invocation and type resolution at runtime.
 *
 * @param name The name of the parameter.
 * @param type The type of the parameter, represented as a {@link Type}.
 */
public record ParameterInfo(String name, Type type) {
}
