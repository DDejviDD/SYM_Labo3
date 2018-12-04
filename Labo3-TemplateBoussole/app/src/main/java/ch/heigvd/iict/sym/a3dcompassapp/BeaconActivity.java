package ch.heigvd.iict.sym.a3dcompassapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
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
import java.util.List;


// Source: https://altbeacon.github.io/android-beacon-library/samples.html

public class BeaconActivity extends AppCompatActivity implements BeaconConsumer{


    private Button listingBeaconsButton;

    protected static final String TAG = "MonitoringActivity";
    private BeaconManager beaconManager;

    private ListView listView;
    private TextView text;
    List<String> data = new ArrayList();


    /*adapter = new ArrayAdapter<String>(this,
    android.R.layout.simple_list_item_1, data);
    listView.setAdapter(adapter);
    adapter.notifyDataSetChanged();*/

    final Activity activity = this; // a reference to the actual Activity

    @Override
    protected void onCreate(Bundle instanceState) {
        super.onCreate(instanceState);
        setContentView(R.layout.activity_beacon);


        listingBeaconsButton = findViewById(R.id.button_beacon);
        listView =  findViewById(R.id.listview_beacon);
        text =  findViewById(R.id.textview_beacon);

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
        beaconManager.unbind((BeaconConsumer) activity);
    }


    @Override
    protected void onPause() {
        super.onPause();
        beaconManager.unbind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        beaconManager.bind(this);
    }

    @Override
    public void onBeaconServiceConnect() {

        RangeNotifier rangeNotifier = new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Log.d(TAG, "didRangeBeaconsInRegion called with beacon count:  "+beacons.size());
                    Beacon firstBeacon = beacons.iterator().next();
                    logToDisplay("The first beacon " + firstBeacon.toString() + " is about " + firstBeacon.getDistance() + " meters away.");
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
                text.append(line+"\n");
            }
        });
    }
}
