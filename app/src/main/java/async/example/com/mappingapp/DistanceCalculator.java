package async.example.com.mappingapp;


import com.google.android.gms.maps.model.LatLng;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticMeasurement;
import org.gavaghan.geodesy.GlobalPosition;

/**
 * Created by Administrator on 07/12/2017.
 */

public class DistanceCalculator {

    //for the following calculation, i used a git repo:
    // https://github.com/niqueco/geodesy
    public static double coordinatesDistance(LatLng first, LatLng sec){

        //ignoring elevation factor in distance calculation
        final int ELEVATION = 1;

        GlobalPosition firstPos = new GlobalPosition(first.latitude, first.longitude, ELEVATION);
        GlobalPosition secPos = new GlobalPosition(sec.latitude, sec.longitude, ELEVATION);

        GeodeticCalculator calc = new GeodeticCalculator();
        GeodeticMeasurement res = calc.calculateGeodeticMeasurement(Ellipsoid.GRS80, firstPos, secPos);

        return res.getPointToPointDistance();
    }

    public static LatLng min(LatLng cmpTo, LatLng first, LatLng sec){

        if(coordinatesDistance(cmpTo, first) < coordinatesDistance(cmpTo, sec)) return first;
        else return sec;
    }

    public static LatLng max(LatLng cmpTo, LatLng first, LatLng sec){

        if(coordinatesDistance(cmpTo, first) > coordinatesDistance(cmpTo, sec)) return first;
        else return sec;
    }

    public static LatLng midPoint(LatLng first, LatLng sec){

        double mx = (first.longitude + sec.longitude)/2;
        double my = (first.latitude + sec.latitude)/2;

        return new LatLng(mx,my);
    }
}
