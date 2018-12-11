package ch.heigvd.iict.sym.a3dcompassapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


// Source: https://altbeacon.github.io/android-beacon-library/samples.html

public class BeaconActivity extends AppCompatActivity implements BeaconConsumer {


    private Button listingBeaconsButton;

    protected static final String TAG = "MonitoringActivity";
    private BeaconManager beaconManager;

    private ListView listView;
    private TextView text;
    List<String> data = new ArrayList();


    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    /*adapter = new ArrayAdapter<String>(this,
    android.R.layout.simple_list_item_1, data);
    listView.setAdapter(adapter);
    adapter.notifyDataSetChanged();*/

    final Activity activity = this; // a reference to the actual Activity

    @Override
    protected void onCreate(Bundle instanceState) {
        super.onCreate(instanceState);
        setContentView(R.layout.activity_beacon);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            // Android M Permission check

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle("This app needs location access");
                    builder.setMessage("Please grant location access so this app can detect beacons.");
                    builder.setPositiveButton(android.R.string.ok, null);

                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {


                        @Override

                        public void onDismiss(DialogInterface dialog) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                            }

                        }


                    });

                    builder.show();

                }
            }

        }

        listingBeaconsButton = findViewById(R.id.button_beacon);
        listView = findViewById(R.id.listview_beacon);
        text = findViewById(R.id.textview_beacon);

        beaconManager = BeaconManager.getInstanceForApplication(activity);

        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        listingBeaconsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                beaconManager.bind((BeaconConsumer) activity);

                onBeaconServiceConnect(); // executes detection of iBeacon devices
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        beaconManager.unbind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //beaconManager.bind(this);
    }

    @Override
    public void onBeaconServiceConnect() {

        RangeNotifier rangeNotifier = new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Log.d(TAG, "Beacons detected:  "+ beacons.size());
                    Iterator beaconIterator = beacons.iterator();
                    while(beaconIterator.hasNext()) {
                        Beacon current = (Beacon) beaconIterator.next();
                        logToDisplay("Beacon detected :" + "\n" + "RSSI : " + current.getRssi() + "\n" +
                                "Distance : " + current.getDistance() + " meters away."  + "\n" + "Minor : "
                                + current.getId2() + "\n" + "Major : " + current.getId3());
                    }
                }
            }

        };
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            beaconManager.addRangeNotifier(rangeNotifier);
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            beaconManager.addRangeNotifier(rangeNotifier);
        } catch (RemoteException e) {   }

    }

    private void logToDisplay(final String line) {
        runOnUiThread(new Runnable() {
            public void run() {
                text.setText(line+"\n"+text.getText().toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

}
