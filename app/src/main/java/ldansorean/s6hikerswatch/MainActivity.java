package ldansorean.s6hikerswatch;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final String DETAILS_FORMAT = "Latitude: %.1f \n\r"
                                                + "Longitude: %.1f \n\r"
                                                + "Accuracy: %.1f \n\r"
                                                + "Speed: %.1f \n\r"
                                                + "Bearing: %.1f \n\r"
                                                + "Altitude: %.1f \n\r"
                                                + "Address: %s \n\r";

    private TextView detailsView;
    private LocationManager locationManager;
    private String locationProvider;
    private Geocoder geocoder;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        detailsView = findViewById(R.id.details);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationProvider = locationManager.getBestProvider(new Criteria(), true);
        geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);

        Location initialLocation = locationManager.getLastKnownLocation(locationProvider);
        showDetails(initialLocation);
    }

    private void showDetails(Location loc) {
        String address = "";
        try {
            address = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1).get(0).getAddressLine(0);
        } catch (IOException e) {
            ; // not showing any address
        }
        String details = String.format(DETAILS_FORMAT, loc.getLatitude(), loc.getLongitude(), loc.getAccuracy(), loc.getSpeed(), loc.getBearing(), loc.getAltitude(), address);
        detailsView.setText(details);
    }

    @Override
    public void onLocationChanged(Location location) {
            // display location
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
