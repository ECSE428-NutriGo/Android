package ca.mcgill.ecse428.nutrigo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class SysAdminHome extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sysadmin_home);
    }

    public void viewUsers(View view){
        intent = new Intent(this, ViewUsersActivity.class);
        startActivity(intent);
    }
}
