package gymmanager.gymproject3;

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
}
