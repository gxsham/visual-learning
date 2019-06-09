package gxsham.visuallearning;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public String Language;
    public String[] Query;
    TextView result;
    Button button;
    public static int PointsMain;
    TextView pointsView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText input = findViewById(R.id.editText);
        result = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        pointsView = findViewById(R.id.pointsView);
        button.setEnabled(false);

        PointsMain = 0;
        pointsView.setText(Integer.toString(PointsMain));
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Language = "en";


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Language = adapter.getItem(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() !=0) {
                    Query = charSequence.toString().split(" ");
                    button.setEnabled(true);
                }
                else {
                    button.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetRequest();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        pointsView.setText(Integer.toString(PointsMain) + " Points");
    }

    private void GetRequest(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String query = "";
        for(String elem:Query){
            query += elem + "+";
        }
        query = query.substring(0, query.length() - 1);

        final ArrayList<GameElement> gameElements = new ArrayList<GameElement>();

        String url ="https://pixabay.com/api/?key=8912041-26682729ed9d1f0cc1c1241f4&q="+query+"&image_type=photo&lang=" + Language;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            Integer totalHits = json.getInt("totalHits");
                            JSONArray hits = json.getJSONArray("hits");
                            for(int i=0;i<hits.length();i++){
                                JSONObject element = hits.getJSONObject(i);
                                String tags = element.getString("tags");
                                String imageUrl = element.getString("webformatURL");
                                gameElements.add(new GameElement(tags, imageUrl));
                            }
                            if(gameElements.size() > 0) {

                                Intent intent = new Intent(getBaseContext(), GameActivity.class);
                                intent.putExtra("gameElements", gameElements);
                                intent.putExtra("totalHits", totalHits);
                                intent.putExtra("totalElements", gameElements.size());
                                startActivityForResult(intent, 1);
                            }
                            else{
                                result.setText("Try something else");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                result.setText("Connection error. Please make sure you are connected to internet.");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
