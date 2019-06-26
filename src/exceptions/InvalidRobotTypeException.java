/**
 * Project Group 23
 */

package exceptions;

/**
 * An exception that is thrown when a type of robot that does not exist is
 * requested
 */
public class InvalidRobotTypeException extends Throwable {
    public InvalidRobotTypeException() {
        super("Invalid Robot Type");
    }
}
