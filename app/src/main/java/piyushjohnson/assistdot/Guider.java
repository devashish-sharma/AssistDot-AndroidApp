package piyushjohnson.assistdot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.support.design.widget.TextInputLayout;
import android.widget.ImageView;

import com.idescout.sql.SqlScoutServer;

public class Guider extends AppCompatActivity {

    ViewPager viewPager;
    PrefManager prefManager;
    Button button;
    Bundle bundle;
    Intent intent;
    int pos,len;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_guider);
        SqlScoutServer.create(this, getPackageName());
        intent = new Intent(this, MainActivity.class);
        //For OneTimeUserInput


        prefManager = new PrefManager(this);
        if (!prefManager.IsOneTime()) {
            launchHomeScreen();
            finish();
        }
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        button = (Button) findViewById(R.id.button);
        viewPager = (ViewPager) findViewById(R.id.pager);

        viewPager.setAdapter(new SliderAdapter(getApplicationContext(),viewPager));
        pos = 0;
        len = viewPager.getAdapter().getCount();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                while(true)
                {
                    if(pos >= (len-1))
                    {
                        //
                        launchHomeScreen();
                        finish();
                    }
                    else
                    {
                        pos += 1;
                        if (pos == 3){

                            //one changed
                            button.setText("FINISH");

                        }
                    }
                    Log.d("App:",""+pos);
                    viewPager.setCurrentItem(pos);
                    break;
                }
            }
        });
    }
    private void launchHomeScreen() {
        prefManager.IsOneTime(false);
        Intent intent = new Intent(this,Home.class);
        startActivity(intent);
        finish();
    }
}
