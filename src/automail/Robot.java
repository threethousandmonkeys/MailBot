/**
 * Project Group 23
 */

package automail;

import exceptions.ExcessiveDeliveryException;
import exceptions.ItemTooHeavyException;
import exceptions.FragileItemBrokenException;
import exceptions.NoValidRobotsAvailableException;
import strategies.IMailPool;
import java.util.Map;
import java.util.TreeMap;

/**
 * The robot delivers mail!
 */
public abstract class Robot implements DelivererBehaviour {

    /**
     * The Tube for a robot.
     */
	StorageTube tube;

    /**
     * The Delivery report.
     */
    IMailDelivery delivery;

    /**
     * The Id of a robot.
     */
    protected final String id;

    /**
     * Possible states the robot can be in
     */
    public enum RobotState { DELIVERING, WAITING, RETURNING }

    /**
     * the current state of a robot
     */
    public RobotState currentState;

    /**
     * the current location of a robot in a building.
     */
    private int currentFloor;

    /**
     * the current destination for a robot.
     */
    private int destinationFloor;

    /**
     * the strategy used for delivery.
     */
    private IMailPool mailPool;

    /**
     * whether the robot has receive a dispatch or not
     */
    private boolean receivedDispatch;

    /**
     * whether the robot is a strong robot or a weak robot
     */
    private boolean strong;

    /**
     * the standard maximum items a robot can carry
     */
    private int maxItems = 4;

    /**
     * items to deliver
     */
    private MailItem deliveryItem;

    /**
     * counting how many items have a robot delivered
     */
    private int deliveryCounter;


    /**
     * Initiates the robot's location at the start to be at the mailroom
     * also set it to be waiting for mail.
     *
     * @param delivery governs the final delivery
     * @param mailPool is the source of mail items
     * @param strong   is whether the robot can carry heavy items
     */
    public Robot(IMailDelivery delivery, IMailPool mailPool,
                     boolean strong){
    	id = "R" + hashCode();
    	currentState = RobotState.RETURNING;
        currentFloor = Building.MAILROOM_LOCATION;
        tube = new StorageTube();
        this.delivery = delivery;
        this.mailPool = mailPool;
        this.receivedDispatch = false;
        this.strong = strong;
        this.deliveryCounter = 0;
    }

    /**
     * changing the status of a robot to being dispatch to deliver items
     */
    public void dispatch() {
    	receivedDispatch = true;
    }

    /**
     * Is strong boolean.
     *
     * @return the boolean
     */
    public boolean isStrong() {
    	return strong;
    }

    /**
     * This is called on every time step
     * @throws ExcessiveDeliveryException if robot delivers more than the
     * capacity of the tube without refilling
     */
    public void step() throws ExcessiveDeliveryException, ItemTooHeavyException,
            FragileItemBrokenException, NoValidRobotsAvailableException {
    	switch(currentState) {
    		/**
             * This state is triggered when the robot is returning to the
             *  mailroom after a delivery
             */
    		case RETURNING:
    			/**
                 * If its current position is at the mailroom, then the robot
                 * should change state
                 */
                if(currentFloor == Building.MAILROOM_LOCATION){
                	while(!tube.isEmpty()) {
                		MailItem mailItem = tube.pop();
                		mailPool.addToPool(mailItem);
                        System.out.printf("T: %3d > old addToPool [%s]%n",
                                Clock.Time(), mailItem.toString());
                	}
        			/** Tell the sorter the robot is ready */
        			mailPool.registerWaiting(this);
                	changeState(RobotState.WAITING);
                } else {
                	/**
                     * If the robot is not at the mailroom floor yet, then move
                     * towards it!
                     */
                    moveTowards(Building.MAILROOM_LOCATION);
                	break;
                }
    		case WAITING:
                /**
                 * If the StorageTube is ready and the Robot is waiting in the
                 * mailroom then start the delivery
                 */
                if(!tube.isEmpty() && receivedDispatch){
                	receivedDispatch = false;
                	deliveryCounter = 0; // reset delivery counter
        			setRoute();
        			mailPool.deregisterWaiting(this);
                	changeState(RobotState.DELIVERING);
                }

                break;
    		case DELIVERING:
    		    // reaching the destination floor
    			if(currentFloor == destinationFloor){
                    /** Delivery complete, report this to the simulator! */
                    delivery.deliver(deliveryItem);
                    deliveryCounter++;

                    // Implies a simulation bug
                    if(deliveryCounter > maxItems){
                    	throw new ExcessiveDeliveryException();
                    }
                    /**
                     * Check if want to return, i.e. if there are no more items
                     * in the tube
                     */
                    if(tube.isEmpty()){
                    	changeState(RobotState.RETURNING);
                    }
                    else{
                        /**
                         * If there are more items, set the robot's route to the
                         * location to deliver the item
                         */
                        setRoute();
                        changeState(RobotState.DELIVERING);
                    }
    			} else {
	        		/** The robot is not at the destination yet, move towards
                     * it!
                     */
	                moveTowards(destinationFloor);
    			}
                break;
    	}
    }

