/**
 * Project Group 23
 */

package automail;

import exceptions.FragileItemBrokenException;
import strategies.Automail;
import strategies.IMailPool;


/**
 * The type Careful Robots.
 */
public class CarefulRobot extends Robot{

    private boolean delaying = true;

    /**
     * maximum fragile item a careful Robot can carry
     */
    public final int MAX_FRAGILE_ITEMS = 1;

    /**
     * maximum items a careful Robot can carry
     */
    public final int CAREFUL_ROBOT_MAX_ITEMS = 3;

    /**
     * Initiates the Robot's location at the start to be at the mailroom
     * also set it to be waiting for mail.
     *
     * @param delivery governs the final delivery
     * @param mailPool is the source of mail items
     */
    public CarefulRobot(IMailDelivery delivery, IMailPool mailPool) {
        super(delivery, mailPool, Automail.STRONG);
        setMaxItems(CAREFUL_ROBOT_MAX_ITEMS);
    }

    @Override
    public void moveTowards(int destination) throws FragileItemBrokenException {
        // A Careful Robot moves slower
        if(delaying){
            delaying = false;
        } else {
            super.moveTowards(destination);
            delaying = true;
        }
    }
}
