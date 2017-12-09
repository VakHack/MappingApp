package async.example.com.mappingapp;

/**
 * Created by Administrator on 07/12/2017.
 */

public class DistanceCalculator {

    //    private static boolean doLinesIntersect(LatLng[] first, LatLng[] sec) {
//
//        if (first[START].longitude == first[END].longitude) {
//            return !(sec[START].longitude == sec[END].longitude && first[END] != sec[END]);
//
//        } else if (sec[START].longitude == sec[END].longitude) {
//            return true;
//
//        } else {
//            // Both lines are not parallel to the y-axis
//            double slope1 = (first[START].latitude - first[END].latitude) / (first[START].longitude - first[END].longitude);
//            double slope2 = (sec[START].latitude - sec[END].latitude) / (sec[START].longitude - sec[END].longitude);
//
//            return slope1 != slope2;
//        }
//    }

//    private static boolean isWithinRangeOfLng(LatLng[] first, LatLng[] sec) {
//
//        double firstHighLng = Math.max(first[0].longitude, first[1].longitude);
//        double firstLowLng = Math.min(first[0].longitude, first[1].longitude);
//
//        double secHighLng = Math.max(sec[0].longitude, sec[1].longitude);
//        double secLowLng = Math.min(sec[0].longitude, sec[1].longitude);
//
//        return secLowLng >= firstLowLng && secHighLng <= firstHighLng;
//    }
//
//    private static boolean isWithinRangeOfLat(LatLng[] first, LatLng[] sec) {
//
//        double firstHighLat = Math.max(first[0].latitude, first[1].latitude);
//        double firstLowLat = Math.min(first[0].latitude, first[1].latitude);
//
//        double secHighLat = Math.max(sec[0].latitude, sec[1].latitude);
//        double secLowLat = Math.min(sec[0].latitude, sec[1].latitude);
//
//        return secLowLat >= firstLowLat && secHighLat <= firstHighLat;
//    }

//    private static LatLng findIntersection(LatLng p1, LatLng p2, LatLng p3, LatLng p4) {
//        Double xD1,yD1,xD2,yD2,xD3,yD3;
//        Double dot,deg,len1,len2;
//        Double segmentLen1,segmentLen2;
//        Double ua,ub,div;
//
//        // calculate differences
//        xD1 = p2.longitude - p1.longitude;
//        xD2 = p4.longitude - p3.longitude;
//        yD1 = p2.latitude - p1.latitude;
//        yD2 = p4.latitude - p3.latitude;
//        xD3 = p1.longitude - p3.longitude;
//        yD3 = p1.latitude - p3.latitude;
//
//        // calculate the lengths of the two lines
//        len1 = sqrt(xD1*xD1+yD1*yD1);
//        len2 = sqrt(xD2*xD2+yD2*yD2);
//
//        // calculate angle between the two lines.
//        dot = (xD1*xD2+yD1*yD2); // dot product
//        deg = dot/(len1*len2);
//
//        // if abs(angle)==1 then the lines are parallell,
//        // so no intersection is possible
//        if(abs(deg)==1) return null;
//
//        // find intersection Pt between two lines
//        LatLng pt = new LatLng(0,0);
//        div = yD2*xD1-xD2*yD1;
//        ua = (xD2*yD3-yD2*xD3)/div;
//        ub = (xD1*yD3-yD1*xD3)/div;
//        pt.longitude = p1.longitude+ua*xD1;
//        pt.latitude = p1.latitude+ua*yD1;
//
//        // calculate the combined length of the two segments
//        // between Pt-p1 and Pt-p2
//        xD1=pt.longitude - p1.longitude;
//        xD2=pt.longitude - p2.longitude;
//        yD1=pt.latitude - p1.latitude;
//        yD2=pt.latitude -p2.latitude;
//        segmentLen1=sqrt(xD1*xD1+yD1*yD1)+sqrt(xD2*xD2+yD2*yD2);
//
//        // calculate the combined length of the two segments
//        // between Pt-p3 and Pt-p4
//        xD1=pt.longitude - p3.longitude;
//        xD2=pt.longitude - p4.longitude;
//        yD1=pt.latitude - p3.latitude;
//        yD2=pt.latitude - p4.latitude;
//        segmentLen2=sqrt(xD1*xD1+yD1*yD1)+sqrt(xD2*xD2+yD2*yD2);
//
//        // if the lengths of both sets of segments are the same as
//        // the lenghts of the two lines the point is actually
//        // on the line segment.
//
//        // if the point isn’t on the line, return null
//        if(abs(len1-segmentLen1)>0.01 || abs(len2-segmentLen2)>0.01)
//            return null;
//
//        // return the valid intersection
//        return pt;
//    }
}
