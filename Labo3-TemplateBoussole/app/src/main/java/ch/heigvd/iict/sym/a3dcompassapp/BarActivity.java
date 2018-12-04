package ch.heigvd.iict.sym.a3dcompassapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BarActivity  extends AppCompatActivity {

    private Button scanningButton;

    @Override
    protected void onCreate(Bundle instanceState) {
        super.onCreate(instanceState);
        setContentView(R.layout.activity_bar);

        scanningButton = findViewById(R.id.button_bar);
        final Activity activity = this; // a reference to the actual Activity


        scanningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(activity).initiateScan(); // references the current Activity
            }
        });
    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
