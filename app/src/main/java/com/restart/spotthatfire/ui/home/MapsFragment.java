package com.restart.spotthatfire.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.restart.spotthatfire.R;
import com.restart.spotthatfire.util.DateUtils;
import com.restart.spotthatfire.util.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap mMap;
    private int counter = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        view.findViewById(R.id.main_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 999);
                mMap.setMyLocationEnabled(true);

                LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
                @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));
                if (location != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(location.getLatitude(), location.getLongitude()))
                            .zoom(8)
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        });

        view.findViewById(R.id.layer_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(counter % 5);
                counter++;
            }
        });
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Activity activity = requireActivity();
        mMap = googleMap;

        mMap.setTrafficEnabled(true);
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {
                try {
                    // Getting view from the layout file infowindowlayout.xml
                    View v = getLayoutInflater().inflate(R.layout.infowindowlayout, null);
                    ImageView im = v.findViewById(R.id.imageView1);
                    TextView tv1 = v.findViewById(R.id.textView1);
                    TextView tv2 = v.findViewById(R.id.textView2);
                    String title=arg0.getTitle();
                    String informations=arg0.getSnippet();

                    tv1.setText(title);
                    tv2.setText(informations);
                    if (arg0.getTag() != null)
                        Picasso.get().load(arg0.getTag().toString()).into(im);
                    return v;
                } catch (Exception e) {

                }

                return null;
            }
        });


        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 999);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setCompassEnabled(true);

        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));
        if (location != null) {
            try {
                InputStream ins = getResources().openRawResource(getResources().getIdentifier("filename", "raw", requireActivity().getPackageName()));
                BufferedReader br = new BufferedReader(new InputStreamReader(ins));
                while (br.readLine() != null) {
                    String[] values = br.readLine().split(",");

                    LatLng newPosition = new LatLng(Double.valueOf(values[0]), Double.valueOf(values[1]));

                    mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_fire))
                            .anchor(0.5f, 0.5f)
                            .position(newPosition)
                            .title("Date: " + values[5])
                            .snippet("Brightness: " + values[2]));

                    double magnitude = Double.valueOf(values[3]) + Double.valueOf(values[4]);
                    int stroke, fill;

                    /* Circles color depends on the magnitude of an earth quake. */
                    if (magnitude > 3) {
                        stroke = ContextCompat.getColor(activity, R.color.strokeCircleDanger);
                        fill = ContextCompat.getColor(activity, R.color.fillCircleDanger);
                    } else if (magnitude > 2) {
                        stroke = ContextCompat.getColor(activity, R.color.strokeCircleWarning);
                        fill = ContextCompat.getColor(activity, R.color.fillCircleWarning);
                    } else {
                        stroke = ContextCompat.getColor(activity, R.color.strokeCircleSafe);
                        fill = ContextCompat.getColor(activity, R.color.fillCircleSafe);
                    }

                    /* Circles size depends slightly on the magnitude of an earth quake. */
                    mMap.addCircle(new CircleOptions()
                            .center(newPosition)
                            .radius(10000 * magnitude)
                            .strokeColor(stroke)
                            .fillColor(fill));
                }
            } catch (IOException | NullPointerException e) {

            }
        }

        try {
            Uri earthQuakesUri = Uri.parse("https://c3xtvwukzg.execute-api.us-west-2.amazonaws.com/default/getUserReports");
            URL earthQuakesURL = new URL(earthQuakesUri.toString());
            new EarthQuakesAsyncTask().execute(earthQuakesURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 999) {
            if (permissions.length == 1 && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    private class EarthQuakesAsyncTask extends AsyncTask<URL, Void, Void> {
        List<String> lat = new ArrayList<>();
        List<String> lng = new ArrayList<>();
        List<String> pic = new ArrayList<>();
        List<String> time = new ArrayList<>();

        /**
         * Start the background process which includes the network call and the parsing of resulted
         * JSON. More network calls will be make for each earth quake to find their respective addresses.
         *
         * @param params Incoming URL to open an http connection with
         * @return N/A
         */
        @Override
        protected Void doInBackground(URL... params) {


            try {
                String resultsJSON = NetworkUtils.HttpResponse(params[0]);
                JSONArray arr = new JSONArray(resultsJSON);


                for (int i = 0; i < arr.length(); ++i) {
                    JSONObject oneEarthQuake = arr.getJSONObject(i);
                    lat.add(oneEarthQuake.getString("latitude"));
                    lng.add(oneEarthQuake.getString("longitude"));
                    pic.add(oneEarthQuake.getString("s3Path"));
                    time.add(oneEarthQuake.getString("timestamp"));
                }
            } catch (IOException e) {
                Log.e("", "Unable in grabbing data");
                e.printStackTrace();
            } catch (SecurityException e) {
                Log.e("", "Permission to access internet was denied");
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e("", "Unable in parsing data");
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void ignored) {
            for (int i = 0; i < lat.size(); ++i) {
                LatLng newPosition = new LatLng(Double.valueOf(lat.get(i)), Double.valueOf(lng.get(i)));

                mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person))
                        .anchor(0.5f, 0.5f)
                        .position(newPosition)
                        .title("Date: Just Now!")).setTag(pic.get(i));

                int stroke =  ContextCompat.getColor(requireActivity(), R.color.strokeCircleUser);
                int fill =  ContextCompat.getColor(requireActivity(), R.color.userCircleColor);

                /* Circles size depends slightly on the magnitude of an earth quake. */
                mMap.addCircle(new CircleOptions()
                        .center(newPosition)
                        .radius(10000 * 2.5)
                        .strokeColor(stroke)
                        .fillColor(fill));
            }
        }
    }
}