package ca.mcgill.ecse428.nutrigo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;


public class AddMealActivity extends AppCompatActivity {
    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
  
    private String username;

    public static String carbs = "0";
    public static String protein = "0";
    public static String fat = "0";
    public static String name = "";
    public static ArrayList<Integer> currentFoodItems = new ArrayList<>();
    public static ArrayList<String> currentFoodItemsNames = new ArrayList<>();
    Intent intent;
    String error = "";
    EditText mealNameBox;


    private void refreshErrorMessage() {
        // set the error message
        TextView tvError = findViewById(R.id.textView_error);
        tvError.setText(error);

        if (error == null || error.length() == 0) {
            tvError.setVisibility(View.GONE);
        } else {
            tvError.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createmeal);

        intent = new Intent(this, AddMealActivity.class);

        mealNameBox = findViewById(R.id.editText_mealname);

        mealNameBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                name = mealNameBox.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mealNameBox.setText(name);

        TextView foodList = findViewById(R.id.textView_itemlist);
        String foodItems = "";

        if (currentFoodItemsNames != null) {
            for (String str : currentFoodItemsNames) {
                foodItems = foodItems.concat(str + ", ");
            }
            foodList.setText(foodItems);
        }
        else{

            TextView carbsTV = findViewById(R.id.textView_carbsDyn);
            carbsTV.setText(carbs);

            TextView proteinTV = findViewById(R.id.textView_proteinDyn);
            proteinTV.setText(protein);

            TextView fatTV = findViewById(R.id.textView_fatDyn);
            fatTV.setText(fat);
        }

    }

    public void addMeal(View view){
        String mealName = mealNameBox.getText().toString();
        intent = new Intent(this, MainActivity.class);

        RequestParams params1 = new RequestParams();
        params1.put("fooditems", currentFoodItems);
        params1.put("name", mealName);

        RequestParams params2 = new RequestParams();
        params2.put("protein", protein);
        params2.put("carb", carbs);
        params2.put("fat", fat);
        params2.put("name", mealName);

        RequestParams params;

        if (currentFoodItems != null)
            params = params1;
        else params = params2;
        asyncHttpClient.addHeader("Authorization", "Token "+LoginActivity.getUserToken());
        asyncHttpClient.post("https://nutrigo-staging.herokuapp.com/nutri/meal/", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                name = "";
                carbs = "0";
                protein = "0";
                fat = "0";
                currentFoodItemsNames.clear();
                currentFoodItems.clear();

                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get("message").toString();
                } catch (JSONException e) {
                    error += e.getMessage();
                }
                refreshErrorMessage();
            }

        });
    }

    public void addFoodItem(View view){
        startActivity(new Intent(this, SearchFoodItemActivity.class));
    }

    public void manuallyEnterMacros(View view){
        intent = new Intent(this, ManuallyAddMacrosActivity.class);
        startActivity(intent);
    }
}
