package gymmanager.gymproject3;

/**
 * Used to create new Premium objects that hold information for members with premium tier membership.
 * These objects can then be compared.
 * Holds all member information, and number of guest passes. For premium the max number of guest passes is 3.
 * @author Aaron Newland, Dylan Pina
 */
public class Premium extends Family implements Comparable<Member> {
    private final double premiumFee = 659.89;

    /**
     * Default constructor for premium tier gym member. All member fields are set to null, and guest passes is set to 3.
     */
    public Premium() {
        super();
        setGuestPasses(3);
    }

    /**
     * Creates new Premium object with given values. Set guest passes to 3.
     * @param fname member first name.
     * @param lname member last name.
     * @param dob member date of birth.
     * @param location member gym location.
     */
    public Premium(String fname, String lname, Date dob, Location location) {
        super(fname, lname, dob, location);
        setGuestPasses(3);
    }

    /**
     * Creates new Premium object with given values. Set guest passes to 3.
     * @param fname member first name.
     * @param lname member last name.
     * @param dob member date of birth.
     * @param expire member membership expiration date.
     * @param location member gym location.
     */
    public Premium(String fname, String lname, Date dob, Date expire, Location location) {
        super(fname, lname, dob, expire, location);
        setGuestPasses(3);
    }

    /**
     * Calculates membership fee owed by Premium member.
     * @return dollar amount of money owed in membership fee.
     */
    @Override
    public double membershipFee() {
        return premiumFee;
    }

    /**
     * Provides a string representation of a Premium object.
     * @return String of Premium object.
     */
    @Override
    public String toString() {
        return getFname() + " " + getLname() + ", DOB: " + getDob().toString() + ", Membership expires "
                + getExpire().toString() + ", " + getLocation().toString() + ", (Premium) Guest-pass remaining: "
                + getGuestPasses();
    }
}
