package com.example.fragmentdemo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.*;


public class SecondFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static int AUTOCOMPLETE_REQUEST_CODE = 1;

   // AutocompleteSupportFragment Locat1, Locat2;
    EditText txtOut;
    Button btnGet, btnRefine, btnGetJson;
    Double StartLat, StartLong, EndLat, EndLong;
    String OutMessage = "";
    String directionsReq = "";
    String ReqRes = "";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SecondFragment() {
        // Required empty public constructor
    }



    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    public static SecondFragment newInstance(String param1, String param2) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Places.initialize(this.requireContext(), getString(R.string.api_key));

      /*  Locat1 = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.location1);
        Locat2 = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.location2);
*/
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        txtOut = (EditText) view.findViewById(R.id.txtOut);
        btnGet = (Button) view.findViewById(R.id.btnGet);
        btnRefine = (Button) view.findViewById(R.id.btnRefine);
        btnGetJson = (Button) view.findViewById(R.id.btnGetJson);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

        AutocompleteSupportFragment Locat1 = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.location1);
        AutocompleteSupportFragment Locat2 = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.location2);
        Locat1.setPlaceFields(fields);
        Locat2.setPlaceFields(fields);


        Locat1.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
                Log.d("abcdefg", "onError: " + status.getStatusMessage());
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                final Place Loc1 = place;
                StartLat = Loc1.getLatLng().latitude;
                StartLong = Loc1.getLatLng().longitude;
                OutMessage += "\n\nStart Location: " + Loc1.getName() +
                        "\nLatitude: " + StartLat +
                        "\nLongitude: " + StartLong;
            }
        });
        Locat2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
                Log.d("abcdefg", "onError: " + status.getStatusMessage());
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                final Place Loc2 = place;
                EndLat = Loc2.getLatLng().latitude;
                EndLong = Loc2.getLatLng().longitude;

                OutMessage += "\n\nEnd Location: " + Loc2.getName() +
                        "\nLatitude: " + EndLat +
                        "\nLongitude: " + EndLong;
            }
        });

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtOut.setText(OutMessage);
                directionsReq = "https://maps.googleapis.com/maps/api/directions/json?origin=" + StartLat + "," + StartLong + "&destination=" + EndLat + "," + EndLong +"&key=" + getString(R.string.api_key);
                OutMessage="";
            }
        });
        btnGetJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directionsReq = "https://maps.googleapis.com/maps/api/directions/json?origin=" + StartLat + "," + StartLong + "&destination=" + EndLat + "," + EndLong +"&key=" + getString(R.string.api_key);

               FetchDirections fd = new FetchDirections();
               fd.execute(directionsReq);
            }
        });
        btnRefine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RefineJson();
            }
        });

    }

    public void RefineJson() {
        try {
            int Distance = 0;
            JSONObject obj = new JSONObject(ReqRes);
            JSONArray routes = obj.getJSONArray("routes");
            JSONObject zero = routes.getJSONObject(0);
            JSONArray legs = zero.getJSONArray("legs");


        } catch (JSONException e){
            Log.d("abcdefg", "RefineJson: " + e.getLocalizedMessage());
        }


    }

    private class FetchDirections extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(directionsReq).newBuilder();
            OkHttpClient newClient = new OkHttpClient.Builder().readTimeout(1000, TimeUnit.MILLISECONDS)
                    .writeTimeout(1000, TimeUnit.MILLISECONDS).build();
            Request req = new Request.Builder().url(urlBuilder.build().toString()).build();
            newClient.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.d("abcdefg", "onFailure: " + e.getLocalizedMessage());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    ReqRes = response.body().string();

                }
            });
            return ReqRes;
        }
        protected void onPostExecute(String res){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtOut.setText(ReqRes);
                }
            });
        }
    }


}