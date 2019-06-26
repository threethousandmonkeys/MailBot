/**
 * Project Group 23
 */

package automail;

import exceptions.ExcessiveDeliveryException;
import exceptions.FragileItemBrokenException;
import exceptions.ItemTooHeavyException;
import exceptions.NoValidRobotsAvailableException;

/**
 * The interface Deliverer behaviour.
 */
public interface DelivererBehaviour {

    /**
     * dispatching a deliverer to deliver the items
     */
    void dispatch();

    /**
     * The steps of a deliverer based on the state that the deliverer have
     *
     * @throws ExcessiveDeliveryException it is thrown if the  deliverer
     * delivers more item that it should
     *
     * @throws ItemTooHeavyException it is thrown if the deliverer carries an
     * item that is heavier than its maximum weight it can handle
     *
     * @throws FragileItemBrokenException it is thrown if the deliverer can
     * not handle fragile item and it assigned to deliver one
     *
     * @throws NoValidRobotsAvailableException it is thrown if there is no
     * robots that can deliver special items that exist in the mail pool
     */
    void step() throws ExcessiveDeliveryException, ItemTooHeavyException,
            FragileItemBrokenException, NoValidRobotsAvailableException;

    /**
     * Setting the route of a deliverer
     *
     * @throws ItemTooHeavyException it is thrown if the deliverer carries an
     * item that is heavier than its maximum weight it can handle
     */
    void setRoute() throws ItemTooHeavyException;

    /**
     * For the deliverers to move towards to a certain floor on in the building
     *
     * @param destination the destination floor
     * @throws FragileItemBrokenException it is thrown if the deliverer can
     * not handle fragile item and it assigned to deliver one
     */
    void moveTowards(int destination) throws FragileItemBrokenException;

}
