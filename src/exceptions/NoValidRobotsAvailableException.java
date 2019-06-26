/**
 * Project Group 23
 */

package exceptions;

/**
 * An exception that is thrown if there are special types of mails in the
 * mail pool that can only be delivered by certain types of robot and that
 * robot does not exist
 */
public class NoValidRobotsAvailableException extends Throwable{
    /**
     * show what robot that does not exist
     * @param robot the name of the robot that does not exist
     */
    public NoValidRobotsAvailableException(String robot) {
        super("There are no " + robot + " robots available to deliver all " +
                "mails");
    }
}
