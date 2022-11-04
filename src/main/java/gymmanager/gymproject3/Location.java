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
