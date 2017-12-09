package async.example.com.mappingapp;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

/**
 * Created by Administrator on 07/12/2017.
 */

//To implements the intersection algorithm, I used the following stackoverflow thread:
// https://stackoverflow.com/questions/563198/whats-the-most-efficent-way-to-calculate-where-two-line-segments-intersect

public class IntersectionChecker {

    public static final int START = 0;
    public static final int END = 1;

    private static LatLng findIntersection(LatLng[] first, LatLng[] sec){

        //find the equations of the two lines according to the sets of points
        double first_x = first[1].longitude - first[0].longitude;
        double first_y = first[1].latitude - first[0].latitude;
        double sec_x = sec[1].longitude - sec[0].longitude;
        double sec_y = sec[1].latitude - sec[0].latitude;

        double denom = first_x * sec_y - sec_x * first_y;
        if (denom == 0) return null;

        boolean denom_is_positive = denom > 0;

        double firstSec_x = first[0].longitude - sec[0].longitude;
        double firstSec_y = first[0].latitude - sec[0].latitude;

        double s_numer = first_x * firstSec_y -first_y * firstSec_x;
        if(s_numer< 0 == denom_is_positive) return null;

        double t_numer =sec_x * firstSec_y - sec_y * firstSec_x;
        if(t_numer< 0== denom_is_positive) return null;

        if((s_numer >denom == denom_is_positive) || (t_numer >denom == denom_is_positive)) return null;

        //collision detected
        double t = t_numer / denom;

        return new LatLng(first[0].latitude + (t * first_y), first[0].longitude + (t * first_x));
    }

    public static boolean isIntersectWithinRange(LatLng[] first, LatLng[] sec){

        return null != findIntersection(first, sec);
    }
}
