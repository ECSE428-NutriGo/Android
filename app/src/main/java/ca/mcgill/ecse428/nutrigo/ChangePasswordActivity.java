package ca.mcgill.ecse428.nutrigo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;




public class ChangePasswordActivity extends AppCompatActivity {
    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private static String userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("oncreate","onCreate of change password acticity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Log.d("oncreate1","onCreate of change password acticity part 2 end");



    }

    public void changePassword(View view) {
        Log.d("entering","success");
        final EditText old_password_field = findViewById(R.id.original_password_field);
        final EditText new_password1 = findViewById(R.id.new_password);
        final EditText new_password2 = findViewById(R.id.confirm_new_password);


        RequestParams rp = new RequestParams();
        rp.put("old_password", old_password_field.getText());
        rp.put("new_password1", new_password1.getText());
        rp.put("new_password2", new_password2.getText());
        rp.put("LOGOUT_ON_PASSWORD_CHANGE","False");

        Log.d("rightbeforehttprequest","rightbefore request");

        asyncHttpClient.addHeader("Authorization", "Token "+ LoginActivity.getUserToken());
        asyncHttpClient.post("https://nutrigo-staging.herokuapp.com/rest-auth/password/change/", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                String m;
                m="";
                try {
                    m = response.get("detail").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*



                try {
                    userToken = response.get("key").toString();
                } catch(JSONException e) {

                }
                Toast.makeText(ChangePasswordActivity.this, userToken, Toast.LENGTH_LONG).show();
                Intent ide = new Intent(ChangePasswordActivity.this, MainActivity.class);
                ide.putExtra("userToken",userToken);
                LoginActivity.setUserToken(userToken);
                startActivity(ide);

                 */
                Log.d("response",m);
                Toast.makeText(ChangePasswordActivity.this, m, Toast.LENGTH_LONG).show();
                finish();




            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                String error = errorResponse.toString();
                String[] messages = error.split("],\"");
                String message = "";
                for(int i = 0; i < messages.length; i++) {
                    int a = messages[i].indexOf("[");
                    if(i == messages.length - 1) {
                        message += messages[i].substring(a+2, messages[i].length()-3);
                    }
                    else{
                        message += messages[i].substring(a+2, messages[i].length()-1)+"\n";
                    }
                }
                Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

    }
}