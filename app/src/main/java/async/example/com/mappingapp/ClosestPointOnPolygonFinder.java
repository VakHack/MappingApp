package async.example.com.mappingapp;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Administrator on 07/12/2017.
 */

public class ClosestPointOnPolygonFinder extends AsyncTask<Void, Void, Double> {

    private List<LatLng> polygonBounds;
    private LatLng marker;

    private LatLng absClosest = null;
    private LatLng firstPairSec = null;
    private LatLng secPairSec = null;


    public ClosestPointOnPolygonFinder(List<LatLng> polygonBounds, LatLng marker) {
        this.polygonBounds = polygonBounds;
        this.marker = marker;
    }

    @Override
    protected Double doInBackground(Void... voids) {

        //return null in case the polygon has less than two points
        if(polygonBounds.size() < 2) return null;

        findClosestCouple();
        findClosetPointBetweenClosestCouple();

        return DistanceCalculator.coordinatesDistance(marker, absClosest);
    }

    private void findClosestCouple(){

        //init min point holders to the first two points
        absClosest = DistanceCalculator.min(marker, polygonBounds.get(0), polygonBounds.get(1));
        firstPairSec = DistanceCalculator.max(marker, polygonBounds.get(0), polygonBounds.get(1));

        for(int i = 1; i < polygonBounds.size() - 1; ++i){

            //for each couple of point, running on bounds points in order, finding the closest point to touch point
            LatLng localMin = DistanceCalculator.min(marker, polygonBounds.get(i), polygonBounds.get(i + 1));
            LatLng localMax = DistanceCalculator.max(marker, polygonBounds.get(i), polygonBounds.get(i + 1));

            //check if absolute minimum and local minimum are the same
            if(absClosest.longitude == localMin.longitude && absClosest.latitude == localMin.latitude){

                //keeping the second pair containing the minimal point
                secPairSec = localMax;

            } else if(localMin == DistanceCalculator.min(marker, localMin, absClosest)){

                absClosest = localMin;
                firstPairSec = localMax;
            }
        }
    }

    private LatLng findClosestPointBetweenPair(LatLng sec, double cycles){

        LatLng localClosest = new LatLng(absClosest.latitude, absClosest.longitude);

        for(int i = 0; i < cycles; ++i){

            LatLng mid = DistanceCalculator.midPoint(localClosest, sec);

            LatLng prevClosest = new LatLng(localClosest.latitude, localClosest.longitude);

            localClosest = DistanceCalculator.min(marker, prevClosest, mid);
            sec = DistanceCalculator.max(marker, prevClosest, mid);
        }

        return localClosest;
    }

    private void findClosetPointBetweenClosestCouple(){

        final double PROXIMATION_CYCLES = Math.sqrt(DistanceCalculator.coordinatesDistance(absClosest, firstPairSec) / 2);

        //finding tne minimal middle-points in both pair of bound-points that contains the closest point (from both its sides)
        //and picking the closest of them
        absClosest = DistanceCalculator.min(marker, findClosestPointBetweenPair(firstPairSec, PROXIMATION_CYCLES)
        ,findClosestPointBetweenPair(secPairSec, PROXIMATION_CYCLES));
    }

    public LatLng getClosest() {
        return absClosest;
    }
}
