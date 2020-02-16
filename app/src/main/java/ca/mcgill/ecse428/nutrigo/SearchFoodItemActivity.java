package ca.mcgill.ecse428.nutrigo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

public class SearchFoodItemActivity extends AppCompatActivity {
    private ArrayList<ListItem> listElements;
    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchfooditem);


        listElements = new ArrayList<>();

        intent = new Intent(this, AddMealActivity.class);

        asyncHttpClient.addHeader("Authorization", "Token adf5ca6fa7ad08d8cb1fdfd471a92a92d6442997");
        asyncHttpClient.get("https://nutrigo-staging.herokuapp.com/nutri/fooditem/", new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Object foodItems = null;
                try {
                    foodItems = response.get("meals");
                } catch(JSONException e) {

                }
                for(int i = 0; i < ((JSONArray)foodItems).length(); i++) {
                    try {
                        JSONObject item = (JSONObject)((JSONArray)foodItems).get(i);
                        String[] macros = {item.get("carbs").toString(),item.get("protein").toString(),item.get("fat").toString()};

                        listElements.add(new ListItem(item.get("name").toString(), macros));
                    } catch(JSONException e) {

                    }
                }
                populateList("");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {}

        });

        final EditText search = (EditText) findViewById(R.id.editText_search);
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


    public void backToAddMeal(View view){
        startActivity(intent);
    }

    public void addFoodItem(View view){

    }

    public void createNewFoodItem(View view){
//        intent = new Intent(this, CreateNewFoodItemActivity.class);
//        startActivity(intent);
    }


    private void populateList(String search) {
        final ListView lv = (ListView) findViewById(R.id.listThing);

        if(search.equals("")) {
            lv.setAdapter(new MyCustomBaseAdapter(this, listElements));
        }
        else{
            ArrayList<ListItem> searchedElements = new ArrayList();
            for(ListItem li : listElements) {
                if(li.getItem().matches("^"+search+".*")) {
                    searchedElements.add(li);
                }
            }
            lv.setAdapter(new MyCustomBaseAdapter(this, searchedElements));
        }
    }
}


class ListItem {
    private String item;
    private String carbs;
    private String protein;
    private String fat;

    public ListItem(String name, String[] macros) {
        this.item = name;
        this.carbs = macros[0];
        this.protein = macros[1];
        this.fat = macros[2];
    }

    public String getItem() {
        return item;
    }

    public String[] getMacros() {
        String[] macros= {carbs, protein, fat};
        return macros;
    }

    public void setItem(String name) {
        this.item = name;
    }

    public void setMacros(String carbs, String protein, String fat) {
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
    }
}