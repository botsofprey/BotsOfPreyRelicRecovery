package Autonomous;

/**
 * Created by root on 8/21/17.
 */

public interface GeoLocator {
    //meant to contain all code necessary to determine location of robot
    public Location getLocation();
    public void setBaseLocation(Location location);
}
