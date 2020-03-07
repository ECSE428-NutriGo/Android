package ca.mcgill.ecse428.nutrigo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

public class SearchFoodItemActivity extends AppCompatActivity {
    private ArrayList<ListFoodItem> listElements;
    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    private HashMap<String, Integer> ids;
    private Integer itemId;
    private String itemName;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchfooditem);

        ids = new HashMap<String, Integer>();

        listElements = new ArrayList<>();

        intent = new Intent(this, AddMealActivity.class);

        asyncHttpClient.addHeader("Authorization", "Token "+ LoginActivity.getUserToken());
        asyncHttpClient.get("https://nutrigo-staging.herokuapp.com/nutri/fooditem/", new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray foodItems = (JSONArray)response.get("fooditems");
                    for(int i = 0; i < ((JSONArray)foodItems).length(); i++) {

                        JSONObject item = (JSONObject)((JSONArray)foodItems).get(i);
                        Integer[] macros = {(Integer.parseInt(item.get("carb").toString())),(Integer.parseInt(item.get("protein").toString())),(Integer.parseInt(item.get("fat").toString()))};
                        String id = item.get("id").toString();
                        Integer id1 = Integer.parseInt(id);

                        ids.put(item.get("name").toString(),id1);

                        listElements.add(new ListFoodItem(item.get("name").toString(), macros));
                    }

                } catch(JSONException e) {

                }

                populateList("");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {}

        });

        ListView list = findViewById(R.id.listThing);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                ListFoodItem item = (ListFoodItem) adapter.getItemAtPosition(position);
                itemName = item.getItem();
                itemId = ids.get(itemName);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapter, View v,
                                           int position, long id) {
                ListFoodItem item = (ListFoodItem) adapter.getItemAtPosition(position);


                String name = item.getItem();
                int food_item_id= ids.get(name);
                startEditFoodItemActivity(food_item_id);

                //Intent i = new Intent(SearchFoodItemActivity, EditFoodItemActivity.class);


               // startActivity(i);
                return true;
            }
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
    public void startEditFoodItemActivity(int itemId){
        intent= new Intent(this, EditFoodItemActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("SelectedID",itemId);
        //intent.putExtra("SelectedID",itemId);
        intent.putExtras(bundle);
        startActivity(intent);


    }

    public void addFoodItem(View view){
        intent = new Intent(this, AddMealActivity.class);
        AddMealActivity.currentFoodItems.add(itemId);
        AddMealActivity.currentFoodItemsNames.add(itemName);
        startActivity(intent);
    }

    public void createNewFoodItem(View view){
        intent = new Intent(SearchFoodItemActivity.this, AddFoodItemActivity.class);
        startActivity(intent);
    }


    private void populateList(String search) {
        final ListView lv = (ListView) findViewById(R.id.listThing);

        if(search.equals("")) {
            lv.setAdapter(new CustomBaseAdapterFoodItem(this, listElements));
        }
        else{
            ArrayList<ListFoodItem> searchedElements = new ArrayList();
            for(ListFoodItem li : listElements) {
                if(li.getItem().matches("^"+search+".*")) {
                    searchedElements.add(li);
                }
            }
            lv.setAdapter(new CustomBaseAdapterFoodItem(this, searchedElements));
        }
    }
}


class ListFoodItem {
    private String item;
    private Integer carbs;
    private Integer protein;
    private Integer fat;

    public ListFoodItem(String name, Integer[] macros) {
        this.item = name;
        this.carbs = macros[0];
        this.protein = macros[1];
        this.fat = macros[2];
    }

    public String getItem() {
        return item;
    }

    public Integer[] getMacros() {
        Integer[] macros = {carbs, protein, fat};
        return macros;
    }

    public void setItem(String name) {
        this.item = name;
    }

    public void setMacros(Integer carbs, Integer protein, Integer fat) {
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
    }
}