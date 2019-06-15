package hci.hal9000;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;


import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class RoomsFragment extends Fragment {
    private ArrayList<Room> rooms = new ArrayList<>();
    public RoomAdapter roomAdapter;
    private GridView gv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.rooms_fragment, container, false);


    }

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);

//        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
//        NavController navController = Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment);
//        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        View fab = getActivity().findViewById(R.id.fab_room);
        FloatingActionButton button = (FloatingActionButton) fab;
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), CreateRoom.class);
                    startActivity(intent);
                }
            });
        }

        gv = (GridView) getActivity().findViewById(R.id.gridviewRooms);
        roomAdapter = new RoomAdapter(getActivity().getApplicationContext(), rooms);
        gv.setAdapter(roomAdapter);

        Api.getInstance(getActivity().getApplicationContext()).getRooms(new Response.Listener<ArrayList<Room>>() {
            @Override
            public void onResponse(ArrayList<Room> response) {
                gv.setAdapter(new RoomAdapter(getActivity().getApplicationContext(),response));
                Log.i("TestApi","Entre al onResponse");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(error);
            }
        });


    }


    private void handleError(VolleyError error) {
        Error response = null;

        NetworkResponse networkResponse = error.networkResponse;
        if ((networkResponse != null) && (error.networkResponse.data != null)) {
            try {
                String json = new String(
                        error.networkResponse.data,
                        HttpHeaderParser.parseCharset(networkResponse.headers));

                JSONObject jsonObject = new JSONObject(json);
                json = jsonObject.getJSONObject("error").toString();

                Gson gson = new Gson();
                response = gson.fromJson(json, Error.class);
            } catch (JSONException e) {
            } catch (UnsupportedEncodingException e) {
            }
        }

        Log.e("Testing", error.toString());
        //String text = getResources().getString(R.string.error_message);
        String text = "Connection error."; //Parametrizar en Strings
        if (response != null)
            text += " " + response.getDescription().get(0);

        //Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            Api.getInstance(getActivity().getApplicationContext()).getRooms(new Response.Listener<ArrayList<Room>>() {
                @Override
                public void onResponse(ArrayList<Room> response) {
                    gv.setAdapter(new RoomAdapter(getActivity().getApplicationContext(),response));
                    Log.i("TestApi","Entre al onResponse");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    handleError(error);
                }
            });
        } else{
            VolleySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue().cancelAll("rooms");
        }
    }
}




