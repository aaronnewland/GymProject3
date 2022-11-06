package gymmanager.gymproject3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

public class GymManagerController {
    StringTokenizer st;
    private boolean oldMemberFlag = false;
    MemberDatabase db = new MemberDatabase();
    ClassSchedule classes;

    /**
     * Loads historic member data from text file
     * Sets oldMemberFlag to "true" then to "false" so addMember() doesn't calculate expire date
     */
    private void loadMemberData(File memberList) {
        oldMemberFlag = true;
        try {
            Scanner memberScanner = new Scanner(memberList);
            output.appendText("-list of members loaded-");
            while (memberScanner.hasNextLine()) {
                st = new StringTokenizer(memberScanner.nextLine());
                addMember('M');
            }
            output.appendText("\n-end of list-\n\n");
        } catch (FileNotFoundException e) {
            output.setText("Error: Member data file not found.\n");
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
                output.appendText(className + ": invalid fitness class!\n");
                return null;
        }
        return fitnessClass;
    }

    /**
     * Initializes fitness classes: Pilates, Spinning, and Cardio by reading class data from text
     * file "classSchedule.txt".
     */
    private void loadClassSchedule(File fitnessSchedule) {
        classes = new ClassSchedule();
        try {
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
                        output.appendText(location + " - invalid location!\n");
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
            output.appendText("Error: Class schedule file not found.\n");
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

        if (!validDob(member)) return;

        location = findLocation(locationName);
        if (location == null) {
            output.appendText(locationName + ": invalid location!\n");
            return;
        }
        member.setLocation(location);

        Date today = new Date();
        Date expirationDate = member instanceof Premium ? today.addOneYear() : today.addThreeMonths();
        if (!oldMemberFlag) member.setExpire(expirationDate);
        if (!member.getExpire().isValid()) {
            output.appendText("Expiration date " + member.getExpire() + ": invalid calendar date!\n");
            return;
        }

        boolean memberAdded = db.add(member);
        if (!oldMemberFlag && memberAdded)
            output.appendText(member.getFname() + " " + member.getLname() + " added.");
        else if (oldMemberFlag && memberAdded)
            output.appendText("\n" + member.getFname() + " " + member.getLname() + " DOB "
                    + member.getDob() + ", " + "Membership expires "
                    + member.getExpire() + ", " + member.getLocation());
        else if (!db.add(member))
            output.appendText(member.getFname() + " " + member.getLname() + " is already in the database.\n");
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
        if (db.remove(member)) output.appendText(member.getFname() + " " + member.getLname() + " removed.\n");
        else output.appendText(member.getFname() + " " + member.getLname() + " is not in the database.\n");
    }

    /**
     * Prints out list of fitness classes, instructor name, time, and participants (if any).
     */
    private void printFitnessClasses(String header) {
        if (classes == null) {
            output.appendText("Fitness class schedule is empty.");
            return;
        }
        output.appendText(header);
        output.appendText(classes.printClassSchedule());
        output.appendText("-end of class list-\n\n");
    }

    /**
     * Checks given member information is valid, and then checks member in for desired fitness class if there are no
     * time conflicts. Then checks member in.
     */
    private void checkIn() {
        if (fitnessClassCheckInFieldsMissing()) return;

        String firstName = fcFirstName.getCharacters().toString().trim();
        String lastName = fcLastName.getCharacters().toString().trim();

        Date dob = new Date(fcDob.getEditor().getCharacters().toString().trim());
        if (!validDob(dob)) return;

        Member memberInfo = new Member(firstName, lastName, dob);
        if (!db.memberExists(memberInfo)) {
            output.appendText(memberInfo.getFname() + " " + memberInfo.getLname() + " " + memberInfo.getDob() +
                    " is not in the database.\n");
            return;
        }
        Member memberFromDb = db.getMemberFromDb(memberInfo);

        String fitnessClassName = fcClassName.getCharacters().toString().trim();
        String instructorName = fcInstructorName.getCharacters().toString().trim();
        String classLocationName = fcClassLocation.getCharacters().toString().trim();
        FitnessClass fitnessClass = getFitnessClass(fitnessClassName, instructorName, classLocationName);
        if (fitnessClass == null) return;

        boolean timeConflict = false;
        for (FitnessClass fc: classes.getClasses())
            if ((fc != null) && (fc.participantCheckedIn(memberFromDb)))
                if (fc.getTime().equals(fitnessClass.getTime()))
                    timeConflict = true;

        String response = fitnessClass.checkIn(memberFromDb);
        if ((timeConflict && response.contains("already checked in.")) || !timeConflict)
            output.appendText(response);
        else
            output.appendText("Time conflict - " + fitnessClass.printNoParticipants() + "\n");
    }

    /**
     * Checks given member information is valid to be able to check in guest, and then checks that member location
     * is the same as the class location to allow them to go to that class. Then checks member in.
     */
    private void checkInGuests() {
        if (fitnessClassCheckInFieldsMissing()) return;

        String firstName = fcFirstName.getCharacters().toString().trim();
        String lastName = fcLastName.getCharacters().toString().trim();
        Date dob = new Date(fcDob.getEditor().getCharacters().toString().trim());
        if (!validDob(dob)) return;

        Member guestSponsor = new Member(firstName, lastName, dob);
        if (!db.memberExists(guestSponsor)) {
            output.appendText(guestSponsor.getFname() + " " + guestSponsor.getLname() + " " + guestSponsor.getDob() +
                    " is not in the database.\n");
            return;
        }
        Member memberFromDb = db.getMemberFromDb(guestSponsor);

        String fitnessClassName = fcClassName.getCharacters().toString().trim();
        String instructorName = fcInstructorName.getCharacters().toString().trim();
        String classLocationName = fcClassLocation.getCharacters().toString().trim();
        FitnessClass fitnessClass = getFitnessClass(fitnessClassName, instructorName, classLocationName);
        if (fitnessClass == null) return;

        output.appendText(fitnessClass.checkInGuest(memberFromDb) + "\n");
    }

    /**
     * Checks if member is valid and checked into appropriate fitness class, then drops them from that fitness class.
     */
    private void checkout() {
        String firstName = fcFirstName.getCharacters().toString().trim();
        String lastName = fcLastName.getCharacters().toString().trim();

        Date dob = new Date(fcDob.getEditor().getCharacters().toString().trim());
        if (!validDob(dob)) return;

        Member memberInfo = new Member(firstName, lastName, dob);
        if (!db.memberExists(memberInfo)) {
            output.appendText(memberInfo.getFname() + " " + memberInfo.getLname() + " " + memberInfo.getDob() +
                    " is not in the database.\n");
            return;
        }

        String fitnessClassName = fcClassName.getCharacters().toString().trim();
        String instructorName = fcInstructorName.getCharacters().toString().trim();
        String classLocationName = fcClassLocation.getCharacters().toString().trim();
        FitnessClass fitnessClass = getFitnessClass(fitnessClassName, instructorName, classLocationName);
        if (fitnessClass == null) return;

        Member memberFromDb = db.getMemberFromDb(memberInfo);
        output.appendText(fitnessClass.checkout(memberFromDb) + "\n");
    }

    /**
     * Checks if given member sponsoring guest is valid and that guest checked into appropriate fitness class.
     * Then drops them from that fitness class.
     */
    private void checkoutGuests() {
        String firstName = fcFirstName.getCharacters().toString().trim();
        String lastName = fcLastName.getCharacters().toString().trim();

        Date dob = new Date(fcDob.getEditor().getCharacters().toString().trim());
        if (!validDob(dob)) return;

        Member guestSponsorInfo = new Member(firstName, lastName, dob);
        if (!db.memberExists(guestSponsorInfo)) {
            output.appendText(guestSponsorInfo.getFname() + " " + guestSponsorInfo.getLname() + " " +
                    guestSponsorInfo.getDob() + " is not in the database.\n");
            return;
        }

        String fitnessClassName = fcClassName.getCharacters().toString().trim();
        String instructorName = fcInstructorName.getCharacters().toString().trim();
        String classLocationName = fcClassLocation.getCharacters().toString().trim();
        FitnessClass fitnessClass = getFitnessClass(fitnessClassName, instructorName, classLocationName);
        if (fitnessClass == null) return;

        Member memberFromDb = db.getMemberFromDb(guestSponsorInfo);
        output.appendText(fitnessClass.checkoutGuest(memberFromDb));
    }

    /**
     * Checks that birthdate is a valid calendar date, is earlier than today, and is over the age of 18.
     * @param member member whose birthdate we are testing for validity
     * @return true if date of birth is valid, false otherwise.
     */
    private boolean validDob(Member member) {
        Date today = new Date();
        if (!member.getDob().isValid()) {
            output.appendText("DOB " + member.getDob() + ": invalid calendar member.getDob()!\n");
            return false;
        } else if (member.getDob().compareTo(today) > 0) {
            output.appendText("DOB " + member.getDob() + ": cannot be today or a future member.getDob()!\n");
            return false;
        } else if (!member.getDob().isOfAge()) {
            output.appendText("DOB " + member.getDob() + ": must be 18 or older to join!\n");
            return false;
        }
        return true;
    }

    /**
     * Checks that birthdate is a valid calendar date, is earlier than today, and is over the age of 18.
     * @param birthdate birthdate which we are testing for validity
     * @return true if date of birth is valid, false otherwise.
     */
    private boolean validDob(Date birthdate) {
        Date today = new Date();
        if (!birthdate.isValid()) {
            output.appendText("DOB " + birthdate + ": invalid calendar birthdate!\n");
            return false;
        } else if (birthdate.compareTo(today) > 0) {
            output.appendText("DOB " + birthdate + ": cannot be today or a future birthdate!\n");
            return false;
        } else if (!birthdate.isOfAge()) {
            output.appendText("DOB " + birthdate + ": must be 18 or older to join!\n");
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
            output.appendText(fitnessClassName + " - class does not exist.\n");
            return null;
        }

        String instructor = st.nextToken();
        if (!instructorExists(instructor)) {
            output.appendText(instructor + " - instructor does not exist.\n");
            return null;
        }

        String locationName = st.nextToken();
        Location location = findLocation(locationName);
        if (location == null) {
            output.appendText(locationName + ": invalid location.\n");
            return null;
        }

        FitnessClass fitnessClass = new FitnessClass(fitnessClassName, instructor, location);
        fitnessClass = classes.getFitnessClass(fitnessClass);
        if (fitnessClass == null) output.appendText(fitnessClassName + " by " + instructor + " does not exist at " +
                locationName + "\n");

        return fitnessClass;
    }

    /**
     * Performs checks to ensures that class is valid.
     * @return FitnessClass if the class exists, null otherwise.
     */
    private FitnessClass getFitnessClass(String fitnessClassName, String instructor, String locationName) {
        if (!classNameExists(fitnessClassName)) {
            output.appendText(fitnessClassName + " - class does not exist.\n");
            return null;
        }

        if (!instructorExists(instructor)) {
            output.appendText(instructor + " - instructor does not exist.\n");
            return null;
        }

        Location location = findLocation(locationName);
        if (location == null) {
            output.appendText(locationName + ": invalid location.\n");
            return null;
        }

        FitnessClass fitnessClass = new FitnessClass(fitnessClassName, instructor, location);
        fitnessClass = classes.getFitnessClass(fitnessClass);
        if (fitnessClass == null) output.appendText(fitnessClassName + " by " + instructor + " does not exist at " +
                locationName + "\n");

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
        output.appendText("Gym Manager terminated.\n");
        System.exit(0);
    }

    @FXML
    private TextArea output;
    @FXML
    private TextField mFirstName, mLastName, mLocation;
    @FXML
    private DatePicker mDob;
    @FXML
    private RadioButton mStandardMembershipOption, mFamilyMembershipOption, mPremiumMembershipOption;
    @FXML
    private TextField fcFirstName, fcLastName, fcClassLocation, fcClassName, fcInstructorName;
    @FXML
    DatePicker fcDob;
    @FXML
    private RadioButton fcMemberOption, fcGuestOption;

    @FXML
    protected void handleMembershipAdd() {
        if (membershipAddFieldsMissing()) return;

        String firstName = mFirstName.getCharacters().toString().trim();
        String lastName = mLastName.getCharacters().toString().trim();

        Date dob = new Date(mDob.getEditor().getCharacters().toString().trim());
        if (!validDob(dob)) return;

        Location location = findLocation(mLocation.getCharacters().toString().trim());
        if (location == null) {
            output.appendText(mLocation.getCharacters().toString() + ": invalid location.\n");
            return;
        }

        Member member = null;
        if (mStandardMembershipOption.isSelected()) member = new Member(firstName, lastName, dob, location);
        else if (mFamilyMembershipOption.isSelected()) member = new Family(firstName, lastName, dob, location);
        else if (mPremiumMembershipOption.isSelected()) member = new Premium(firstName, lastName, dob, location);

        Date expirationDate = member instanceof Premium ? new Date().addOneYear() : new Date().addThreeMonths();
        if (!oldMemberFlag) member.setExpire(expirationDate);
        if (!member.getExpire().isValid()) {
            output.appendText("Expiration date " + member.getExpire() + ": invalid calendar date!\n");
            return;
        }

        boolean memberAdded = db.add(member);
        if (!oldMemberFlag && memberAdded)
            output.appendText(member.getFname() + " " + member.getLname() + " added.\n");
        else if (oldMemberFlag && memberAdded)
            output.appendText("\n" + member.getFname() + " " + member.getLname() + " DOB " + member.getDob() + ", "
                    + "Membership expires " + member.getExpire() + ", " + member.getLocation() + "\n") ;
        else if (!db.add(member))
            output.appendText(member.getFname() + " " + member.getLname() + " is already in the database.\n");
    }

    @FXML
    protected void handleMembershipRemove() {
        if (membershipRemoveFieldsMissing() || memberDbEmpty()) return;

        String firstName = mFirstName.getCharacters().toString().trim();
        String lastName = mLastName.getCharacters().toString().trim();
        Date dob = new Date(mDob.getEditor().getCharacters().toString().trim());
        if (!validDob(dob)) return;

        Member member = new Member(firstName, lastName, dob);
        if (db.remove(member)) output.appendText(member.getFname() + " " + member.getLname() + " removed.\n");
        else output.appendText(member.getFname() + " " + member.getLname() + " is not in the database.\n");
    }

    @FXML
    protected void handleFitnessClassCheckIn() {
        if (fitnessClassCheckInFieldsMissing()) return;
        if (fcMemberOption.isSelected()) checkIn();
        else if (fcGuestOption.isSelected()) checkInGuests();
    }

    @FXML
    protected void handleFitnessClassCheckout() {
        if (fitnessClassCheckoutFieldsMissing() || memberDbEmpty()) return;
        if (fcMemberOption.isSelected()) checkout();
        else if (fcGuestOption.isSelected()) checkoutGuests();
    }

    @FXML
    protected void handleInformationHubPrint() {
        if (!memberDbEmpty()) output.appendText(db.print() + "\n");
    }

    @FXML
    protected void handleInformationHubPrintByCountyZip() {
        if (!memberDbEmpty()) output.appendText(db.printByCounty()  + "\n");
    }

    @FXML
    protected void handleInformationHubPrintByLastFirst() {
        if (!memberDbEmpty()) output.appendText(db.printByName()  + "\n");
    }

    @FXML
    protected void handleInformationHubPrintByExpiration() {
        if (!memberDbEmpty()) output.appendText(db.printByExpirationDate() + "\n");
    }

    @FXML
    protected void handleInformationHubLoadMembershipList() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open membership list");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) loadMemberData(file);
    }

