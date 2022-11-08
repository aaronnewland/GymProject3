package gymmanager.gymproject3;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
/**
 * Controller for GymManager.fxml
 * @author Aaron Newland, Dylan Pina
 */
public class GymManagerController {
    MemberDatabase db = new MemberDatabase();
    ClassSchedule classes;

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

    /**
     * Action handler for the 'Add' button in the Membership form.
     * Adds a member to the member database with the form values.
     */
    @FXML
    protected void handleMembershipAdd() {
        if (membershipAddFieldsMissing()) return;

        String firstName = mFirstName.getCharacters().toString().trim();
        String lastName = mLastName.getCharacters().toString().trim();

        Location location =  Location.findLocation(mLocation.getCharacters().toString().trim());
        if (location == null) {
            output.appendText(mLocation.getCharacters().toString() + ": invalid location!\n");
            return;
        }

        Date dob = new Date(mDob.getEditor().getCharacters().toString().trim());
        if (!validDob(dob)) return;

        Member member = null;
        if (mStandardMembershipOption.isSelected()) member = new Member(firstName, lastName, dob, location);
        else if (mFamilyMembershipOption.isSelected()) member = new Family(firstName, lastName, dob, location);
        else if (mPremiumMembershipOption.isSelected()) member = new Premium(firstName, lastName, dob, location);

        Date expirationDate = member instanceof Premium ? new Date().addOneYear() : new Date().addThreeMonths();
        member.setExpire(expirationDate);
        if (!member.getExpire().isValid()) {
            output.appendText("Expiration date " + member.getExpire() + ": invalid calendar date!\n");
            return;
        }

        boolean memberAdded = db.add(member);
        if (memberAdded)
            output.appendText(member.getFname() + " " + member.getLname() + " added.\n");
        else if (!db.add(member))
            output.appendText(member.getFname() + " " + member.getLname() + " is already in the database.\n");
    }

    /**
     * Action handler for the 'Remove' button in the Membership form.
     * Removes the specified member from the member database.
     */
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

    /**
     * Action handler for the 'Check In' button in the Fitness Class form.
     * Checks in a member into a fitness class.
     */
    @FXML
    protected void handleFitnessClassCheckIn() {
        if (fitnessClassCheckInFieldsMissing()) return;
        if (fcMemberOption.isSelected()) checkIn();
        else if (fcGuestOption.isSelected()) checkInGuests();
    }

    /**
     * Action handler for the 'Checkout' button in the Fitness Class form.
     * Checks out a member from a fitness class.
     */
    @FXML
    protected void handleFitnessClassCheckout() {
        if (fitnessClassCheckoutFieldsMissing() || memberDbEmpty()) return;
        if (fcMemberOption.isSelected()) checkout();
        else if (fcGuestOption.isSelected()) checkoutGuests();
    }

    /**
     * Action handler for the 'Print' menu option under the 'Member Database' menu in the Information Hub.
     * Prints a list of all members in the member database.
     */
    @FXML
    protected void handleInformationHubPrint() {
        if (!memberDbEmpty()) output.appendText(db.print() + "\n");
    }

    /**
     * Action handler for the 'Print by County/Zipcode' menu option under the 'Member Database' menu in the Information Hub.
     * Prints a list of all members in the member database sorted by county then zipcode.
     */
    @FXML
    protected void handleInformationHubPrintByCountyZip() {
        if (!memberDbEmpty()) output.appendText(db.printByCounty()  + "\n");
    }

    /**
     * Action handler for the 'Print by Last/First Names' menu option under the 'Member Database' menu in the Information Hub.
     * Prints a list of all members in the member database sorted by last and first name.
     */
    @FXML
    protected void handleInformationHubPrintByLastFirst() {
        if (!memberDbEmpty()) output.appendText(db.printByName()  + "\n");
    }

    /**
     * Action handler for the 'Print by Expiration Date' menu option under the 'Member Database' menu in the Information Hub.
     * Prints a list of all members in the member database sorted by their expiration dates.
     */
    @FXML
    protected void handleInformationHubPrintByExpiration() {
        if (!memberDbEmpty()) output.appendText(db.printByExpirationDate() + "\n");
    }

    /**
     * Action handler for the 'Print with Fees' menu option under the 'Member Database' menu in the Information Hub.
     * Prints a list of all members in the member database along with their membership fees.
     */
    @FXML
    protected void handleInformationHubPrintFees() {
        if (!memberDbEmpty()) output.appendText(db.printWithFees());
    }

    /**
     * Action handler for the 'Load Member List from File' menu option under the 'Member Database' menu in the Information Hub.
     * Prompts the user to input a file which is used to load members into the member database.
     */
    @FXML
    protected void handleInformationHubLoadMembershipList() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open membership list");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            db.loadMemberData(file);
            output.appendText(db.print("-list of members loaded-"));
        }
    }

    /**
     * Action handler for the 'Show All Classes' menu option under the 'Classes Schedule' menu in the Information Hub.
     * Prints out list of fitness classes, instructor name, time, and participants (if any).
     */
    @FXML
    protected void handleInformationHubShowAllClasses() {
        printFitnessClasses("-Fitness classes-\n");
    }

    /**
     * Action handler for the 'Load Class Schedule from File' menu option under the 'Classes Schedule' menu in the Information Hub.
     * Prompts the user to input a file which is used to load fitness classes.
     */
    @FXML
    protected void handleInformationHubLoadClassSchedule() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open class schedule");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            classes = new ClassSchedule();
            classes = classes.loadClassSchedule(file);
            printFitnessClasses("-Fitness classes loaded-\n");
        }
    }

    /**
     * Action handler for the 'Clear Output' button in the Information Hub.
     * Clears all content displayed in the Output text area.
     */
    @FXML
    protected void clearOutput() {
        output.clear();
    }

    /**
     * Checks that all required fields for adding a member in the Membership form have been supplied values
     * @return true if all required fields for adding a member have been supplied values
     */
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

    /**
     * Checks that all required fields for removing a member in the Membership form have been supplied values
     * @return true if all required fields for removing a member have been supplied values
     */
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

    /**
     * Checks that all required fields for checking in a member or guest in the Fitness Class form have been supplied values
     * @return true if all required fields for checking in a member or guest have been supplied values
     */
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

    /**
     * Checks that all required fields for checking out a member or guest in the Fitness Class form have been supplied values
     * @return true if all required fields for checking out a member or guest have been supplied values
     */
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

    /**
     * Checks to see if the member database is empty
     * @returns true if the member database is empty
     */
    private boolean memberDbEmpty() {
        if (db.memberDbEmpty()) {
            output.appendText("Member database is empty!\n");
            return true;
        } else return false;
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
        else {
            fitnessClass.checkout(memberFromDb);
            output.appendText("Time conflict - " + fitnessClass.printNoParticipants() + "\n");
        }
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

        Location location = Location.findLocation(locationName);
        if (location == null) {
            output.appendText(locationName + " - invalid location.\n");
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
}