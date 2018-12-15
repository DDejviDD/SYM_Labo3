package ch.heigvd.iict.sym.a3dcompassapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AuthNFCActivity extends AppCompatActivity implements ActivityWithNFC {

    public static final String TAG = "test";
    public static final String MIME_TEXT_PLAIN = "text/plain";

    private NfcAdapter mNfcAdapter;
    private Button maxSecurity;
    private Button mediumSecurity;
    private Button lowSecurity;
    private TextView securityLevelField;
    private Boolean maxPrivileges = true;
    private Boolean mediumPrivileges = true;
    private Boolean lowPrivileges = true;
    private CountDownTimer countDown;
    private int securityLevel = AUTHENTICATE_MAX;

    private static final int INITIAL_TIME = 60 * 1000;
    private static final int COUNTDOWN_INTERVAL = 6 * 1000;
    private static final int AUTHENTICATE_MAX = 10;
    private static final int AUTHENTICATE_MEDIUM = 6;
    private static final int AUTHENTICATE_LOW = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_auth);

        // Link UI element to the activity
        maxSecurity = findViewById(R.id.buttonMaxSecurity);
        mediumSecurity = findViewById(R.id.buttonMediumSecurity);
        lowSecurity = findViewById(R.id.buttonLowSecurity);
        securityLevelField = findViewById(R.id.nfcSecurityLevel);


        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        handleIntent(getIntent());

        // Set and launch our countDown
        securityLevelField.setText("Security Level: " + String.valueOf(securityLevel));
        countDown = new CountDownTimer(INITIAL_TIME, COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long l) {
                securityLevelField.setText("Security Level: " + String.valueOf(--securityLevel));
                if (securityLevel > AUTHENTICATE_MEDIUM) {
                    maxPrivileges = true;
                    mediumPrivileges = true;
                    lowPrivileges = true;
                } else if (securityLevel > AUTHENTICATE_LOW) {
                    maxPrivileges = false;
                    mediumPrivileges = true;
                    lowPrivileges = true;
                } else {
                    maxPrivileges = false;
                    mediumPrivileges = false;
                    lowPrivileges = true;
                }
            }

            @Override
            public void onFinish() {
                maxPrivileges = false;
                mediumPrivileges = false;
                lowPrivileges = false;
                securityLevelField.setText("You have no more permission, re-use your NFC card");
            }
        };
        countDown.start();

        // Buttons click event management
        maxSecurity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(maxPrivileges)
                    Toast.makeText(AuthNFCActivity.this, R.string.access_with_sufficient_privileges , Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(AuthNFCActivity.this, R.string.access_without_sufficient_privileges , Toast.LENGTH_LONG).show();
            }
        });

        mediumSecurity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(mediumPrivileges)
                    Toast.makeText(AuthNFCActivity.this, R.string.access_with_sufficient_privileges , Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(AuthNFCActivity.this, R.string.access_without_sufficient_privileges , Toast.LENGTH_LONG).show();
            }
        });

        lowSecurity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(lowPrivileges)
                    Toast.makeText(AuthNFCActivity.this, R.string.access_with_sufficient_privileges , Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(AuthNFCActivity.this, R.string.access_without_sufficient_privileges , Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        stopForegroundDispatch(this, mNfcAdapter);

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NFCReader(this).execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        }
    }


    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }



    // The interface method (the after NFC tag reading behavior of the activity)
    @Override
    public void doThisWhenNfcTagRead(String readingResult) {
        countDown.cancel();

        maxPrivileges = true;
        mediumPrivileges = true;
        lowPrivileges = true;

        securityLevel = AUTHENTICATE_MAX;
        securityLevelField.setText("Security Level: " + String.valueOf(securityLevel));

        countDown.start();
    }

}
