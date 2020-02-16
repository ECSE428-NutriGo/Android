package ca.mcgill.ecse428.nutrigo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ManuallyAddMacrosActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manualmacros);

        intent = new Intent(this, AddMealActivity.class);

        //mealNameBox = findViewById(R.id.editText_mealname);
    }

    public void backToAddMeal(View view){
        startActivity(intent);
    }

    public void confirmMacros(View view){
        EditText carbsET = findViewById(R.id.editText_carbs);
        EditText proteinET = findViewById(R.id.editText_protein);
        EditText fatsET = findViewById(R.id.editText_fat);

        AddMealActivity.carbs = carbsET.getText().toString();
        AddMealActivity.protein = proteinET.getText().toString();
        AddMealActivity.fat = fatsET.getText().toString();

        AddMealActivity.currentFoodItems = null;

        startActivity(intent);
    }
}