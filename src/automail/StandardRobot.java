/**
 * Project Group 23
 */

package automail;

import strategies.Automail;
import strategies.IMailPool;

/**
 * The type Standard Robot.
 */
public class StandardRobot extends Robot{
    /**
     * Initiates the Robot's location at the start to be at the mailroom
     * also set it to be waiting for mail.
     *
     * @param delivery governs the final delivery
     * @param mailPool is the source of mail items
     */
    public StandardRobot(IMailDelivery delivery, IMailPool mailPool) {
        // standard Robot can handle any weight
        super(delivery, mailPool, Automail.STRONG);
    }
}
