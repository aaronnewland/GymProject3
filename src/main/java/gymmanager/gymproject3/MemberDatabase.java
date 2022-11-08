package gymmanager.gymproject3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Creates and maintains a database that holds members and can sort by name, expiration date, or county.
 * @author Aaron Newland, Dylan Pina
 */
public class MemberDatabase {
    final int INIT_CAP = 4;
    private Member[] mlist;
    private int size;
    StringTokenizer st;
    private boolean oldMemberFlag = true;

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
     * Loads historical member info from text file.
     * @param memberList file to read in member info from.
     */
    public void loadMemberData(File memberList) {
        try {
            Scanner memberScanner = new Scanner(memberList);
            while (memberScanner.hasNextLine()) {
                st = new StringTokenizer(memberScanner.nextLine());
                addMember();
            }
        } catch (FileNotFoundException e) {
        }
    }

    /**
     * Reads in member info, creates a member for each entry, and places new member in database.
     */
    private void addMember() {
        Member member = new Member();
        if (member == null) return;
        member.setFname(st.nextToken());
        member.setLname(st.nextToken());
        member.setDob(new Date(st.nextToken()));
        if (oldMemberFlag) member.setExpire(new Date(st.nextToken()));
        String locationName = st.nextToken();
        Location location;

        if (!validDob(member)) return;

        location = Location.findLocation(locationName);
        if (location == null) {
            return;
        }
        member.setLocation(location);

        if (!member.getExpire().isValid()) {
            return;
        }

        this.add(member);
    }

    /**
     * Checks that birthdate is a valid calendar date, is earlier than today, and is over the age of 18.
     * @param member member whose birthdate we are testing for validity
     * @return true if date of birth is valid, false otherwise.
     */
    private boolean validDob(Member member) {
        Date today = new Date();
        if (!member.getDob().isValid()) {
            return false;
        } else if (member.getDob().compareTo(today) > 0) {
            return false;
        } else if (!member.getDob().isOfAge()) {
            return false;
        }
        return true;
    }

    /**
     * Prints list of members with the provided header.
     * @param header describes the list that is being printed.
     * @return a String list of members with the provided header.
     */
    public String print(String header) {
        StringBuilder sb = new StringBuilder();
        if (memberDbEmpty()) {
            sb.append("Member database is empty!");
            return sb.toString();
        }
        sb.append("-" + header + "-");
        for (Member m : mlist) if (m != null) sb.append("\n" + m);
        sb.append("\n-end of list-\n");
        return sb.toString();
    }

    /**
     * Print list of members in database in without sorting.
     * @return A String list of members in database in without sorting.
     */
    public String print() {
        return print("list of members");
    }

    /**
     * Print list of members with their membership fees
     */

    /**
     * Print list of members with their membership fees
     * @return String list of members with their membership fees
     */
    public String printWithFees() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n-list of members with membership fees-\n");
        for (Member m : mlist)
            if (m != null) sb.append(m + ", Membership fee: $" + m.membershipFee() + "\n");
        sb.append("-end of list-\n");
        return sb.toString();
    }

    /**
     * Print list sorted by county names then by zipcode.
     * @return String list sorted by county names then by zipcode.
     */
    public String printByCounty() {
        sortByCounty();
        return print("list of members sorted by county and zipcode");
    }

    /**
     * Print list sorted by expiration date.
     * @return String list sorted by expiration date.
     */
    public String printByExpirationDate() {
        sortByExpiration();
        return print("list of members sorted by membership expiration date");
    }

    /**
     * Print list sorted by last name then by first name.
     * @return String list sorted by last name then by first name.
     */
    public String printByName() {
        sortByName();
        return print("list of members sorted by last name, and first name");
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
    public boolean memberDbEmpty() {
        return mlist == null || mlist.length == 0;
    }
}
