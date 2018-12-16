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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// This code is greatly inspired by : https://code.tutsplus.com/tutorials/reading-nfc-tags-with-android--mobile-17278
public class NFCActivity extends AppCompatActivity implements ActivityWithNFC {

    public static final String TAG = "test";
    public static final String MIME_TEXT_PLAIN = "text/plain";

    private NfcAdapter mNfcAdapter;
    private EditText username;
    private EditText password;
    private Button connect;
    private TextView NFCtimer;
    private int NFCounter = -1;
    private CountDownTimer countDown;

    private static final int MAX_DELAY_ALLOWED = 60 * 1000;
    private static final String dummyUsername = "Hector";
    private static final String dummyPassword = "LeCastor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        // Map attribute to the views elements
        NFCtimer = findViewById(R.id.nfctimer);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        connect = findViewById(R.id.buttonConnect);

        // Control if the NFC is available
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;

        }
        if (!mNfcAdapter.isEnabled()) {
            NFCtimer.setText("NFC is disabled.");
        }

        countDown = new CountDownTimer(MAX_DELAY_ALLOWED, 1000) {
            @Override
            public void onTick(long l) {
                NFCtimer.setText(String.valueOf(--NFCounter));
            }

            @Override
            public void onFinish() {
                NFCtimer.setText("Too late, you must re-use your NFC card");
                NFCounter = -1; // Just in case
            }
        };

        handleIntent(getIntent());

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check the max delay between the reading of the NFC card and the login attempt
                if (NFCounter > 0) {
                    // Check the credentials
                    if (username.getText().toString().equals(dummyUsername.toString()) && password.getText().toString().equals(dummyPassword)) {
                        Toast.makeText(NFCActivity.this, "Authentication is well done !", Toast.LENGTH_LONG).show();
                        // Start nested activity
                        Intent intent = new Intent(NFCActivity.this, AuthNFCActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(NFCActivity.this, "Your credentials are incorrect", Toast.LENGTH_LONG).show();

                    }
                }
                else {
                    Toast.makeText(NFCActivity.this, "You must use an NFC card before using the connect button", Toast.LENGTH_LONG).show();
                }
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


    // Method from our interface ActivityWithNfc
    @Override
    public void doThisWhenNfcTagRead(String readingResult) {
        if (readingResult.equals(TAG)) {
            NFCounter = 60;
            NFCtimer.setText(String.valueOf(NFCounter));
            countDown.cancel();
            countDown.start();
        }
        else {
            Toast.makeText(NFCActivity.this, "Your NFC tag is incorrect !", Toast.LENGTH_LONG).show();
        }

    }

}
