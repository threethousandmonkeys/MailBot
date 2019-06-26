/**
 * Project Group 23
 */

package strategies;

import java.util.LinkedList;
import java.util.Comparator;
import java.util.ListIterator;

import automail.*;
import exceptions.NoValidRobotsAvailableException;
import exceptions.TubeFullException;
import exceptions.FragileItemBrokenException;

/**
 * The strategy implemented to decide the next sets of items for a robot to
 * deliver
 */
public class MyMailPool implements IMailPool {
	private class Item {
		/**
		 * The Priority level of an item.
		 */
		int priority;
		/**
		 * The Destination of the item.
		 */
		int destination;
		/**
		 * Whether the item is heavy or not.
		 */
		boolean heavy;
		/**
		 * Whether the item is fragile or not
		 */
		boolean fragile;
		/**
		 * The Mail item.
		 */
		MailItem mailItem;
		// Use stable sort to keep arrival time relative positions

		/**
		 * Instantiates a new Item.
		 *
		 * @param mailItem the mail item
		 */
		public Item(MailItem mailItem) {
			priority = (mailItem instanceof PriorityMailItem) ?
					((PriorityMailItem) mailItem).getPriorityLevel() : 1;
			heavy = mailItem.getWeight() >= WeakRobot.WEAK_ROBOT_MAX_WEIGHT;
			fragile = mailItem.getFragile();
			destination = mailItem.getDestFloor();
			this.mailItem = mailItem;
		}
	}

	private boolean carefulRobotExists = false;
	private boolean strongRobotExists = false;

	/**
	 * The type Item comparator.
	 */
	public class ItemComparator implements Comparator<Item> {
		@Override
		public int compare(Item i1, Item i2) {
			int order = 0;
			if (i1.priority < i2.priority) {
				order = 1;
			} else if (i1.priority > i2.priority) {
				order = -1;
			} else if (i1.destination < i2.destination) {
				order = 1;
			} else if (i1.destination > i2.destination) {
				order = -1;
			}
			return order;
		}
	}
	
	private LinkedList<Item> pool;
	private LinkedList<Robot> robots;
	private int lightCount;

	/**
	 * Instantiates a new My mail pool.
	 */
	public MyMailPool(){
		// Start empty
		pool = new LinkedList<Item>();
		lightCount = 0;
		robots = new LinkedList<Robot>();
	}

	public void addToPool(MailItem mailItem)
			throws NoValidRobotsAvailableException {
		Item item = new Item(mailItem);
        if (mailItem.getFragile() && !carefulRobotExists){
        	// checking if there is careful robot to deliver fragile items
        	throw new NoValidRobotsAvailableException("Careful");
        }
        pool.add(item);
        if (!item.heavy) {
        	lightCount++;
		} else if (item.heavy && !strongRobotExists){
        	// checking if there is strong robot to deliver heavy items
        	throw new NoValidRobotsAvailableException("Strong");
		}
        pool.sort(new ItemComparator());
	}
	
	@Override
	public void step() throws FragileItemBrokenException {
		for (Robot robot: (Iterable<Robot>) robots::iterator) {
			fillStorageTube(robot);
		}
	}
	
	private void fillStorageTube(Robot robot) throws FragileItemBrokenException{
		StorageTube tube = robot.getTube();
		StorageTube temp = new StorageTube(tube.getMaximumTubeSize());

		// Get as many items as available or as fit
		try {
			// To iterate through the pool
			ListIterator<Item> i = pool.listIterator();

			while(temp.getSize() < robot.getMaxItems() && !pool.isEmpty() &&
					i.hasNext()){
				Item item = i.next();

				/**
				 * if item is fragile and the robot is not a certain type
				 * that can handle it, or if the robot can not carry anymore
				 * fragile items, it will iterate to the next mail.
				 */
				if(item.fragile && (!(robot instanceof CarefulRobot) ||
						temp.getFragileCount() >=
						((CarefulRobot) robot).MAX_FRAGILE_ITEMS)) continue;

				// adding certain mails to certain robot
				if (robot.isStrong()){
					temp.addItem(item.mailItem);
					if (!item.heavy) lightCount--;
					i.remove();
				} else {
					if (lightCount <= 0) break;
					if (!item.heavy) {
						temp.addItem(item.mailItem);
						i.remove();
						lightCount--;
					}
				}
			}

			// removing from temporary tube and adding to robot's tube
			if (temp.getSize() > 0) {
				while (!temp.isEmpty()) tube.addItem(temp.pop());
				robot.dispatch();
			}

		}
		catch(TubeFullException e){
			e.printStackTrace();
		}
	}

	@Override
	public void registerWaiting(Robot robot) {

		// checking if necessary robots types exists in the lineup
		if (robot instanceof CarefulRobot) carefulRobotExists = true;
		if (robot.isStrong()) {
			robots.add(robot);
			strongRobotExists = true;
		} else {
			/**
			 *  weak robots last as want more efficient delivery with highest
			 *  priorities
			 */
			robots.addLast(robot);
		}
	}

	@Override
	public void deregisterWaiting(Robot robot) {
		robots.remove(robot);
	}

}
