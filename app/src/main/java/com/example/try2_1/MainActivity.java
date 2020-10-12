package com.example.try2_1;
import java.lang.Math;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.YuvImage;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {
    Button r;
    Button p;
    TextView fin;
    private Handler handler = new Handler();
    TextView textView;
    private long pauseoffset;
    boolean running;
Chronometer ch;
    int stepDetector ;
long avg;
    long time1 = 0,time2,diff,l;
    SensorManager sm;
    Sensor s;
    SensorEvent event;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;

    Map<String ,Object>obj=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*---------------------------------progress bar start------------------------------------------------------------------*/
/*
        Resources resources = getResources();
        Drawable drawable = resources.getDrawable(R.drawable.circularprogressbar);
        final ProgressBar progressBar = findViewById(R.id.circularProgressbar);
        progressBar.setProgress(0);
        progressBar.setSecondaryProgress(100);
        progressBar.setMax(100);
        progressBar.setProgressDrawable(drawable);
        textView = findViewById(R.id.textView);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (status < 100) {
                    status += 1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(status);
                            textView.setText(String.format("%d%%", status));
                        }
                    });
                    try {
                        Thread.sleep(16);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
*/
/*------------------------------progress bar End--------------------------------------------------------*/
        sm=(SensorManager) getSystemService(SENSOR_SERVICE);
        s=sm.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        r=(Button)findViewById(R.id.res);
        p=(Button) findViewById(R.id.pau);
        ch=findViewById(R.id.chronometer);
        fin=(TextView) findViewById(R.id.fi);
        r.setOnClickListener(this);
        p.setOnClickListener(this);
        fauth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();







        Resources resources = getResources();
        Drawable drawable = resources.getDrawable(R.drawable.circularprogressbar);
        final ProgressBar progressBar = findViewById(R.id.circularProgressbar);
        progressBar.setProgress(0);
        progressBar.setSecondaryProgress(100);
        progressBar.setMax(100);
        progressBar.setProgressDrawable(drawable);
        textView = findViewById(R.id.textView);


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (stepDetector <= 100) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(stepDetector);

                        }
                    });
                    try {
                        Thread.sleep(16);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();



    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {


            case R.id.res:

                sm.registerListener(this,s,SensorManager.SENSOR_DELAY_NORMAL);
                Toast.makeText(getApplicationContext(),"RESUME",Toast.LENGTH_SHORT).show();
                if(!running) {
                                 time1=SystemClock.elapsedRealtimeNanos();
                                 ch.setBase(SystemClock.elapsedRealtime()-pauseoffset);


                        /*         Map<String,Object>obj=new HashMap<>();

                                 fstore.collection("Data").document("step detection").set(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                     @Override
                                     public void onSuccess(Void aVoid) {
                                         Toast.makeText(getApplicationContext(),"Updated successfully",Toast.LENGTH_SHORT).show();
                                     }
                                 }).addOnFailureListener(new OnFailureListener() {
                                     @Override
                                     public void onFailure(@NonNull Exception e) {
                                         Toast.makeText(getApplicationContext(),"plzz try again", Toast.LENGTH_SHORT);
                                     }
                                 });


                                 */
                                 ch.start();
                                 running =true;
                            }

                break;
            case R.id.pau:
                sm.unregisterListener(this,s);
                Toast.makeText(getApplicationContext(),"pause",Toast.LENGTH_SHORT).show();
                if(running)
                {   time2=SystemClock.elapsedRealtimeNanos();
                    ch.stop();
                    running=false;
                    pauseoffset=SystemClock.elapsedRealtime()-ch.getBase();
                    //avg=(long)(stepDetector*0.765)/(time2-time1);
                    avg= (long) ((long) (stepDetector*0.765)/(((time2-time1)*Math.pow(10,-9))));
                    textView.setText(String.valueOf(avg)+" M/s");


                    String fspeed= textView.getText().toString();
                   // Map<String ,Object>obj=new HashMap<>();
                    obj.put("speed",fspeed);

                    fstore.collection("Step detectator").document("Data").set(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Updated successfully",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"plzz try again", Toast.LENGTH_SHORT);
                        }
                    });
                }

    
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        switch (sensorEvent.sensor.TYPE_STEP_DETECTOR) {
            case Sensor.TYPE_STEP_DETECTOR:
                stepDetector++;

                if(stepDetector==1)
                {
                    diff=0;
                }

                fin=(TextView) findViewById(R.id.fi);
                Toast.makeText(getApplicationContext(),"sensor changed",Toast.LENGTH_SHORT).show();
                fin.setText((String.valueOf((stepDetector))));

        }
        String fstep= (String) fin.getText();
       // Map<String,Object> obj1=new HashMap<>();
        obj.put("Steps",fstep);

        fstore.collection("Step detectator").document("Data").set(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"updated successfully",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"plzz try again",Toast.LENGTH_SHORT).show();
            }
        });

    }





    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}