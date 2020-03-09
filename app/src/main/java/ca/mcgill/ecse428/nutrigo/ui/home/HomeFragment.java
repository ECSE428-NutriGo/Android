package ca.mcgill.ecse428.nutrigo.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import ca.mcgill.ecse428.nutrigo.ChangePasswordActivity;
import ca.mcgill.ecse428.nutrigo.EditUserActivity;
import ca.mcgill.ecse428.nutrigo.LoginActivity;
import ca.mcgill.ecse428.nutrigo.R;
import cz.msebera.android.httpclient.Header;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button: {
                LoginActivity.setUserToken("");
                Intent ide = new Intent(getActivity(), LoginActivity.class);
                startActivity(ide);
            }
            break;
            case R.id.edit_user: {
                Intent i= new Intent(getActivity(), EditUserActivity.class);
                this.startActivity(i);
            }
            break;
            case R.id.change_password_button: {
                Intent i= new Intent(getActivity(), ChangePasswordActivity.class);
                this.startActivity(i);
                break;

            }

        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Button b1 = (Button) root.findViewById(R.id.logout_button);
        b1.setOnClickListener(this);

        Button b2 = (Button) root.findViewById(R.id.change_password_button);
        b2.setOnClickListener(this);

        Button b3 = (Button) root.findViewById(R.id.edit_user);
        b3.setOnClickListener(this);

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
}