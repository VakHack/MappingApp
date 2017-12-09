package async.example.com.mappingapp;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Administrator on 08/12/2017.
 */

public class WithinPolygonChecker extends AsyncTask<LatLng, Void, Boolean> {

    private List<LatLng> boundaries;
    private int intersectionRange;

    public WithinPolygonChecker(List<LatLng> boundaries, int intersectionRange) {
        this.boundaries = boundaries;
        this.intersectionRange = intersectionRange;
    }

    @Override
    protected Boolean doInBackground(LatLng... latLngs) {
        return isWithin(latLngs[0]);
    }

    private boolean isIntersect(LatLng startPoint, LatLng endPoint){

        LatLng[] firstLine = {startPoint, endPoint};

        for(int i = 0; i < boundaries.size() - 1; ++i){

            LatLng[] secLine = {boundaries.get(i), boundaries.get(i + 1)};

            if(IntersectionFinder.isIntersect(firstLine, secLine)) return true;
        }

        return false;
    }

    private boolean isWithin(LatLng latLng) {

        //checking if the line leaving latLng towards north, east, south and west intersects with polygon
        LatLng latLow = new LatLng(latLng.latitude - intersectionRange, latLng.longitude);
        LatLng latHi = new LatLng(latLng.latitude + intersectionRange, latLng.longitude);

        LatLng lngLow = new LatLng(latLng.latitude, latLng.longitude - intersectionRange);
        LatLng lngHi = new LatLng(latLng.latitude, latLng.longitude + intersectionRange);

        boolean allSidesIntersection = isIntersect(latLng, latLow)
                && isIntersect(latLng, latHi)
                && isIntersect(latLng, lngLow)
                && isIntersect(latLng, lngHi);

        Log.i("maplog", String.valueOf(allSidesIntersection));

        return allSidesIntersection;
    }
}
