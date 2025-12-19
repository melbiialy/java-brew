package http.routing;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Represents the definition of an endpoint, encapsulating information about
 * its associated controller, method, and parameters.
 *
 * This class is used to map HTTP routing logic to specific controller methods
 * and provides details essential for invoking the method dynamically during
 * runtime.
 *
 * Components:
 * - The `controller` represents the instance of the class containing the method.
 * - The `method` represents the target method within the controller to be invoked.
 * - The `parameters` represent the list of parameter details required by the method,
 *   each described by a {@code ParameterInfo} object which provides both parameter
 *   name and type.
 */
public record EndpointDefinition(Object controller, Method method, List<ParameterInfo> parameters) {
}
