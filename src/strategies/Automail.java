/**
 * Project Group 23
 */

package strategies;

import automail.*;
import exceptions.InvalidRobotTypeException;

import java.util.List;

/**
 * The type Automail creates the requested robots.
 */
public class Automail {
	      
    public final DelivererBehaviour[] ROBOTS;
    public final IMailPool MAIL_POOL;
    // Can't handle packages heavier than the weak robots limit
    public static final boolean WEAK= false;

    // Can handle packages of any weight
    public static final boolean STRONG = true;

    /**
     * Instantiates a new Automail.
     *
     * @param delivery     the delivery
     * @param robotsToMake the robots to make
     * @throws InvalidRobotTypeException the invalid robot type exception
     */
    public Automail(IMailDelivery delivery, String mailPoolName,
                    List<Simulation.RobotType> robotsToMake) throws
            InvalidRobotTypeException, ClassNotFoundException,
            IllegalAccessException, InstantiationException {

        /**
         * Created the mail pool
         */
        MAIL_POOL = (IMailPool) Class.forName(mailPoolName).
                newInstance();

    	/**
         * Initialize robots
         */
    	ROBOTS = new DelivererBehaviour[robotsToMake.size()];
        for (int i = 0; i < robotsToMake.size(); i++) {
            ROBOTS[i] = makeRobot(robotsToMake.get(i), delivery);
        }

    }

    /**
     * Make the requested deliverers.
     *
     * @param robotType the robots type to make
     * @param delivery  the delivery report
     * @return the deliverer
     * @throws InvalidRobotTypeException it is thrown if a requested robots does
     * not exist
     */
    public DelivererBehaviour makeRobot(Simulation.RobotType robotType,
                                        IMailDelivery delivery)
            throws InvalidRobotTypeException {

        // create robots based on the designated type
        switch(robotType){
            case Big:
                return new BigRobot(delivery, MAIL_POOL);
            case Weak:
                return new WeakRobot(delivery, MAIL_POOL);
            case Careful:
                return new CarefulRobot(delivery, MAIL_POOL);
            case Standard:
                return new StandardRobot(delivery, MAIL_POOL);
            default:
                throw new InvalidRobotTypeException();
        }
    }
}
