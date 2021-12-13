package com.example.openweather;

import android.location.Address;
import android.location.Geocoder;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.List;

public class GeoLocationGetLocationRunnable implements Runnable {

    private final MainActivity mainActivity;
    private final String location;

    GeoLocationGetLocationRunnable(MainActivity mainActivity, String location) {
        this.mainActivity = mainActivity;
        this.location = location;
    }

    @Override
    public void run() {
        final String result = getLocationName(location);
        mainActivity.runOnUiThread(() -> mainActivity.handleLocationResult(result));
    }

    @Nullable
    private String getLocationName(String userProvidedLocation) {
        Geocoder geocoder = new Geocoder(mainActivity);
        try {
            List<Address> address =
                    geocoder.getFromLocationName(userProvidedLocation, 1);

            if (address == null || address.isEmpty()) {
                return null;
            }

            Address addressItem = address.get(0);

            String country = addressItem.getCountryCode();
            String p1;
            String p2;

            if (country.equals("US")) {
                p1 = addressItem.getFeatureName();
                p2 = addressItem.getAdminArea();
            } else {
                p1 = addressItem.getLocality();
                if (p1 == null)
                    p1 = addressItem.getFeatureName();
                p2 = addressItem.getCountryName();
            }

            if (p1 == null || p1.isEmpty()) return null;
            if (p2 == null || p2.isEmpty()) return null;

            return p1 + ", " + p2;
        } catch (IOException e) {
            return null;
        }
    }
}
