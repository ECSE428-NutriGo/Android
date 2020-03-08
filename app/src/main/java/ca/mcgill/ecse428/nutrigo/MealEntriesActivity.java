package ca.mcgill.ecse428.nutrigo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import ca.mcgill.ecse428.nutrigo.ui.dashboard.MyCustomBaseAdapter;
import cz.msebera.android.httpclient.Header;

public class MealEntriesActivity extends AppCompatActivity {

    private ArrayList<String> meals;
    private ArrayList<String> timestamps;
    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    private HashMap<String, Integer> ids;
    private String meal;
    private String timestamp;
    private Integer id;
    private Integer itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mealentries);

        ids = new HashMap<String, Integer>();

        meals = new ArrayList<>();
        timestamps = new ArrayList<>();

        asyncHttpClient.addHeader("Authorization", "Token "+ LoginActivity.getUserToken());
        asyncHttpClient.get("https://nutrigo-staging.herokuapp.com/nutri/mealentry/", new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray mealEntries = (JSONArray)response.get("mealentries");
                    for(int i = 0; i < ((JSONArray)mealEntries).length(); i++) {

                        JSONObject mealEntry = (JSONObject)((JSONArray)mealEntries).get(i);
                        meal = mealEntry.get("meal").toString();
                        timestamp = mealEntry.get("timestamp").toString();
                        id = (int)mealEntry.get("id");

                        ids.put(meal,id);

                        meals.add(meal);
                        timestamps.add(timestamp);
                    }

                } catch(JSONException e) {

                }

                populateList("");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                String error = errorResponse.toString();
                String[] messages = error.split("],\"");
                String message = "";
                for(int i = 0; i < messages.length; i++) {
                    int a = messages[i].indexOf("[");
                    if(i == messages.length - 1) {
                        message += messages[i].substring(a+2, messages[i].length()-3);
                    }
                    else{
                        message += messages[i].substring(a+2, messages[i].length()-1)+"\n";
                    }
                }
                Toast.makeText(MealEntriesActivity.this, message, Toast.LENGTH_LONG).show();
            }

        });

        ListView list = findViewById(R.id.mealEntries_list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                String item = (String) adapter.getItemAtPosition(position);
                meal = item;
                itemId = ids.get(meal);
            }
        });

        final EditText search = (EditText) findViewById(R.id.search_mealEntries);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                populateList(search.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void deleteMealEntry(View view){
        final ListView lv = (ListView) findViewById(R.id.mealEntries_list);

        try {
            final String item = lv.getSelectedItem().toString();
            asyncHttpClient.addHeader("Authorization", "Token "+ LoginActivity.getUserToken());
            RequestParams rp = new RequestParams();
            rp.put("mealentry", item);
            asyncHttpClient.patch("https://nutrigo-staging.herokuapp.com/nutri/mealentry/",rp, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    String error = response.toString();
                    String[] messages = error.split("],\"");
                    String message = "";
                    for(int i = 0; i < messages.length; i++) {
                        int a = messages[i].indexOf("[");
                        if(i == messages.length - 1) {
                            message += messages[i].substring(a+2, messages[i].length()-3);
                        }
                        else{
                            message += messages[i].substring(a+2, messages[i].length()-1)+"\n";
                        }
                    }
                    Toast.makeText(MealEntriesActivity.this, message, Toast.LENGTH_LONG).show();
                    populateList("");
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    String error = errorResponse.toString();
                    String[] messages = error.split("],\"");
                    String message = "";
                    for(int i = 0; i < messages.length; i++) {
                        int a = messages[i].indexOf("[");
                        if(i == messages.length - 1) {
                            message += messages[i].substring(a+2, messages[i].length()-3);
                        }
                        else{
                            message += messages[i].substring(a+2, messages[i].length()-1)+"\n";
                        }
                    }
                    Toast.makeText(MealEntriesActivity.this, message, Toast.LENGTH_LONG).show();
                }
            });
        }
        catch(Exception e){

        }

    }


    private void populateList(String search) {
        final ListView lv = (ListView) findViewById(R.id.mealEntries_list);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, meals) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(meals.get(position));
                text2.setText(timestamps.get(position));
                return view;
            }
        };
        if(search.equals("")) {
            lv.setAdapter(adapter);
        }
        else{
            ArrayList<String> searchedElements = new ArrayList();
            for(String li : meals) {
                if(li.matches("^"+search+".*")) {
                    searchedElements.add(li);
                }
            }
            lv.setAdapter(adapter);
        }
    }
}


class MealEntry {
    private String name;
    private String timestamp;
    private String meal;

    public MealEntry(String name, String timestamp, String meal) {
        this.name = name;
        this.timestamp = timestamp;
        this.meal = meal;
    }

    public String getName() {
        return name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMeal() {
        return meal;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }
}