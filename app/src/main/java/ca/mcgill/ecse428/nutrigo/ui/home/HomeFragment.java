package ca.mcgill.ecse428.nutrigo.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import ca.mcgill.ecse428.nutrigo.LoginActivity;
import ca.mcgill.ecse428.nutrigo.R;
import cz.msebera.android.httpclient.Header;

public class HomeFragment extends Fragment {

    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView fat_text = root.findViewById(R.id.fat_text);
        final TextView carbs_text = root.findViewById(R.id.carbs_text);
        final TextView proteins_text = root.findViewById(R.id.proteins_text);

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