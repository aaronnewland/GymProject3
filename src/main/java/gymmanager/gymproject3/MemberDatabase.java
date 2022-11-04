package gymmanager.gymproject3;

/**
 * Creates and maintains a database that holds members and can sort by name, expiration date, or county.
 * @author Aaron Newland, Dylan Pina
 */
public class MemberDatabase {
    final int INIT_CAP = 4;
    private Member[] mlist;
    private int size;

    /**
     * Searches for Member in database.
     * @param member member to search for.
     * @return return index if member is found, returns NOT_FOUND otherwise.
     */
    private int find(Member member) {
        if (!memberDbEmpty())
            for (int i = 0; i < size; i++)
                if (mlist[i] != null && mlist[i].equals(member)) return i;
        return Constants.NOT_FOUND;
    }

    /**
     * Determines if member exists in database.
     * @param member member to search for.
     * @return true if member exists in database, false otherwise.
     */
    public boolean memberExists(Member member) {
        if (!memberDbEmpty())
            for (Member m : mlist)
                if (m != null && m.equals(member)) return true;
        return false;
    }

    /**
     * Retrieves a member from the database.
     * @param member member to be retrieved from database.
     * @return member if found, otherwise return null.
     */
    public Member getMemberFromDb(Member member) {
        int memberIndex = find(member);
        if (memberIndex != Constants.NOT_FOUND) return mlist[memberIndex];
        System.out.println(member.getFname() + " " + member.getLname() + " " + member.getDob() + " is not in the database.");
        return null;
    }

    /**
     * If database is full, then grows by factor of INIT_CAP.
     */
    private void grow() {
        Member[] newList = new Member[mlist.length + INIT_CAP];
        for (int i = 0; i < size; i++) newList[i] = mlist[i];
        mlist = newList;
    }

    /**
     * Adds member to database.
     * @param member member to be added to database.
     * @return true if member was added, false otherwise.
     */
    public boolean add(Member member) {
        if (member == null || memberExists(member)) return false;
        if (memberDbEmpty()) {
            mlist = new Member[INIT_CAP];
            size = 0;
        }
        if (mlist.length == size) grow();
        mlist[size++] = member;
        return true;
    }

    /**
     * Removes member from database.
     * @param member member to be removed.
     * @return true if member was successfully removed, false otherwise.
     */
    public boolean remove(Member member) {
        int memberIndex = find(member);
        if (memberIndex == Constants.NOT_FOUND) return false;
        Member[] newList = new Member[size-- - 1];
        int i = 0;
        for (Member m : mlist)
            if (m != null && !m.equals(member)) newList[i++] = m;
        mlist = newList;
        return true;
    }

    /**
     * Prints list of members with the provided header.
     * @param header describes the list that is being printed.
     */
    private void print(String header) {
        if (memberDbEmpty()) {
            System.out.println("Member database is empty!");
            return;
        }
        System.out.println("\n-" + header + "-");
        for (Member m : mlist) if (m != null) System.out.println(m);
        System.out.println("-end of list-\n");
    }

    /**
     * Print list of members in database in without sorting.
     */
    public void print() {
        print("list of members");
    }

    /**
     * Print list of members with their membership fees
     */
    public void printWithFees() {
        if (memberDbEmpty()) {
            System.out.println("Member database is empty!");
            return;
        }
        System.out.println("\n-list of members with membership fees-");
        for (Member m : mlist)
            if (m != null) System.out.println(m + ", Membership fee: $" + m.membershipFee());
        System.out.println("-end of list-\n");
    }

    /**
     * Print list sorted by county names then by zipcode.
     */
    public void printByCounty() {
        sortByCounty();
        print("list of members sorted by county and zipcode");
    }

    /**
     * Print list sorted by expiration date.
     */
    public void printByExpirationDate() {
        sortByExpiration();
        print("list of members sorted by membership expiration date");
    }

    /**
     * Print list sorted by last name then by first name.
     */
    public void printByName() {
        sortByName();
        print("list of members sorted by last name, and first name");
    }

    /**
     * Sort by county then by zipcode.
     */
    private void sortByCounty() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (mlist[j] == null || mlist[j + 1] == null) continue;
                Member m1 = mlist[j];
                Member m2 = mlist[j + 1];
                String m1County = m1.getLocation().getCounty();
                String m2County = m2.getLocation().getCounty();
                if (m1County.compareTo(m2County) >= 1) {
                    swap(j, j + 1);
                } else if (m1County.compareTo(m2County) == 0) {
                    Long m1Zip = m1.getLocation().getZip();
                    Long m2Zip = m2.getLocation().getZip();
                    if (m1Zip > m2Zip) {
                        swap(j, j + 1);
                    }
                }
            }
        }
    }

    /**
     * Sort by expiration date.
     */
    private void sortByExpiration() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (mlist[j] == null || mlist[j + 1] == null) continue;
                Member m1 = mlist[j];
                Member m2 = mlist[j + 1];
                Date m1Exp = m1.getExpire();
                Date m2Exp = m2.getExpire();
                if (m1Exp.compareTo(m2Exp) >= 1)
                    swap(j, j + 1);
            }
        }
    }

    /**
     * Sort by last name then by first name.
     */
    private void sortByName() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (mlist[j] == null || mlist[j + 1] == null) continue;
                Member m1 = mlist[j];
                Member m2 = mlist[j + 1];
                if (m1.compareTo(m2) >= 1)
                    swap(j, j + 1);
            }
        }
    }

    /**
     * Swaps the two members.
     * @param i1 first member to swap.
     * @param i2 second member to swap.
     */
    private void swap(int i1, int i2) {
        Member temp = mlist[i1];
        mlist[i1] = mlist[i2];
        mlist[i2] = temp;
    }

    /**
     * Determines if member database is empty.
     * @return true if database is empty, false otherwise.
     */
    private boolean memberDbEmpty() {
        return mlist == null || mlist.length == 0;
    }
}
