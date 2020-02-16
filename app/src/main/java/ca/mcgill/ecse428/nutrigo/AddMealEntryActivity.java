package ca.mcgill.ecse428.nutrigo;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.widget.EditText;
import android.widget.Spinner;
import android.view.View;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;

import java.sql.Timestamp;
import java.util.GregorianCalendar;


public class AddMealEntryActivity extends AppCompatActivity {
    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private String username;


    private Date date;
    private Time time;

    private Timestamp timeStamp;
    DatePicker datePicker;
    TimePicker timePicker;
    TextView text;
    //THESE ARE THE STATIC VALUES THAT MUST BE SET BY THE QUERY MEALS ACTIVITY !!!!
    public static CharSequence mealName;
    public static long mealID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal_entry);

        //NOTE THAT WHEN WE come to this activity page we need to transmit the meal id from wich it was
        //accessed and we should take that value and set mealID to it so we know what meal to create.
        mealID= 1; //
        mealName="pasta";







        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.time_picker);
        text = (TextView) findViewById(R.id.textView4);
        text.setText("Create Meal Entry of ");
        text.append(mealName);



    }

    //NOTE THAT WHEN WE come to this activity page we need to transmit the meal id from wich it was
    //accessed and we hsoudl

    @RequiresApi(api = Build.VERSION_CODES.M)// don't know if this messes it up
    public void sendMessage(View view) {
        // Do something in response to button click
        int day=datePicker.getDayOfMonth();
        int month= datePicker.getMonth();
        int year= datePicker.getYear();

        int hour= timePicker.getHour();

        int minute= timePicker.getMinute();

        GregorianCalendar cal = new GregorianCalendar(year, month, day, hour, minute, 0);
        Timestamp ts = new Timestamp(cal.getTimeInMillis());

        RequestParams rp = new RequestParams();
        rp.put("meal", mealID);
        rp.put("timestamp", ts.toString());



        asyncHttpClient.put("https://nutrigo-staging.herokuapp.com/rest-auth/nutri/mealentry/", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {}
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {}
        });

        //NOW WE RETURN TO THE Query meal page!!!!!
        this.finish();





    }





}

