package ca.mcgill.ecse428.nutrigo.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import ca.mcgill.ecse428.nutrigo.AddMealActivity;
import ca.mcgill.ecse428.nutrigo.AddMealEntryActivity;
import ca.mcgill.ecse428.nutrigo.LoginActivity;
import ca.mcgill.ecse428.nutrigo.R;
import cz.msebera.android.httpclient.Header;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    private ArrayList<MealItem> listElements;
    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    @Override
    public void onClick(View v) {
        Intent ide = new Intent(getActivity(), AddMealActivity.class);
        startActivity(ide);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_meals, container, false);

        Button b = (Button) root.findViewById(R.id.createNewMeal_button);
        b.setOnClickListener(this);

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

                            listElements.add(new MealItem(meal.get("name").toString(), summary));
                        } catch(JSONException e) {

                        }
                    }
                } catch(JSONException e) {

                }
                populateList("");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {}

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

    public void createMeal() {
        Intent ide = new Intent(getActivity(), AddMealActivity.class);
        startActivity(ide);
    }

    public void mealEntry(){
        Intent ide = new Intent(getActivity(), AddMealEntryActivity.class);
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