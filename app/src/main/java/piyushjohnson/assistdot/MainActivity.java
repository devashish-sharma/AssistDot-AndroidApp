package piyushjohnson.assistdot;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.StackView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    ArrayList<String> list;
    StackView stackView;
    ProgressBar progressBar;
    Button button;
    TextView textView;
    StackAdapter stackAdapter;
    CountDownTimer countDownTimer;
    int minute,second;
    int interval,progress = 0,total_progress = 100;
    boolean timer_pause = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<String>();
        populateList();

        stackView = (StackView) findViewById(R.id.stackView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textView = (TextView) findViewById(R.id.textView);
        button = (Button)findViewById(R.id.button);
        stackAdapter = new StackAdapter(MainActivity.this,list);
        stackView.setAdapter(stackAdapter);
        stackAdapter.notifyDataSetChanged();
        button.setOnTouchListener(this);
    }

    public void onCount(View view)
    {
        boolean state = ((CheckBox) view).isChecked();
        if(state)
        {
            if(timer_pause)
            {
                startTimer(minute,second);
            }
            else
            {
                startTimer(2,24);
            }
        }
        else
        {
            timer_pause = true;
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    public void startTimer(int min, int sec)
    {
       final long total_time = (sec * 1000) + (min * 60 * 1000);
       final int total_sec = (int) total_time/1000;
        countDownTimer = new CountDownTimer(total_time,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
//                StringBuilder time = new StringBuilder();
//                TimeUtils.formatDuration(millisUntilFinished,time);
                textView.setText(minute + " : " + second);
                interval = (int) millisUntilFinished/1000;
                second =(int) millisUntilFinished/1000 % 60;
                minute = interval / 60;
                total_progress = progress = (interval * 100)/total_sec;
                Log.d("App:",String.valueOf((interval * 100)/total_sec));
                progressBar.setProgress(total_progress);
            }
            @Override
            public void onFinish() {
                progressBar.setProgress(100);
                textView.setText("00:00");
            }
        }.start();
    }

    public void populateList()
    {
        list.add("Running Marathon");
        list.add("Watching Movie");
        list.add("Playing Cricket");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_UP:
                startActivity(new Intent(MainActivity.this,Home.class));
                return true;
            default:
                return false;
        }
    }
}
