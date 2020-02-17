package ca.mcgill.ecse428.nutrigo.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import ca.mcgill.ecse428.nutrigo.AddMealActivity;
import ca.mcgill.ecse428.nutrigo.AddMealEntryActivity;
import ca.mcgill.ecse428.nutrigo.LoginActivity;
import ca.mcgill.ecse428.nutrigo.R;
import ca.mcgill.ecse428.nutrigo.SignupActivity;
import cz.msebera.android.httpclient.Header;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    private ArrayList<MealItem> listElements;
    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    public static String selectedMealName = "default";
    public static int selectedMealId = -1;

    HashMap<String, Integer> mealIds = new HashMap<String, Integer>();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createNewMeal_button: {
                Intent ide = new Intent(getActivity(), AddMealActivity.class);
                startActivity(ide);
            }
            break;
            case R.id.createMealEntry_button: {
                if(selectedMealId != -1) {
                    Intent ide = new Intent(getActivity(), AddMealEntryActivity.class);
                    startActivity(ide);
                }
                else{
                    Toast.makeText(getActivity(), "Error: no meal provided", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_meals, container, false);

        Button b1 = (Button) root.findViewById(R.id.createNewMeal_button);
        b1.setOnClickListener(this);

        Button b2 = (Button) root.findViewById(R.id.createMealEntry_button);
        b2.setOnClickListener(this);

        final ListView lv = root.findViewById(R.id.mealList);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                MealItem item = (MealItem) adapter.getItemAtPosition(position);
                selectedMealName = item.getName();
                selectedMealId = mealIds.get(selectedMealName);
            }
        });

        listElements = new ArrayList<>();

        asyncHttpClient.addHeader("Authorization", "Token "+ LoginActivity.getUserToken());
        asyncHttpClient.get("https://nutrigo-staging.herokuapp.com/nutri/meal/", new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Object meals = null;
                try {
                    meals = response.get("meals");
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
                            mealIds.put(meal.get("name").toString(),Integer.parseInt(meal.get("id").toString()));
                            listElements.add(new MealItem(meal.get("name").toString(), summary));
                        } catch(JSONException e) {

                        }
                    }
                } catch(JSONException e) {

                }
                populateList("");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getActivity(), errorResponse.toString(), Toast.LENGTH_LONG).show();
            }

        });

        final EditText search = root.findViewById(R.id.search);
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
        return root;
    }

    private void populateList(String search) {
        final ListView lv = getView().findViewById(R.id.mealList);

        if(search.equals("")) {
            lv.setAdapter(new MyCustomBaseAdapter(this.getContext(), listElements));
        }
        else{
            ArrayList<MealItem> searchedElements = new ArrayList();
            for(MealItem li : listElements) {
                if(li.getName().matches("^"+search+".*")) {
                    searchedElements.add(li);
                }
            }
            lv.setAdapter(new MyCustomBaseAdapter(this.getContext(), searchedElements));
        }
    }

    public void createMeal(View view) {
        Intent ide = new Intent(getActivity(), AddMealActivity.class);
        startActivity(ide);
    }

    public void mealEntry(View view){
        Intent ide = new Intent(this.getContext(), AddMealEntryActivity.class);

        Bundle extras = new Bundle();
        extras.putInt("id", selectedMealId);
        extras.putString("mealname", selectedMealName);
        Log.v("a", extras.toString());
        ide.putExtras(extras);
        startActivity(ide);
    }
}

class MealItem {
    private String name;
    private String items;

    public MealItem(String name, String items) {
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