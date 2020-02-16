package ca.mcgill.ecse428.nutrigo;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.widget.EditText;
import android.widget.Spinner;
import android.view.View;

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

public class GoalsActivity extends AppCompatActivity {

    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        final EditText fat_field = (EditText) findViewById(R.id.fat_field);
        final EditText carbs_field = (EditText) findViewById(R.id.carbs_field);
        final EditText proteins_field = (EditText) findViewById(R.id.proteins_field);

        asyncHttpClient.addHeader("Authorization", "Token adf5ca6fa7ad08d8cb1fdfd471a92a92d6442997");
        asyncHttpClient.get("https://nutrigo-staging.herokuapp.com/rest-auth/user/", new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    proteins_field.setText(response.get("protein_target").toString());
                    carbs_field.setText(response.get("carb_target").toString());
                    fat_field.setText(response.get("fat_target").toString());
                    username = response.get("username").toString();
                } catch (JSONException e) {

                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {}
        });
    }

    public void saveTargets(View view) {
        final EditText fat_field = (EditText) findViewById(R.id.fat_field);
        final EditText carbs_field = (EditText) findViewById(R.id.carbs_field);
        final EditText proteins_field = (EditText) findViewById(R.id.proteins_field);

        RequestParams rp = new RequestParams();
        rp.put("username", username);
        rp.put("protein_target", proteins_field.getText().toString());
        rp.put("carb_target", carbs_field.getText().toString());
        rp.put("fat_target", fat_field.getText().toString());

        asyncHttpClient.put("https://nutrigo-staging.herokuapp.com/rest-auth/user/", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {}
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {}
        });
    }

}