    /**
     * Sets the route for the robot
     */
    public void setRoute() throws ItemTooHeavyException{
        /** Pop the item from the StorageUnit */
        deliveryItem = tube.pop();
        if (!strong && deliveryItem.weight > WeakRobot.WEAK_ROBOT_MAX_WEIGHT)
            throw new ItemTooHeavyException();
        /** Set the destination floor */
        destinationFloor = deliveryItem.getDestFloor();
    }

    /**
     * Generic function that moves the robot towards the destination
     * @param destination the floor towards which the robot is moving
     */
    public void moveTowards(int destination) throws FragileItemBrokenException {


        // preventing robots that are not certain types to carry fragile items
        if (!(this instanceof CarefulRobot) && ( deliveryItem != null &&
                deliveryItem.getFragile() ||
                !tube.isEmpty() && tube.peek().getFragile()))
            throw new FragileItemBrokenException();
        if(currentFloor < destination){
            currentFloor++;
        }
        else{
            currentFloor--;
        }
    }
    
    private String getIdTube() {
    	return String.format("%s(%1d/%1d)", id, tube.getSize(), getTube().getMaximumTubeSize());
    }

    /**
     * Prints out the change in state
     *
     * @param nextState the state to which the robot is transitioning
     */
    public void changeState(RobotState nextState){
    	if (currentState != nextState) {
            System.out.printf("T: %3d > %7s changed from %s to %s%n", Clock.Time(), getIdTube(), currentState, nextState);
    	}
    	currentState = nextState;
    	if(nextState == RobotState.DELIVERING){
            System.out.printf("T: %3d > %7s-> [%s]%n", Clock.Time(), getIdTube(), deliveryItem.toString());
    	}
    }

    /**
     * Sets current floor.
     *
     * @param currentFloor the current floor
     */
    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    /**
     * Gets tube.
     *
     * @return the tube
     */
    public StorageTube getTube() {
		return tube;
	}
    
	static private int count = 0;
	static private Map<Integer, Integer> hashMap = new TreeMap<Integer, Integer>();

    /**
     * Gets mail pool.
     *
     * @return the mail pool
     */
    public IMailPool getMailPool() {
        return mailPool;
    }

    /**
     * Gets delivery.
     *
     * @return the delivery
     */
    public IMailDelivery getDelivery() {
        return delivery;
    }

    /**
     * Gets current floor.
     *
     * @return the current floor
     */
    public int getCurrentFloor() {
        return currentFloor;
    }

    /**
     * Gets destination floor.
     *
     * @return the destination floor
     */
    public int getDestinationFloor() {
        return destinationFloor;
    }

    /**
     * Is received dispatch boolean.
     *
     * @return the boolean
     */
    public boolean isReceivedDispatch() {
        return receivedDispatch;
    }

    /**
     * Gets delivery item.
     *
     * @return the delivery item
     */
    public MailItem getDeliveryItem() {
        return deliveryItem;
    }

    /**
     * Gets delivery counter.
     *
     * @return the delivery counter
     */
    public int getDeliveryCounter() {
        return deliveryCounter;
    }

    /**
     * Sets received dispatch.
     *
     * @param receivedDispatch the received dispatch
     */
    public void setReceivedDispatch(boolean receivedDispatch) {
        this.receivedDispatch = receivedDispatch;
    }

    /**
     * Sets delivery counter.
     *
     * @param deliveryCounter the delivery counter
     */
    public void setDeliveryCounter(int deliveryCounter) {
        this.deliveryCounter = deliveryCounter;
    }

    /**
     * Gets max items.
     *
     * @return the max items
     */
    public int getMaxItems() {
        return maxItems;
    }

    /**
     * Sets max items a robot can carry.
     *
     * @param maxItems the max items
     */
    public void setMaxItems(int maxItems) {
        this.maxItems = maxItems;
        this.tube.setMaximumTubeSize(maxItems);
    }

    @Override
	public int hashCode() {
		Integer hash0 = super.hashCode();
		Integer hash = hashMap.get(hash0);
		if (hash == null) { hash = count++; hashMap.put(hash0, hash); }
		return hash;
	}
}
