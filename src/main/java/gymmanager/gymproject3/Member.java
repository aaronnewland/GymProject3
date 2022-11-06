package gymmanager.gymproject3;
/**
 * Used to create new Member objects that hold member information. These objects can then be compared.
 * Holds member name, date of birth, membership expiration date, and their gym location.
 * @author Aaron Newland, Dylan Pina
 */
public class Member implements Comparable<Member> {
    private String fname;
    private String lname;
    private Date dob;
    private Date expire;
    private Location location;

    private final double initFee = 29.99;
    private final double monthlyFee = 39.99;
    private final double quartlyFee = monthlyFee * 3;

    /**
     * Creates new Member object with null values.
     */
    public Member() {
        fname = null;
        lname = null;
        dob = null;
        expire = null;
        location = null;
    }

    /**
     * Creates new Member object with given values.
     * @param fname member first name.
     * @param lname member last name.
     * @param dob member date of birth.
     */
    public Member(String fname, String lname, Date dob) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.location = null;
    }

    /**
     * Creates new Member object with given values.
     * @param fname member first name.
     * @param lname member last name.
     * @param dob member date of birth.
     * @param location member gym location.
     */
    public Member(String fname, String lname, Date dob, Location location) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.location = location;
    }

    /**
     * Creates new Member object with given values.
     * @param fname member first name.
     * @param lname member last name.
     * @param dob member date of birth.
     * @param expire member membership expiration date.
     * @param location member gym location.
     */
    public Member(String fname, String lname, Date dob, Date expire, Location location) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.expire = expire;
        this.location = location;
    }

    /**
     * gets member's gym location.
     * @return member's gym location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets member's gym location.
     * @param location member's gym location.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * gets member's first name.
     * @return member's first name.
     */
    public String getFname() {
        return fname;
    }

    /**
     * Sets member's first name.
     * @param fname member's first name.
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     * gets member's last name.
     * @return member's last name.
     */
    public String getLname() {
        return lname;
    }

    /**
     * Sets member's last name.
     * @param lname member's last name.
     */
    public void setLname(String lname) {
        this.lname = lname;
    }

    /**
     * gets member's date of birth.
     * @return member's date of birth.
     */
    public Date getDob() {
        return dob;
    }

    /**
     * Sets member's date of birth.
     * @param dob member's date of birth.
     */
    public void setDob(Date dob) {
        this.dob = dob;
    }

    /**
     * Gets member's membership expiration date.
     * @return member's membership expiration date.
     */
    public Date getExpire() {
        return expire;
    }

    /**
     * Sets member's membership expiration date.
     * @param expire member's membership expiration date.
     */
    public void setExpire(Date expire) {
        this.expire = expire;
    }

    /**
     * Calculates membership fee owed by Member.
     * @return dollar amount of money owed in membership fee.
     */
    public double membershipFee() {
        return initFee + quartlyFee;
    }

    /**
     * Determines if a member is equal to another member. First name, last name, and date of birth are the same.
     * @param obj Member to check if equal to another Member.
     * @return true if Member is equal to other Member, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (fname.equalsIgnoreCase(((Member) obj).getFname()) && (lname.equalsIgnoreCase(((Member) obj).getLname()) &&
                (dob.equals(((Member) obj).getDob())))) return true;
        return false;
    }

    /**
     * Compares two members to each other. M1 and M2.
     * @param member Member (M2) to compare to other Member.
     * @return positive int if M1 > M2, 0 if M1 == M2, and negative int if M1 < M2.
     */
    @Override
    public int compareTo(Member member){
        if (lname.compareToIgnoreCase(member.lname) != 0)
            return lname.compareToIgnoreCase(member.lname);
        else return fname.compareToIgnoreCase(member.fname);
    }

    /**
     * Provides a string representation of a Member object.
     * @return String of Member object.
     */
    @Override
    public String toString() {
        return fname + " " + lname + ", DOB: " + dob.toString() + ", Membership expires "
                + expire.toString() + ", " + location.toString();
    }
}
