package com.example.repaso1t;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class MainActivity extends AppCompatActivity {

    WebView wb_listado;
    RequestQueue queue;
    final String url = "https://www.esmadrid.com/opendata/alojamientos_v1_es.xml";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wb_listado = findViewById(R.id.wb_listado);
        queue = Volley.newRequestQueue(this);
        listar(url);
    }

    private void listar(final String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        String respuesta = response;

                        InputStream inputStream = new ByteArrayInputStream(respuesta.getBytes(Charset.forName("UTF-8")));
                        String tabla_html = "<html>\n" +
                                "<head>\n" +
                                "\t<title></title>\n" +
                                "</head>\n" +
                                "<body>\n" +
                                "\n" +
                                "\t<table>\n" +
                                "\t\t<tr><th>title</th><th>body</th></tr>\n" +
                                "\t\t\n";
                        try {
                            ParsearXML toXML  = new ParsearXML();
                            ArrayList<Servicio> misDatos =  (ArrayList) toXML.parsear(inputStream);

                            for (Servicio dat:misDatos) {
                                tabla_html+= "<tr><th>" + dat.getBody() + "</th><th>"+ dat.getTitle()+"</th></tr>";
                            }

                            tabla_html+="\t</table>\n" +
                                    "\n" +
                                    "\n" +
                                    "</body>\n" +
                                    "</html>";




                            wb_listado.setWebViewClient(new WebViewClient());
                            wb_listado.getSettings().setJavaScriptEnabled(true);
                            wb_listado.loadData(tabla_html,"text/html", null);



                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response","Error");
                        Toast toastError = Toast.makeText(getApplicationContext(),"No ha sido Insertado",Toast.LENGTH_SHORT);
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders()throws AuthFailureError
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Accept","aplication/xml");
                return params;
            }
        };

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}

