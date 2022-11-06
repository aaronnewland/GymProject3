package gymmanager.gymproject3;

import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Used to create new ClassSchedule objects that hold all fitness classes at all gym locations, and the number of
 * classes.
 * @author Aaron Newland, Dylan Pina
 */
public class ClassSchedule {
    private FitnessClass[] classes;
    private int numClasses;

    /**
     * Creates new ClassSchedule object, that can hold the default number of classes, 15.
     */
    public ClassSchedule() {
        classes = new FitnessClass[Constants.DEFAULT_ARRAY_LEN];
        numClasses = 0;
    }

    /**
     * Adds given fitnessClass to ClassSchedule.
     * @param fitnessClass class to be added to schedule.
     */
    public void addFitnessClass(FitnessClass fitnessClass) {
        classes[numClasses++] = fitnessClass;
    }

    /**
     * Searches through the fitness classes in the schedule to find the given fitness class.
     * @param fitnessClass class to be retrieved.
     * @return fitnessClass if found, null if not found.
     */
    public FitnessClass getFitnessClass(FitnessClass fitnessClass) {
        for (int i = 0; i < numClasses; i++)
            if ((classes[i] != null) && (classes[i].equals(fitnessClass))){
                return classes[i];
            }
        return null;
    }

    /**
     * Retrieves the schedule of classes.
     * @return classes array in ClassSchedule.
     */
    public FitnessClass[] getClasses() {
        return classes;
    }

    /**
     * Creates String of all fitness classes if there are any classes in the schedule.
     * @return String of all fitness classes if they exist, or a string saying the schedule is empty if not.
     */
    public String printClassSchedule() {
        if (classes == null) return "Fitness class schedule is empty.";
        StringBuilder classSchedule = new StringBuilder();
        for (FitnessClass fitnessClass : classes)
            if (fitnessClass != null) classSchedule.append(fitnessClass + "\n");
        return classSchedule.toString();
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
                //output.appendText(className + ": invalid fitness class!\n");
                return null;
        }
        return fitnessClass;
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

    public ClassSchedule loadClassSchedule(File fitnessSchedule) {
        ClassSchedule classList = new ClassSchedule();
        try {
            Scanner fitnessScanner = new Scanner(fitnessSchedule);
            while (fitnessScanner.hasNextLine()) {
                String[] line = fitnessScanner.nextLine().split(" ");
                for (int i = 0; i < line.length - 1; i++) {
                    String instructor;
                    Time time;
                    Location location;
                    FitnessClass fitnessClass = determineClass(line[i].toUpperCase());
                    if (fitnessClass == null) return null;
                    instructor = line[i + 1];
                    time = findTime(line[i + 2]);
                    location = findLocation(line[i + 3]);
                    if (location == null) {
                        //output.appendText(location + " - invalid location!\n");
                        return null;
                    }
                    fitnessClass.setInstructorName(instructor);
                    fitnessClass.setTime(time);
                    fitnessClass.setLocation(location);
                    classList.addFitnessClass(fitnessClass);
                    break;
                }
            }
            //printFitnessClasses("-Fitness classes loaded-\n");
        } catch (FileNotFoundException e) {
            //output.appendText("Error: Class schedule file not found.\n");
        }
        return classList;
    }




}
