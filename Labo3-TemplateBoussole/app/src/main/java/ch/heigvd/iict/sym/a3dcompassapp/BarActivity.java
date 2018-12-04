package ch.heigvd.iict.sym.a3dcompassapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class BarActivity  extends AppCompatActivity implement{

    private Button scanningButton;

    @Override
    protected void onCreate(Bundle instanceState) {
        super.onCreate(instanceState);
        setContentView(android.R.layout.);

        scanningButton = findViewById(R.id.button_bar);


        scanningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}