    @FXML
    protected void handleInformationHubShowAllClasses() {
        printFitnessClasses("-Fitness classes-\n");
    }

    @FXML
    protected void handleInformationHubLoadClassSchedule() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open class schedule");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) loadClassSchedule(file);
    }

    @FXML
    protected void handleInformationHubFirstBill() {
        output.appendText("Membership Fee First Bill\n");
    }

    @FXML
    protected void handleInformationHubNextBill() {
        output.appendText("Membership Fee Next Bill\n");
    }

    @FXML
    protected void clearOutput() {
        output.clear();
    }

    private boolean membershipAddFieldsMissing() {
        String firstName = mFirstName.getCharacters().toString();
        if (firstName.length() == 0) {
            output.appendText("Missing first name.\n");
            return true;
        }
        String lastName = mLastName.getCharacters().toString();
        if (lastName.length() == 0) {
            output.appendText("Missing last name.\n");
            return true;
        }
        String mDobLocaleDate = mDob.getEditor().getCharacters().toString();
        if (mDobLocaleDate.length() == 0) {
            output.appendText("Missing date of birth.\n");
            return true;
        }
        String locationName = mLocation.getCharacters().toString();
        if (locationName.length() == 0) {
            output.appendText("Location missing.\n");
            return true;
        }
        if (!mStandardMembershipOption.isSelected() && !mFamilyMembershipOption.isSelected() &&
                !mPremiumMembershipOption.isSelected()) {
            output.appendText("Membership type missing.\n");
            return true;
        }
        return false;
    }

    private boolean membershipRemoveFieldsMissing() {
        String firstName = mFirstName.getCharacters().toString();
        if (firstName.length() == 0) {
            output.appendText("Missing first name.\n");
            return true;
        }
        String lastName = mLastName.getCharacters().toString();
        if (lastName.length() == 0) {
            output.appendText("Missing last name.\n");
            return true;
        }
        String mDobLocaleDate = mDob.getEditor().getCharacters().toString();
        if (mDobLocaleDate.length() == 0) {
            output.appendText("Missing date of birth.\n");
            return true;
        }
        return false;
    }

    private boolean fitnessClassCheckInFieldsMissing() {
        String firstName = fcFirstName.getCharacters().toString();
        if (firstName.length() == 0) {
            output.appendText("Missing first name.\n");
            return true;
        }
        String lastName = fcLastName.getCharacters().toString();
        if (lastName.length() == 0) {
            output.appendText("Missing last name.\n");
            return true;
        }
        String mDobLocaleDate = fcDob.getEditor().getCharacters().toString();
        if (mDobLocaleDate.length() == 0) {
            output.appendText("Missing date of birth.\n");
            return true;
        }
        String classLocationName = fcClassLocation.getCharacters().toString();
        if (classLocationName.length() == 0) {
            output.appendText("Class location missing.\n");
            return true;
        }
        String className = fcClassName.getCharacters().toString();
        if (className.length() == 0) {
            output.appendText("Class name missing.\n");
            return true;
        }
        String instructorName = fcInstructorName.getCharacters().toString();
        if (instructorName.length() == 0) {
            output.appendText("Instructor name missing.\n");
            return true;
        }
        if (!fcMemberOption.isSelected() && !fcGuestOption.isSelected()) {
            output.appendText("Member type missing.\n");
            return true;
        }
        return false;
    }
    private boolean fitnessClassCheckoutFieldsMissing() {
        String firstName = fcFirstName.getCharacters().toString();
        if (firstName.length() == 0) {
            output.appendText("Missing first name.\n");
            return true;
        }
        String lastName = fcLastName.getCharacters().toString();
        if (lastName.length() == 0) {
            output.appendText("Missing last name.\n");
            return true;
        }
        String mDobLocaleDate = fcDob.getEditor().getCharacters().toString();
        if (mDobLocaleDate.length() == 0) {
            output.appendText("Missing date of birth.\n");
            return true;
        }
        String className = fcClassName.getCharacters().toString();
        if (className.length() == 0) {
            output.appendText("Class name missing.\n");
            return true;
        }
        String instructorName = fcInstructorName.getCharacters().toString();
        if (instructorName.length() == 0) {
            output.appendText("Instructor name missing.\n");
            return true;
        }
        return false;
    }

    private void printMembershipFields() {
        output.appendText("First Name: " + mFirstName.getCharacters() + "\n");
        output.appendText("Last Name: " + mLastName.getCharacters() + "\n");
        output.appendText("Date of Birth: " + mDob.getValue() + "\n");
        output.appendText("Location: " + mLocation.getCharacters() + "\n");
        output.appendText("Standard Membership Option Selected?: " + mStandardMembershipOption.isSelected() + "\n");
        output.appendText("Family Membership Option Selected?: " + mFamilyMembershipOption.isSelected() + "\n");
        output.appendText("Premium Membership Option Selected?: " + mPremiumMembershipOption.isSelected() + "\n");
    }

    private void printFitnessClassFields() {
        output.appendText("First Name: " + fcFirstName.getCharacters() + "\n");
        output.appendText("Last Name: " + fcLastName.getCharacters() + "\n");
        output.appendText("Date of Birth: " + fcDob.getValue() + "\n");
        output.appendText("Class Location: " + fcClassLocation.getCharacters() + "\n");
        output.appendText("Class Name: " + fcClassName.getCharacters() + "\n");
        output.appendText("Instructor Name: " + fcInstructorName.getCharacters() + "\n");
        output.appendText("Member Option Selected?: " + fcMemberOption.isSelected() + "\n");
        output.appendText("Guest Option Selected?: " + fcGuestOption.isSelected() + "\n");
    }

    private boolean memberDbEmpty() {
        if (db.memberDbEmpty()) {
            output.appendText("Member database is empty!\n");
            return true;
        } else return false;
    }
}