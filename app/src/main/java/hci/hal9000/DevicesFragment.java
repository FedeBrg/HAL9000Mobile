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

public class DevicesFragment extends Fragment {

    private ArrayList<Device> devices = new ArrayList<>();
    public DeviceAdapter devicesAdapter;
    private GridView gv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.devices_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
        


        View fab = getActivity().findViewById(R.id.fab_devices);
        FloatingActionButton button = (FloatingActionButton) fab;
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), CreateDevice.class);
                    startActivity(intent);
                }
            });
        }



        gv = (GridView) getActivity().findViewById(R.id.gridviewDevices);
        devicesAdapter = new DeviceAdapter(getActivity().getApplicationContext(), devices);
        gv.setAdapter(devicesAdapter);

        Api.getInstance(getContext()).getDevices(new Response.Listener<ArrayList<Device>>() {
            @Override
            public void onResponse(ArrayList<Device> response) {
                gv.setAdapter(new DeviceAdapter(getActivity().getApplicationContext(),response));
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
        String text =getString(R.string.connectionError); //Parametrizar en Strings
        if (response != null)
            text += " " + response.getDescription().get(0);

        //Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            Api.getInstance(getActivity().getApplicationContext()).getDevices(new Response.Listener<ArrayList<Device>>() {
                @Override
                public void onResponse(ArrayList<Device> response) {
                    gv.setAdapter(new DeviceAdapter(getActivity().getApplicationContext(),response));
                    Log.i("TestApi","Entre al onResponse");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    handleError(error);
                }
            });
        } else{
            //VolleySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue().cancelAll("devices");
        }
    }



}
