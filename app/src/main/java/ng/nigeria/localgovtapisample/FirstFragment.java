package ng.nigeria.localgovtapisample;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FirstFragment extends Fragment {

    TextView lgaFullLists;
    String apiUrl = "https://nigeriastateandlocalgovtarea.herokuapp.com/lgalists";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment with view
        View v = inflater.inflate(R.layout.fragment_first, container, false);
        lgaFullLists = v.findViewById(R.id.textview_first);

        try {
            run();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return v;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

    }


    public void run() throws IOException, JSONException {

        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                       // lgaFullLists.setText(data);
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(myResponse);
                            for (int i = 0 ; i<jsonArray.length();i++) {
                                //JSONObject listing = jsonArray.getJSONObject(i);
                                JSONObject listing = jsonArray.getJSONObject(i);
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("SerialNumber: "+ listing.getString("SerialNumber")+ "\n");
                                stringBuilder.append("Country: "+listing.getString("Country") + "\n");
                                stringBuilder.append("State: "+listing.getString("State")+ "\n");
                                stringBuilder.append("Local Govt Area: "+listing.getString("LGA")+ "\n");
                                stringBuilder.append("Senatorial District: "+listing.getString("SenDistrict")+ "\n");
                                stringBuilder.append("Senatorial District Code: "+listing.getString("SenDistrictCode")+ "\n");
                                stringBuilder.append("Geo Length: "+listing.getString("Shape_Length")+ "\n");
                                stringBuilder.append("Geographical Area: "+listing.getString("Shape_Area")+ "\n\n");

                                lgaFullLists.append(stringBuilder);

                                Log.d("String Results:",stringBuilder.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }
}