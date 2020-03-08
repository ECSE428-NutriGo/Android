package ca.mcgill.ecse428.nutrigo.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ca.mcgill.ecse428.nutrigo.AddMealEntryActivity;
import ca.mcgill.ecse428.nutrigo.LoginActivity;
import ca.mcgill.ecse428.nutrigo.R;
import cz.msebera.android.httpclient.Header;
import ca.mcgill.ecse428.nutrigo.ChangePasswordActivity;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    @Override
    public void onClick(View v) {

        LoginActivity.setUserToken("");
        Intent ide = new Intent(getActivity(), LoginActivity.class);
        //Intent ide = new Intent(getActivity(), ChangePasswordActivity.class);
        startActivity(ide);



    }
    /*
    //maybe delete
    public void logOutButton(View view){
        LoginActivity.setUserToken("");
        Intent ide = new Intent(getActivity(), LoginActivity.class);
        startActivity(ide);

    }
    *
     */

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Button b = (Button) root.findViewById(R.id.logout_button);
        b.setOnClickListener(this);

        final TextView fat_text = root.findViewById(R.id.carb_text);
        final TextView carbs_text = root.findViewById(R.id.protein_text);
        final TextView proteins_text = root.findViewById(R.id.fat_text);

        asyncHttpClient.addHeader("Authorization", "Token " + LoginActivity.getUserToken());
        asyncHttpClient.get("https://nutrigo-staging.herokuapp.com/nutri/daily/", new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    fat_text.setText("Fat : " +response.get("fat").toString() + " g");
                    carbs_text.setText("Carbs : " +response.get("carb").toString()+ " g");
                    proteins_text.setText("Protein : " +response.get("protein").toString()+ " g");
                } catch (JSONException e) {

                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {}
        });
        return root;
    }
    //maybe delete
    /*
    public void changePassword(View view){
        Intent i= new Intent(this.getActivity(), ChangePasswordActivity.class);
        this.startActivity(i);

    }

     */
}