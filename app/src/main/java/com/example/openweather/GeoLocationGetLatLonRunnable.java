package com.example.openweather;

import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;

public class GeoLocationGetLatLonRunnable implements Runnable {

    private final MainActivity mainActivity;
    private final String location;

    GeoLocationGetLatLonRunnable(MainActivity mainActivity, String location) {
        this.mainActivity = mainActivity;
        this.location = location;
    }

    @Override
    public void run() {
        final double[] result = getLatLon(location);
        mainActivity.runOnUiThread(() -> mainActivity.handleLatLonResult(result));
    }

    private double[] getLatLon(String userProvidedLocation) {
        Geocoder geocoder = new Geocoder(mainActivity);
        try {
            List<Address> address =
                    geocoder.getFromLocationName(userProvidedLocation, 1);

            if (address == null || address.isEmpty()) {
                return null;
            }

            double lat = address.get(0).getLatitude();
            double lon = address.get(0).getLongitude();

            return new double[] {lat, lon};

        } catch (IOException e) {
            return null;
        }
    }
}
