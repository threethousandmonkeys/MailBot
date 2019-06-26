/**
 * Project Group 23
 */

package automail;

import exceptions.TubeFullException;

import java.util.Stack;

/**
 * The storage tube carried by the robots.
 */
public class StorageTube {

    /**
     * standard maximum tube size
     */
    private int maximumTubeSize = 4;
    private int fragileCount = 0;
    private Stack<MailItem> tube;

    /**
     * Constructor for the storage tube
     */
    public StorageTube(){
        this.tube = new Stack<MailItem>();
    }

    /**
     * Constructor for the storage tube based on the desired number of capacity
     * @param capacity the desired number of capacity
     */
    public StorageTube(int capacity){
        this.tube = new Stack<>();
        maximumTubeSize = capacity;
    }
    /**
     * @return if the storage tube is full
     */
    public boolean isFull(){
        return tube.size() == maximumTubeSize;
    }

    /**
     * @return if the storage tube is empty
     */
    public boolean isEmpty(){
        return tube.isEmpty();
    }
    
    /**
     * @return the first item in the storage tube (without removing it)
     */
    public MailItem peek() {
    	return tube.peek();
    }

    /**
     * Add an item to the tube
     * @param item The item being added
     * @throws TubeFullException thrown if an item is added which
     * exceeds the capacity
     */
    public void addItem(MailItem item) throws TubeFullException {
        if (item.getFragile()) fragileCount++;
        if(tube.size() < maximumTubeSize){
        	tube.add(item);
        } else {
            throw new TubeFullException();
        }
    }

    /** @return the size of the tube **/
    public int getSize(){
    	return tube.size();
    }
    
    /** 
     * @return the first item in the storage tube (after removing it)
     */
    public MailItem pop(){
        return tube.pop();
    }

    public void setMaximumTubeSize(int maximumTubeSize) {
        this.maximumTubeSize = maximumTubeSize;
    }

    public int getMaximumTubeSize() {
        return maximumTubeSize;
    }

    public int getFragileCount() {
        return fragileCount;
    }
}
