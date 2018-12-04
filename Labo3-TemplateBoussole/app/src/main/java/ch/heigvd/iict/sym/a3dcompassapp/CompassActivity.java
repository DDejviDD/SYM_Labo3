package ch.heigvd.iict.sym.a3dcompassapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class CompassActivity extends AppCompatActivity implements SensorEventListener {

    //opengl
    private OpenGLRenderer  opglr           = null;
    private GLSurfaceView   m3DView         = null;


    // Sensors
    private SensorManager manager;
    private Sensor acceleratorSensor;
    private Sensor magneticFieldSensor;

    // Data for the matrix
    private float[] r = new  float[16];
    private float[] i = null;
    private float[] gravity = new float[3];
    private float[] geomagnetic = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // we need fullscreen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // we initiate the view
        setContentView(R.layout.activity_compass);

        // link to GUI
        this.m3DView = findViewById(R.id.compass_opengl);

        //we create the 3D renderer
        this.opglr = new OpenGLRenderer(getApplicationContext());

        //init opengl surface view
        this.m3DView.setRenderer(this.opglr);

        // Sensor management
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acceleratorSensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticFieldSensor = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.registerListener(this, acceleratorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        manager.registerListener(this, magneticFieldSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        switch (mySensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                gravity = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                geomagnetic = event.values.clone();
                break;
            default:
                break;
        }
        SensorManager.getRotationMatrix(r, i, gravity, geomagnetic);
        this.opglr.swapRotMatrix(r);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing.
    }
}
