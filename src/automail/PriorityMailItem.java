package automail;


/**
 * The type Priority mail item.
 */
public class PriorityMailItem extends MailItem{
	
	/** The priority of the mail item from 1 low to 100 high */
    private final int PRIORITY_LEVEL;

    /**
     * Instantiates a new Priority mail item.
     *
     * @param dest_floor     the destination floor
     * @param arrival_time   the arrival time
     * @param weight         the weight
     * @param fragile        the fragile
     * @param priority_level the priority level
     */
    public PriorityMailItem(int dest_floor, int arrival_time, int weight,
                            boolean fragile, int priority_level) {
		super(dest_floor, arrival_time, weight, fragile);
        this.PRIORITY_LEVEL = priority_level;
	}

    /**
     * Get priority level int.
     *
     * @return the priority level of a mail item
     */
    public int getPriorityLevel(){
       return PRIORITY_LEVEL;
   }
   
   @Override
   public String toString(){
       return super.toString() + String.format(" | Priority: %3d",
               PRIORITY_LEVEL);
   }

}
