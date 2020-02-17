package ca.mcgill.ecse428.nutrigo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import ca.mcgill.ecse428.nutrigo.ui.dashboard.DashboardFragment;
import cz.msebera.android.httpclient.Header;


public class AddMealEntryActivity extends AppCompatActivity {
    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    DatePicker datePicker;
    TimePicker timePicker;

    public static String targetMealName = "";
    public static Integer targetMealID = -1;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal_entry);

        targetMealID = DashboardFragment.selectedMealId;
        targetMealName = DashboardFragment.selectedMealName;
        Log.v("hey", targetMealID.toString());

        intent = new Intent(this, MainActivity.class);

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.time_picker);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)// don't know if this messes it up
    public void createEntry(View view) {
        // Do something in response to button click
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        GregorianCalendar cal = new GregorianCalendar(year, month, day, hour, minute, 0);
        Timestamp ts = new Timestamp(cal.getTimeInMillis());

        RequestParams rp = new RequestParams();

        rp.put("meal", targetMealID);
        //rp.put("timestamp", ts.toString());

        asyncHttpClient.addHeader("Authorization", "Token "+ LoginActivity.getUserToken());
        asyncHttpClient.post("https://nutrigo-staging.herokuapp.com/nutri/mealentry/", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                targetMealName = "";
                targetMealID = -1;
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }
        });
    }
}

