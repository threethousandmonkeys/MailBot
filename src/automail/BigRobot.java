/**
 * Project Group 23
 */

package automail;

import strategies.Automail;
import strategies.IMailPool;


/**
 * The type Big robot.
 */
public class BigRobot extends Robot{
    /**
     * Maximum items a big robot can carry
     */
    public static final int BIG_ROBOT_MAX_ITEMS = 6;

    /**
     * Initiates the robot's location at the start to be at the mailroom
     * also set it to be waiting for mail.
     *
     * @param delivery governs the final delivery
     * @param mailPool is the source of mail items
     */
    public BigRobot(IMailDelivery delivery, IMailPool mailPool) {
        // Big Robot can carry more items and items of any weight
        super(delivery, mailPool, Automail.STRONG);
        setMaxItems(BIG_ROBOT_MAX_ITEMS);
    }
}
