package ca.mcgill.ecse428.nutrigo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;

public class ChangeContactInfoActivity extends AppCompatActivity {

    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_contact_info);
    }





}
