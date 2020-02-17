package ca.mcgill.ecse428.nutrigo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ManuallyAddMacrosActivity extends AppCompatActivity {

    Intent intent;

    EditText carbsET;
    EditText proteinET;
    EditText fatsET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manualmacros);

        intent = new Intent(this, AddMealActivity.class);

        carbsET = findViewById(R.id.editText_carbs);
        proteinET = findViewById(R.id.editText_protein);
        fatsET = findViewById(R.id.editText_fat);

    }

    public void backToAddMeal(View view){
        startActivity(intent);
    }

    public void confirmMacros(View view){

        AddMealActivity.carbs = carbsET.getText().toString();
        AddMealActivity.protein = proteinET.getText().toString();
        AddMealActivity.fat = fatsET.getText().toString();

        Log.v("Carbs: ", AddMealActivity.carbs);
        Log.v("Protein: ", AddMealActivity.protein);
        Log.v("Fat: ", AddMealActivity.fat);

        AddMealActivity.currentFoodItems.clear();
        AddMealActivity.currentFoodItemsNames.clear();

        startActivity(intent);
    }
}