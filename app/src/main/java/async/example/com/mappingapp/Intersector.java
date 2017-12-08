package async.example.com.mappingapp;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Administrator on 07/12/2017.
 */

//To implements the intersection algorithm, I used the following stackoverflow thread:
// https://stackoverflow.com/questions/563198/whats-the-most-efficent-way-to-calculate-where-two-line-segments-intersect

public class Intersector {

    public static final int START = 0;
    public static final int END = 1;

    private static boolean doLinesIntersect(LatLng[] first, LatLng[] sec) {

        if (first[START].longitude == first[END].longitude) {
            return !(sec[START].longitude == sec[END].longitude && first[END] != sec[END]);

        } else if (sec[START].longitude == sec[END].longitude) {
            return true;

        } else {
            // Both lines are not parallel to the y-axis
            double slope1 = (first[START].latitude - first[END].latitude) / (first[START].longitude - first[END].longitude);
            double slope2 = (sec[START].latitude - sec[END].latitude) / (sec[START].longitude - sec[END].longitude);

            return slope1 != slope2;
        }
    }

    private static boolean isWithinRangeOfLng(LatLng[] first, LatLng[] sec) {

        double firstHighLng = Math.max(first[0].longitude, first[1].longitude);
        double firstLowLng = Math.min(first[0].longitude, first[1].longitude);

        double secHighLng = Math.max(sec[0].longitude, sec[1].longitude);
        double secLowLng = Math.min(sec[0].longitude, sec[1].longitude);

        return secLowLng >= firstLowLng && secHighLng <= firstHighLng;
    }

    private static boolean isWithinRangeOfLat(LatLng[] first, LatLng[] sec) {

        double firstHighLat = Math.max(first[0].latitude, first[1].latitude);
        double firstLowLat = Math.min(first[0].latitude, first[1].latitude);

        double secHighLat = Math.max(sec[0].latitude, sec[1].latitude);
        double secLowLat = Math.min(sec[0].latitude, sec[1].latitude);

        return secLowLat >= firstLowLat && secHighLat <= firstHighLat;
    }

    public static boolean isIntersectWithinRange(LatLng[] first, LatLng[] sec, boolean forLatRange){

        if(forLatRange) return doLinesIntersect(first, sec) && isWithinRangeOfLat(first, sec);
        else return doLinesIntersect(first, sec) && isWithinRangeOfLng(first, sec);
    }
}
