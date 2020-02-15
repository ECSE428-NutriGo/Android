package ca.mcgill.ecse428.nutrigo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.text.Editable;
import android.text.TextWatcher;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;
import com.loopj.android.http.AsyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QueryMealsActivity extends AppCompatActivity {
    private ArrayList<ListItem> listElements;
    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);

        listElements = new ArrayList<>();

        asyncHttpClient.addHeader("Authorization", "Token adf5ca6fa7ad08d8cb1fdfd471a92a92d6442997");
        asyncHttpClient.get("https://nutrigo-staging.herokuapp.com/nutri/meal/", new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Object meals = null;
                try {
                    meals = response.get("meals");
                } catch(JSONException e) {

                }
                for(int i = 0; i < ((JSONArray)meals).length(); i++) {
                    try{
                        JSONObject meal = (JSONObject)((JSONArray)meals).get(i);
                        JSONArray foods = (JSONArray)meal.get("fooditems");
                        String summary = "";
                        for(int j = 0; j < foods.length(); j++) {
                            if(j != foods.length() - 1){
                                summary += ((JSONObject)(foods.get(j))).get("name").toString()+", ";
                            }
                            else {
                                summary += ((JSONObject)(foods.get(j))).get("name").toString();
                            }
                        }

                        listElements.add(new ListItem(meal.get("name").toString(), summary));
                    } catch(JSONException e) {

                    }
                }
                populateList("");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {}

        });

        final EditText search = (EditText) findViewById(R.id.search);
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

    private void populateList(String search) {
        final ListView lv = (ListView) findViewById(R.id.mealList);

        if(search.equals("")) {
            lv.setAdapter(new MyCustomBaseAdapter(this, listElements));
        }
        else{
            ArrayList<ListItem> searchedElements = new ArrayList();
            for(ListItem li : listElements) {
                if(li.getName().matches("^"+search+".*")) {
                    searchedElements.add(li);
                }
            }
            lv.setAdapter(new MyCustomBaseAdapter(this, searchedElements));
        }




    }
}

class ListItem {
    private String name;
    private String items;

    public ListItem(String name, String items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public String getItems() {
        return items;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItems(String items) {
        this.items = items;
    }
}
