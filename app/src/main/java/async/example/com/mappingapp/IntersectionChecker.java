package async.example.com.mappingapp;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Administrator on 07/12/2017.
 */

//To implements the intersection algorithm, I used the following stackoverflow thread:
// https://stackoverflow.com/questions/16314069/calculation-of-intersections-between-line-segments

public class IntersectionChecker {

    private static LatLng[] findHigher(LatLng[] firstLine, LatLng[] secLine){

        if(firstLine[0].longitude > secLine[0].longitude) return firstLine;
        else return secLine;
    }

    private static LatLng[] findLower(LatLng[] firstLine, LatLng[] secLine){

        if(firstLine[0].longitude < secLine[0].longitude) return firstLine;
        else return secLine;
    }

    private static boolean isVerticalsOverlap(LatLng[] firstLine, LatLng[] secLine){

        LatLng[] high = findHigher(firstLine, secLine);
        LatLng[] low = findLower(firstLine, secLine);

        double higherMin = Math.min(high[0].latitude, high[1].latitude);
        double lowerMax = Math.max(low[0].latitude, low[1].latitude);

        if(higherMin <= lowerMax) return true;
        else return false;
    }

    public static boolean isIntersect(LatLng[] firstLine, LatLng[] secLine){

        //check if line are vertical
        if(firstLine[0].longitude == firstLine[1].longitude && secLine[0].longitude == secLine[1].latitude) {

            //if two longs unequal, than no intersection
            if (firstLine[0] != secLine[0]) return false;
            else return isVerticalsOverlap(firstLine, secLine);

        } else if(firstLine[0].longitude == firstLine[1].longitude) {

            //in case only one is vertical

        }
    }
}
