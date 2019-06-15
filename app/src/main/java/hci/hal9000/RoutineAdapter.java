package hci.hal9000;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

public class RoutineAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Routine> routines;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private ArrayList<ImageView> imageViews;
    private int i;


    public RoutineAdapter(Context context, ArrayList<Routine> routines) {
        this.context = context;
        this.routines = routines;
    }

    @Override
    public int getCount() {
        return routines.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Routine routine = routines.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.linearlayout_routine, null);
        }

        imageViews = new ArrayList<>();

        imageView1 = convertView.findViewById(R.id.imageview_cover_art_routine1);
        imageView2 = convertView.findViewById(R.id.imageview_cover_art_routine2);
        imageView3 = convertView.findViewById(R.id.imageview_cover_art_routine3);
        imageView4 = convertView.findViewById(R.id.imageview_cover_art_routine4);

        imageViews.add(imageView1);
        imageViews.add(imageView2);
        imageViews.add(imageView3);
        imageViews.add(imageView4);

        final TextView nameTextView = convertView.findViewById(R.id.textview_routine_name);
        nameTextView.setText(routine.getName());

        for(i = 0; i<routine.getActions().size()-1;i++){
            Api.getInstance(context).getDevice(routine.getActions().get(i).getDeviceId(), new Response.Listener<Device>() {
                @Override
                public void onResponse(Device response) {
                    Log.i("Routines",String.format("%s",routine.getId()));
                    setResource(response.getTypeId(),imageViews.get(i));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("Routines","Error de getDevice");

                }
            });
        }

        Button execute = convertView.findViewById(R.id.executeRoutine);
        execute.bringToFront();
        execute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Api.getInstance(context).executeRoutine(routine.getId(), new Response.Listener<ArrayList<Boolean>>() {
                    @Override
                    public void onResponse(ArrayList<Boolean> response) {
                        Log.i("routines","Routine Executed");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("routines","Error de execute");
                        handleError(error);

                    }
                });
            }
        });

        return convertView;
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

        Log.i("Testing", error.toString());
        //String text = getResources().getString(R.string.error_message);
        String text = "Connection error.";//getString(R.string.connectionError); //Parametrizar en Strings
        if (response != null)
            text += " " + response.getDescription().get(0);

        //Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }
    private void setResource(String device,ImageView imageView) {
        if (device.compareTo("go46xmbqeomjrsjr") == 0) {
            imageView.setImageResource(R.drawable.light);
        } else if (device.compareTo("eu0v2xgprrhhg41g") == 0) {
            imageView.setImageResource(R.drawable.curtains);
        } else if (device.compareTo("li6cbv5sdlatti0j") == 0) {
            imageView.setImageResource(R.drawable.air_conditioner);
        } else if (device.compareTo("im77xxyulpegfmv8") == 0) {
            imageView.setImageResource(R.drawable.oven);
        } else if (device.compareTo("lsf78ly0eqrjbz91") == 0) {
            imageView.setImageResource(R.drawable.door);
        } else if (device.compareTo("rnizejqr2di0okho") == 0) {
            imageView.setImageResource(R.drawable.fridge);
        }
    }
}
