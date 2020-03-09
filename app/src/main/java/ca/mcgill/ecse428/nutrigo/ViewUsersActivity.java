package ca.mcgill.ecse428.nutrigo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

public class ViewUsersActivity extends AppCompatActivity {

    private ArrayList<ListUser> listElements;
    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    private HashMap<String, Integer> ids;
    private String email;
    private String userName;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sysadmin_users);

        listElements = new ArrayList<>();

        RequestParams rp = new RequestParams();
        rp.add("keyword", "@");

        asyncHttpClient.addHeader("Authorization", "Token "+ LoginActivity.getUserToken());
        asyncHttpClient.get("https://nutrigo-staging.herokuapp.com/profile/users", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray users = (JSONArray)response.get("fooditems");
                    for(int i = 0; i < ((JSONArray)users).length(); i++) {

                        JSONObject item = (JSONObject)((JSONArray)users).get(i);
                        listElements.add(new ListUser(item.get("username").toString(), item.get("email").toString()));
                    }

                } catch(JSONException e) {

                }

                populateList("");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                Log.e("error: ",errorResponse.toString());
            }
        });

        ListView list = findViewById(R.id.userList);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                ListUser item = (ListUser) adapter.getItemAtPosition(position);
                userName = item.getItem();
                email = item.getEmail();
            }
        });

        final EditText search = (EditText) findViewById(R.id.editText);
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

    private void populateList(String search) {
        final ListView lv = (ListView) findViewById(R.id.userList);

        if(search.equals("")) {
            lv.setAdapter(new CustomBaseAdapterUser(this, listElements));
        }
        else{
            ArrayList<ListUser> searchedElements = new ArrayList();
            for(ListUser li : listElements) {
                if(li.getItem().matches("^"+search+".*")) {
                    searchedElements.add(li);
                }
            }
            lv.setAdapter(new CustomBaseAdapterUser(this, searchedElements));
        }
    }



    public void lockOutUser(View view){
        RequestParams params = new RequestParams();
        params.put("email", email);
        asyncHttpClient.addHeader("Authorization", "Token "+ LoginActivity.getUserToken());
        asyncHttpClient.post("https://nutrigo-staging.herokuapp.com/profile/lockout/", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                populateList("");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {}

        });
    }

    public void viewUserDetails(View view){
        intent = new Intent(this, UserDetailsActivity.class);
        startActivity(intent);
    }

    class ListUser {
        private String item;
        private String email;

        public ListUser(String name, String email) {
            this.item = name;
            this.email = email;
        }

        public String getItem() {
            return item;
        }

        public String getEmail() {
            return this.email;
        }

        public void setItem(String name) {
            this.item = name;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
