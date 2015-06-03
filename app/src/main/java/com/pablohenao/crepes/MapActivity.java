package com.pablohenao.crepes;

import android.location.Location;
import android.location.LocationListener;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private GoogleMap map;
    private CameraUpdate cameraUpdate;

    private final LatLng LOCATION_HOME = new LatLng(6.260768,-75.601676);
    private final LatLng LOCATION_U = new LatLng(6.267577,-75.568995);

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private LatLng currentLocation;

    private boolean newLocationReady = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();

        map.addMarker(new MarkerOptions()
                .position(LOCATION_HOME)
                .title("My Home")
                .snippet("Where the maginc Happens")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        map.addMarker(new MarkerOptions()
                .position(LOCATION_U)
                .title("Universidad de Antioquia")
                .snippet("The best of the best of the best")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        buildGoogleApiClient();
        createLocationRequest();

    }

    public void onClickHome(View view){
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(LOCATION_HOME, 16);
        map.animateCamera(cameraUpdate);
    }
    public void onClickU(View view){
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(LOCATION_U,16);
        map.animateCamera(cameraUpdate);
    }

    public void onClickCurrent(View view){
        if(newLocationReady){
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation,16);
            map.animateCamera(cameraUpdate);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation!=null){
            setNewLocation(mLastLocation);
            newLocationReady=true;
        }
        /*else{
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        }*/

    }

    @Override
    public void onConnectionSuspended(int i) {



    }

    @Override
    public void onLocationChanged(Location location) {

        setNewLocation(location);
        newLocationReady=true;


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

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void setNewLocation(Location location){
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        currentLocation = new LatLng(latitude,longitude);
        map.addMarker(new MarkerOptions()
                .position(currentLocation)
                .title("Im here, Bitch")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }
}