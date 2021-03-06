package com.paperflywings.user.paperflyv0.DeliveryApp.LocationService;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

public class GPStracker implements LocationListener {
    Context context;
    public GPStracker(Context c)
    {
        context = c;
    }

    public Location getLocation(){

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
//             Toast.makeText(context,"Permission not Granted", Toast.LENGTH_SHORT).show();
            return null;
        }

        LocationManager lm = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);


        if(isGPSEnabled){
            lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,0,0,this);
            Location l = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            return l;

        }else{
//            Toast.makeText(context,"Please enable gps", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    @Override
    public void onLocationChanged(Location location) {

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
