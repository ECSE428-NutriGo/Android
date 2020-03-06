package ca.mcgill.ecse428.nutrigo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import ca.mcgill.ecse428.nutrigo.ui.dashboard.DashboardFragment;
import cz.msebera.android.httpclient.Header;


public class DeleteFoodItemActivity extends AppCompatActivity {
    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private ArrayList<String> fooditemList = new ArrayList<>();
    private HashMap<String, Integer> entries = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_fooditem);

        final ListView lv = findViewById(R.id.fooditemList);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapter, View v,
                                           int position, long id) {
                final String item = adapter.getItemAtPosition(position).toString();
                int selectedFoodItemId = entries.get(item);
                asyncHttpClient.addHeader("Authorization", "Token "+ LoginActivity.getUserToken());
                RequestParams rp = new RequestParams();
                rp.put("meal", DashboardFragment.selectedMealId);
                rp.put("fooditem", selectedFoodItemId);
                asyncHttpClient.patch("https://nutrigo-staging.herokuapp.com/nutri/meal/",rp, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        fooditemList.remove(item);
                        populateList();
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                    }
                });
                return true;
            }
        });

        asyncHttpClient.addHeader("Authorization", "Token "+ LoginActivity.getUserToken());
        asyncHttpClient.get("https://nutrigo-staging.herokuapp.com/nutri/meal/"+DashboardFragment.selectedMealId, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray fooditems = (JSONArray)((JSONObject)response.get("meal")).get("fooditems");
                    for(int i = 0; i < fooditems.length(); i++) {
                        JSONObject fooditem = (JSONObject)((JSONArray)fooditems).get(i);
                        fooditemList.add(fooditem.get("name").toString());
                        entries.put(fooditem.get("name").toString(), (int)fooditem.get("id"));
                    }
                } catch(JSONException e) {
                }
                populateList();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
    }

    private void populateList() {
        final ListView lv = findViewById(R.id.fooditemList);
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fooditemList));
    }
}
