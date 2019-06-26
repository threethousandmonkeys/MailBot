package automail;


/**
 * The type Clock.
 */
public class Clock {
	
	/** Represents the current time **/
    private static int Time = 0;

    /**
     * The threshold for the latest time for mail to arrive
     */
    public static int LAST_DELIVERY_TIME;

    /**
     * Time int.
     *
     * @return the int
     */
    public static int Time() {
    	return Time;
    }

    /**
     * Tick.
     */
    public static void Tick() {
    	Time++;
    }
}
