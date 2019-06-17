package hci.hal9000;

import hci.hal9000.R;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;


public class VoiceFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.voice_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
        final EditText editText = getActivity().findViewById(R.id.editText);

        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null){
                    editText.setText(matches.get(0));
                    parseSpeech(matches.get(0));
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        getActivity().findViewById(R.id.button).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        editText.setHint(R.string.beforeListen);
                        break;

                    case MotionEvent.ACTION_DOWN:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if ((ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                                mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                                editText.setText("");
                                editText.setHint(R.string.listening);
                            }
                            else{
                                editText.setText("");
                                editText.setHint(R.string.microphone);
                            }
                        }
                        else{
                            editText.setText("");
                            editText.setHint(R.string.listening);
                        }

                        break;
                }
                return false;
            }
        });
    }

    private void parseSpeech(String speech){
        String[] speechArray = speech.split(" ");
        StringBuilder str = new StringBuilder();

        if(speechArray.length < 2){
            return;
        }

        if(speechArray[0].toLowerCase().equals("encender") || speechArray[0].toLowerCase().equals("abrir") || speechArray[0].toLowerCase().equals("open")){
            for(int i = 1; i < speechArray.length; i++){
                str.append(speechArray[i]);
                if(i != speechArray.length - 1){
                    str.append(" ");
                }
            }

            getDevicesAndTurnOn(str.toString());
        }

        else if((speechArray[0].toLowerCase().equals("turn") && speechArray[1].toLowerCase().equals("on"))){
            if(speechArray.length == 2){
                return;
            }

            for(int i = 2; i < speechArray.length; i++){
                str.append(speechArray[i]);
                if(i != speechArray.length - 1){
                    str.append(" ");
                }
            }

            getDevicesAndTurnOn(str.toString());
        }

        else if((speechArray[0].toLowerCase().equals("turn") && speechArray[1].toLowerCase().equals("off"))){
            if(speechArray.length == 2){
                return;
            }

            for(int i = 2; i < speechArray.length; i++){
                str.append(speechArray[i]);
                if(i != speechArray.length - 1){
                    str.append(" ");
                }
            }

            getDevicesAndTurnOff(str.toString());
        }

        else if(speechArray[0].toLowerCase().equals("apagar") || speechArray[0].toLowerCase().equals("cerrar") || speechArray[0].toLowerCase().equals("close")){
            for(int i = 1; i < speechArray.length; i++){
                str.append(speechArray[i]);
                if(i != speechArray.length - 1){
                    str.append(" ");
                }
            }

            getDevicesAndTurnOff(str.toString());
        }
    }

    private void getDevicesAndTurnOn(String device){
        final String deviceName = device;
        Api.getInstance(getContext()).getDevices(new Response.Listener<ArrayList<Device>>() {
            @Override
            public void onResponse(ArrayList<Device> response) {
               for(Device d : response){
                   if(d.getName().toLowerCase().equals(deviceName.toLowerCase())){
                       String str;
                       if(d.getTypeId().equals("lsf78ly0eqrjbz91") || d.getTypeId().equals("eu0v2xgprrhhg41g")){
                           str = "open";
                       }
                       else{
                           str = "turnOn";
                       }

                       Api.getInstance(getContext()).setDeviceStatusBoolean(d.getId(), str, new Response.Listener<Boolean>() {
                           @Override
                           public void onResponse(Boolean response) {}
                       }, new Response.ErrorListener() {
                           @Override
                           public void onErrorResponse(VolleyError error) {}
                       });
                   }
               }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(error);
            }
        });
    }

    private void getDevicesAndTurnOff(String device){
        final String deviceName = device;
        Api.getInstance(getContext()).getDevices(new Response.Listener<ArrayList<Device>>() {
            @Override
            public void onResponse(ArrayList<Device> response) {
                for(Device d : response){
                    if(d.getName().toLowerCase().equals(deviceName.toLowerCase())){
                        String str;
                        if(d.getTypeId().equals("lsf78ly0eqrjbz91") || d.getTypeId().equals("eu0v2xgprrhhg41g")){
                            str = "close";
                        }
                        else{
                            str = "turnOff";
                        }

                        Api.getInstance(getContext()).setDeviceStatusBoolean(d.getId(), str, new Response.Listener<Boolean>() {
                            @Override
                            public void onResponse(Boolean response) {}
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {}
                        });
                    }
                }
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
}
