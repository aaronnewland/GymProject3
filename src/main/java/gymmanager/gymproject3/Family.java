package gymmanager.gymproject3;

/**
 * Used to create new Family objects that hold information for members with family tier membership.
 * These objects can then be compared.
 * Holds all member information, and number of guest passes. For family the max number of guest passes is 1.
 * @author Aaron Newland, Dylan Pina
 */
public class Family extends Member implements Comparable<Member> {
    private int guestPasses;

    private final double initFee = 29.99;
    private final double monthlyFee = 59.99;
    private final double quarterlyFee = monthlyFee * 3;

    /**
     * Default constructor for family tier gym member. All member fields are set to null, and guest passes is set to 1.
     */
    public Family() {
        super();
        guestPasses = 1;
    }

    /**
     * Creates new Family object with given values. Set guest passes to 1.
     * @param fname member first name.
     * @param lname member last name.
     * @param dob member date of birth.
     * @param location member gym location.
     */
    public Family(String fname, String lname, Date dob, Location location) {
        super(fname, lname, dob, location);
        guestPasses = 1;
    }

    /**
     * Creates new Family object with given values. Set guest passes to 1.
     * @param fname member first name.
     * @param lname member last name.
     * @param dob member date of birth.
     * @param expire member membership expiration date.
     * @param location member gym location.
     */
    public Family(String fname, String lname, Date dob, Date expire, Location location) {
        super(fname, lname, dob, expire, location);
        guestPasses = 1;
    }

    /**
     * Gets Family number of guest passes.
     * @return number of guest passes.
     */
    public int getGuestPasses() {
        return guestPasses;
    }

    /**
     * Sets Family number of guest passes.
     * @param guestPasses number of guest passes.
     */
    public void setGuestPasses(int guestPasses) {
        this.guestPasses = guestPasses;
    }

    /**
     * Increments number of guest passes by one.
     */
    public void incrementGuestPass() {
        guestPasses++;
    }

    /**
     * Decrements number of guest passes by one.
     */
    public void decrementGuestPass() {
        guestPasses--;
    }

    /**
     * Determines if Family has any guest passes.
     * @return true if Family does not have any guest passes left, false otherwise.
     */
    public boolean hasGuestPass() {
        return guestPasses != 0;
    }

    /**
     * Calculates membership fee owed by Family member.
     * @return dollar amount of money owed in membership fee.
     */
    @Override
    public double membershipFee() {
        return initFee + quarterlyFee;
    }

    /**
     * Provides a string representation of a Family object.
     * @return String of Family object.
     */
    @Override
    public String toString() {
        return getFname() + " " + getLname() + ", DOB: " + getDob().toString() + ", Membership expires "
                + getExpire().toString() + ", " + getLocation().toString() + ", (Family) Guest-pass remaining: "
                + getGuestPasses();
    }
}
