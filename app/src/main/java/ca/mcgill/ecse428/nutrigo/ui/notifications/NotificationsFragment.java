package ca.mcgill.ecse428.nutrigo.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import ca.mcgill.ecse428.nutrigo.R;
import cz.msebera.android.httpclient.Header;

public class NotificationsFragment extends Fragment {

    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private String username;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_goals, container, false);
        final EditText fat_field = root.findViewById(R.id.fat_field);
        final EditText carbs_field = root.findViewById(R.id.carbs_field);
        final EditText proteins_field = root.findViewById(R.id.proteins_field);

        asyncHttpClient.addHeader("Authorization", "Token adf5ca6fa7ad08d8cb1fdfd471a92a92d6442997");
        asyncHttpClient.get("https://nutrigo-staging.herokuapp.com/rest-auth/user/", new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    proteins_field.setText(response.get("protein_target").toString());
                    carbs_field.setText(response.get("carb_target").toString());
                    fat_field.setText(response.get("fat_target").toString());
                    username = response.get("username").toString();
                } catch (JSONException e) {

                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {}
        });
        return root;
    }

    public void saveTargets(View view) {
        final EditText fat_field = getView().findViewById(R.id.fat_field);
        final EditText carbs_field = getView().findViewById(R.id.carbs_field);
        final EditText proteins_field = getView().findViewById(R.id.proteins_field);

        RequestParams rp = new RequestParams();
        rp.put("username", username);
        rp.put("protein_target", proteins_field.getText().toString());
        rp.put("carb_target", carbs_field.getText().toString());
        rp.put("fat_target", fat_field.getText().toString());

        asyncHttpClient.put("https://nutrigo-staging.herokuapp.com/rest-auth/user/", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }
        });
    }
}