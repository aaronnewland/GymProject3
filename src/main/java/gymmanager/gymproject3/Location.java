package gymmanager.gymproject3;

import java.text.DecimalFormat;

/**
 * Holds enumeration data for the five gym locations.
 * @author Aaron Newland, Dylan Pina
 */
public enum Location {
    BRIDGEWATER (8807, "SOMERSET"),
    EDISON (8837, "MIDDLESEX"),
    FRANKLIN (8873, "SOMERSET"),
    PISCATAWAY (8854, "MIDDLESEX"),
    SOMERVILLE (8876, "SOMERSET");

    private final long zip;
    private final String county;

    /**
     * Creates new Location object using given zip code and county name.
     * @param zip zip code of gym location in long type.
     * @param county county of gym location in String.
     */
    Location(long zip, String county) {
        this.zip = zip;
        this.county = county;
    }


    /**
     * Determines the home location of a new gym member that is being added to the database
     * @param locationName String of member's gym location that needs to be found
     * @return Location object of gym members location, returns null Location if location not found
     */
    public static Location findLocation(String locationName) {
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
     * Get zip code of gym location.
     * @return zip code.
     */
    public long getZip() {
        return zip;
    }

    /**
     * Get county of gym location.
     * @return county name.
     */
    public String getCounty() {
        return county;
    }

    /**
     * Provides a string representation of a Location object.
     * @return String of Location object.
     */
    @Override
    public String toString() {
        DecimalFormat zipCodeFormat = new DecimalFormat("00000");
        return "Location: " + name() + ", " + zipCodeFormat.format(zip) + ", " + county;
    }
}
