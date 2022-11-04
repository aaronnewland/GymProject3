package gymmanager.gymproject3;
/**
 * Holds enumeration data for the times that the fitness classes are held at.
 * @author Aaron Newland, Dylan Pina
 */
public enum Time {
    MORNING(9, 30),
    AFTERNOON(14, 00),
    EVENING(18, 30);

    private final int hour;
    private final int minutes;

    /**
     * Creates new Time object using provided int values.
     * @param hour hour given in int.
     * @param minutes minutes given in int.
     */
    Time(int hour, int minutes) {
        this.hour = hour;
        this.minutes = minutes;
    }

    /**
     * Provides a string representation of a Time object.
     * @return String of Time object.
     */
    @Override
    public String toString() {
        return hour + ":" + String.format("%02d", minutes);
    }
}
