package gymmanager.gymproject3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class GymManagerController {
    StringTokenizer st;
    private boolean oldMemberFlag = false;
    // TODO: initialize MemberDatabase. Keep or move to method?
    MemberDatabase db = new MemberDatabase();
    ClassSchedule classes;

    /**
     * Creates a new MemberDatabase object, initializes fitness classes, and takes input from user until 'Q' is read.
     */
//    public void run() {
//        db = new MemberDatabase();
//        System.out.println("Gym Manager running...");
//        Scanner s = new Scanner(System.in);
//
//        while (true) {
//            String workingLine;
//            int tokensRead = 0;
//
//            workingLine = s.nextLine();
//            st = new StringTokenizer(workingLine);
//
//            while (st.hasMoreTokens()) {
//                if (tokensRead == 0) setCommand(st.nextToken());
//                else break;
//                tokensRead--;
//            }
//        }
//    }

    /**
     * Selects the appropriate operation using the operation code given.
     * If operation code is not listed then it is an invalid command.
     * @param command the operation code to determine command to execute
     */
//    private void setCommand(String command) {
//        switch (command) {
//            case "A":
//                addMember('M');
//                break;
//            case "AF":
//                addMember('F');
//                break;
//            case "AP":
//                addMember('P');
//                break;
//            case "R":
//                removeMember();
//                break;
//            case "P":
//                db.print();
//                break;
//            case "PC":
//                db.printByCounty();
//                break;
//            case "PN":
//                db.printByName();
//                break;
//            case "PD":
//                db.printByExpirationDate();
//                break;
//            case "PF":
//                db.printWithFees();
//                break;
//            case "S":
//                printFitnessClasses("-Fitness classes-");
//                break;
//            case "C":
//                checkIn();
//                break;
//            case "CG":
//                checkInGuests();
//                break;
//            case "D":
//                checkout();
//                break;
//            case "DG":
//                checkoutGuests();
//                break;
//            case "Q":
//                quitProgram();
//                break;
//            case "LS":
//                initFitnessClasses();
//                break;
//            case "LM":
//                loadMemberData();
//                break;
//            default: System.out.println(command + " is an invalid command!");
//        }
//    }

    /**
     * Loads historic member data from text file named "memberList.txt
     * Sets oldMemberFlag to "true" then to "false" so addMember() doesn't calculate expire date
     */
    @FXML
    private void loadMemberData() {
        output.clear();
        oldMemberFlag = true;
        try {
            File memberList = new File("memberList.txt");
            Scanner memberScanner = new Scanner(memberList);
            //System.out.println("\n-list of members loaded-");
            output.appendText("\n-list of members loaded-");
            while (memberScanner.hasNextLine()) {
                st = new StringTokenizer(memberScanner.nextLine());
                addMember('M');
            }
            //System.out.println("-end of list-\n");
            output.appendText("\n-end of list-\n");
        } catch (FileNotFoundException e) {
            //System.out.println("Error.");
            output.setText("Error.");
            // TODO: prints to system out? Remove?
            e.printStackTrace();
        }
        oldMemberFlag = false;
    }

    /**
     * Determines the fitness class given the name of a fitness class.
     * @param className String containing the name of class to find.
     * @return FitnessClass object if class is found, null otherwise.
     */
    private FitnessClass determineClass(String className) {
        FitnessClass fitnessClass;
        switch (className) {
            case "PILATES":
                fitnessClass = new FitnessClass("PILATES");
                break;
            case "SPINNING":
                fitnessClass = new FitnessClass("SPINNING");
                break;
            case "CARDIO":
                fitnessClass = new FitnessClass("CARDIO");
                break;
            default:
                System.out.println(className + ": invalid fitness class!");
                return null;
        }
        return fitnessClass;
    }

    /**
     * Initializes fitness classes: Pilates, Spinning, and Cardio by reading class data from text
     * file "classSchedule.txt".
     */
    @FXML
    private void initFitnessClasses() {
        // clear output field
        output.clear();
        classes = new ClassSchedule();
        try {
            File fitnessSchedule = new File("classSchedule.txt");
            Scanner fitnessScanner = new Scanner(fitnessSchedule);
            while (fitnessScanner.hasNextLine()) {
                String[] line = fitnessScanner.nextLine().split(" ");
                for (int i = 0; i < line.length - 1; i++) {
                    String instructor;
                    Time time;
                    Location location;
                    FitnessClass fitnessClass = determineClass(line[i].toUpperCase());
                    if (fitnessClass == null) return;
                    instructor = line[i + 1];
                    time = findTime(line[i + 2]);
                    location = findLocation(line[i + 3]);
                    if (location == null) {
                        //System.out.println(location + " - invalid location!");
                        output.appendText(location + " - invalid location!");
                        return;
                    }
                    fitnessClass.setInstructorName(instructor);
                    fitnessClass.setTime(time);
                    fitnessClass.setLocation(location);
                    classes.addFitnessClass(fitnessClass);
                    break;
                }
            }
            printFitnessClasses("-Fitness classes loaded-\n");
        } catch (FileNotFoundException e) {
            //System.out.println("Error.");
            output.appendText("Error.");
            e.printStackTrace();
        }
    }

    /**
     * Determines the time by being given a time of day, "MORNING", "AFTERNOON", "EVENING".
     * @param timeOfDay String representing the time of day.
     * @return Time object of the time of day based on given string.
     */
    private Time findTime(String timeOfDay) {
        timeOfDay = timeOfDay.toUpperCase();
        Time time = null;
        switch (timeOfDay) {
            case "MORNING":
                time = Time.MORNING;
                break;
            case "AFTERNOON":
                time = Time.AFTERNOON;
                break;
            case "EVENING":
                time = Time.EVENING;
                break;
        }
        return time;
    }

    /**
     * Continues reading member information from input, then calls for checks to ensure appropriate values for member
     * fields. Determines membership tier (member, family, premium). Finally, sets values to appropriate member fields.
     */
    private void addMember(char tier) {
        Member member = determineMembership(tier);
        if (member == null) return;
        member.setFname(st.nextToken());
        member.setLname(st.nextToken());
        member.setDob(new Date(st.nextToken()));
        if (oldMemberFlag) member.setExpire(new Date(st.nextToken()));
        String locationName = st.nextToken();
        Location location = null;

        if (!validBirthDate(member)) return;

        location = findLocation(locationName);
        if (location == null) {
            //System.out.println(locationName + ": invalid location!");
            output.appendText(locationName + ": invalid location!");
            return;
        }
        member.setLocation(location);

        Date today = new Date();
        Date expirationDate = member instanceof Premium ? today.addOneYear() : today.addThreeMonths();
        if (!oldMemberFlag) member.setExpire(expirationDate);
        if (!member.getExpire().isValid()) {
            //System.out.println("Expiration date " + member.getExpire() + ": invalid calendar date!");
            output.appendText("Expiration date " + member.getExpire() + ": invalid calendar date!");
            return;
        }

        boolean memberAdded = db.add(member);
        if (!oldMemberFlag && memberAdded)
            //System.out.println(member.getFname() + " " + member.getLname() + " added.");
            output.appendText(member.getFname() + " " + member.getLname() + " added.");
        else if (oldMemberFlag && memberAdded)
//            System.out.println(member.getFname() + " " + member.getLname() + " DOB "
//                    + member.getDob() + ", " + "Membership expires "
//                    + member.getExpire() + ", " + member.getLocation());
            output.appendText("\n" + member.getFname() + " " + member.getLname() + " DOB "
                    + member.getDob() + ", " + "Membership expires "
                    + member.getExpire() + ", " + member.getLocation());
        else if (!db.add(member))
            //System.out.println(member.getFname() + " " + member.getLname() + " is already in the database.");
            output.appendText(member.getFname() + " " + member.getLname() + " is already in the database.");
    }

    /**
     * Determines the membership tier given a character indicating the tier.
     * M for member, F for family, and P for premium
     * @param tier character indicating the level of membership.
     * @return Member object if member tier is determined, null otherwise
     */
    private Member determineMembership(char tier) {
        Member member;
        switch (tier) {
            case 'M':
                member = new Member();
                break;
            case 'F':
                member = new Family();
                break;
            case 'P':
                member = new Premium();
                break;
            default:
                return null;
        }
        return member;
    }

    /**
     * Determines the home location of a new gym member that is being added to the database
     * @param locationName String of member's gym location that needs to be found
     * @return Location object of gym members location, returns null Location if location not found
     */
    private Location findLocation(String locationName) {
        Location location = null;
        switch (locationName.toUpperCase()) {
            case "BRIDGEWATER":
                location = Location.BRIDGEWATER;
                break;
            case "EDISON":
                location = Location.EDISON;
                break;
            case "PISCATAWAY":
                location = Location.PISCATAWAY;
                break;
            case "FRANKLIN":
                location = Location.FRANKLIN;
                break;
            case "SOMERVILLE":
                location = Location.SOMERVILLE;
                break;
        }
        return location;
    }

    /**
     * Checks if member is in database, if true then the member gets removed.
     */
    private void removeMember() {
        Member member = new Member();
        member.setFname(st.nextToken());
        member.setLname(st.nextToken());
        member.setDob(new Date(st.nextToken()));
        if (db.remove(member)) System.out.println(member.getFname() + " " + member.getLname() + " removed.");
        else System.out.println(member.getFname() + " " + member.getLname() + " is not in the database.");
    }

    /**
     * Prints out list of fitness classes, instructor name, time, and participants (if any).
     */
    private void printFitnessClasses(String header) {
        if (classes == null) {
            //System.out.println("Fitness class schedule is empty.");
            output.appendText("Fitness class schedule is empty.");
            return;
        }
        //System.out.println(header);
        //System.out.print(classes.printClassSchedule());
        //System.out.println("-end of class list-\n");
        output.appendText(header);
        output.appendText(classes.printClassSchedule());
        output.appendText("-end of class list-\n");
    }

    /**
     * Checks given member information is valid, and then checks member in for desired fitness class if there are no
     * time conflicts. Then checks member in.
     */
    private void checkIn() {
        FitnessClass fitnessClass = getFitnessClass();
        if (fitnessClass == null) return;

        Member memberInfo = new Member();
        memberInfo.setFname(st.nextToken());
        memberInfo.setLname(st.nextToken());
        memberInfo.setDob(new Date(st.nextToken()));

        if (!db.memberExists(memberInfo)) {
            System.out.println(memberInfo.getFname() + " " + memberInfo.getLname() + " " + memberInfo.getDob() +
                    " is not in the database.");
            return;
        }

        Member memberFromDb = db.getMemberFromDb(memberInfo);
        boolean timeConflict = false;
        for (FitnessClass fc: classes.getClasses())
            if ((fc != null) && (fc.participantCheckedIn(memberFromDb)))
                if (fc.getTime().equals(fitnessClass.getTime()))
                    timeConflict = true;

        String response = fitnessClass.checkIn(memberFromDb);
        if ((timeConflict && response.contains("already checked in.")) || !timeConflict)
            System.out.println(response);
        else
            System.out.println("Time conflict - " + fitnessClass.printNoParticipants());
    }

    /**
     * Checks given member information is valid to be able to check in guest, and then checks that member location
     * is the same as the class location to allow them to go to that class. Then checks member in.
     */
    private void checkInGuests() {
        FitnessClass fitnessClass = getFitnessClass();
        if (fitnessClass == null) return;

        Member guestSponsor = new Member();
        guestSponsor.setFname(st.nextToken());
        guestSponsor.setLname(st.nextToken());
        guestSponsor.setDob(new Date(st.nextToken()));

        if (!db.memberExists(guestSponsor)) {
            System.out.println(guestSponsor.getFname() + " " + guestSponsor.getLname() + " " + guestSponsor.getDob() +
                    " is not in the database.");
            return;
        }

        Member memberFromDb = db.getMemberFromDb(guestSponsor);
        System.out.println(fitnessClass.checkInGuest(memberFromDb));
    }

    /**
     * Checks if member is valid and checked into appropriate fitness class, then drops them from that fitness class.
     */
    private void checkout() {
        FitnessClass fitnessClass = getFitnessClass();
        if (fitnessClass == null) return;

        Member memberInfo = new Member();
        memberInfo.setFname(st.nextToken());
        memberInfo.setLname(st.nextToken());
        memberInfo.setDob(new Date(st.nextToken()));

        if (!memberInfo.getDob().isValid()) {
            System.out.println("DOB " + memberInfo.getDob() + ": invalid calendar date!");
            return;
        }

        if (!db.memberExists(memberInfo)) {
            System.out.println(memberInfo.getFname() + " " + memberInfo.getLname() + " " + memberInfo.getDob() +
                    " is not in the database.");
            return;
        }

        Member memberFromDb = db.getMemberFromDb(memberInfo);
        System.out.println(fitnessClass.checkout(memberFromDb));
    }

    /**
     * Checks if given member sponsoring guest is valid and that guest checked into appropriate fitness class.
     * Then drops them from that fitness class.
     */
    private void checkoutGuests() {
        FitnessClass fitnessClass = getFitnessClass();
        if (fitnessClass == null) return;

        Member guestSponsor = new Member();
        guestSponsor.setFname(st.nextToken());
        guestSponsor.setLname(st.nextToken());
        guestSponsor.setDob(new Date(st.nextToken()));

        if (!guestSponsor.getDob().isValid()) {
            System.out.println("DOB: " + guestSponsor.getDob() + " invalid calendar date!");
            return;
        }

        if (!db.memberExists(guestSponsor)) {
            System.out.println(guestSponsor.getFname() + " " + guestSponsor.getLname() + " " + guestSponsor.getDob() +
                    " is not in the database.");
            return;
        }

        Member memberFromDb = db.getMemberFromDb(guestSponsor);
        System.out.println(fitnessClass.checkoutGuest(memberFromDb));
    }

    /**
     * Checks that birthdate is a valid calendar date, is earlier than today, and is over the age of 18.
     * @param member member to get date of birth from.
     * @return true if date of birth is valid, false otherwise.
     */
    private boolean validBirthDate(Member member) {
        Date today = new Date();
        if (!member.getDob().isValid()) {
            //System.out.println("DOB " + member.getDob() + ": invalid calendar date!");
            output.appendText("DOB " + member.getDob() + ": invalid calendar date!");
            return false;
        } else if (member.getDob().compareTo(today) > 0) {
            //System.out.println("DOB " + member.getDob() + ": cannot be today or a future date!");
            output.appendText("DOB " + member.getDob() + ": cannot be today or a future date!");
            return false;
        } else if (!member.getDob().isOfAge()) {
            //System.out.println("DOB " + member.getDob() + ": must be 18 or older to join!");
            output.appendText("DOB " + member.getDob() + ": must be 18 or older to join!");
            return false;
        }
        return true;
    }

    /**
     * Continues reading from input to get information of fitness class to be retrieved. Then performs checks to ensure
     * that class is valid.
     * @return FitnessClass if the class exists, null otherwise.
     */
    private FitnessClass getFitnessClass() {
        String fitnessClassName = st.nextToken();
        if (!classNameExists(fitnessClassName)) {
            System.out.println(fitnessClassName + " - class does not exist.");
            return null;
        }

        String instructor = st.nextToken();
        if (!instructorExists(instructor)) {
            System.out.println(instructor + " - instructor does not exist.");
            return null;
        }

        String locationName = st.nextToken();
        Location location = findLocation(locationName);
        if (location == null) {
            System.out.println(locationName + ": invalid location.");
            return null;
        }

        FitnessClass fitnessClass = new FitnessClass(fitnessClassName, instructor, location);
        fitnessClass = classes.getFitnessClass(fitnessClass);
        if (fitnessClass == null) System.out.println(fitnessClassName + " by " + instructor + " does not exist at " +
                locationName);

        return fitnessClass;
    }

    /**
     * Checks given instructor against instructors teaching classes to see if they exist.
     * @param instructor String of instructor to check.
     * @return true if instructor exists, false otherwise.
     */
    private boolean instructorExists(String instructor) {
        for (FitnessClass fc: classes.getClasses())
            if ((fc != null) && (fc.getInstructorName().equalsIgnoreCase(instructor)))
                return true;
        return false;
    }

    /**
     * Checks given class name against names of classing being offered to see if they exist.
     * @param className String of class name to check.
     * @return true if class name exists, false otherwise.
     */
    private boolean classNameExists(String className) {
        for (FitnessClass fc: classes.getClasses())
            if ((fc != null) && (fc.getClassName().equalsIgnoreCase(className)))
                return true;
        return false;
    }

    /**
     * Terminates program.
     */
    private void quitProgram() {
        System.out.println("Gym Manager terminated.");
        System.exit(0);
    }







    @FXML
    private TextArea output;

    @FXML
    protected void onHelloButtonClick() {
        output.setText("Welcome to JavaFX Application!");
    }
}