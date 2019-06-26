/**
 * Project Group 23
 */

package automail;

import strategies.Automail;
import strategies.IMailPool;


/**
 * The type Weak robot.
 */
public class WeakRobot extends Robot {

    /**
     * The maximum weight a weak robot can carry.
     */
    public static final int WEAK_ROBOT_MAX_WEIGHT = 2000;

    /**
     * Initiates the Robot's location at the start to be at the mailroom
     * also set it to be waiting for mail.
     *
     * @param delivery governs the final delivery
     * @param mailPool is the source of mail items
     */
    public WeakRobot(IMailDelivery delivery, IMailPool mailPool) {
        // weak robots have limits in its maximum weight
        super(delivery, mailPool, Automail.WEAK);
    }
}
