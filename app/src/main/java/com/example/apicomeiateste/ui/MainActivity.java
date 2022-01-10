package com.example.apicomeiateste.ui;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apicomeiateste.R;
import com.example.apicomeiateste.adapters.Adapter_InfoCidades;
import com.example.apicomeiateste.interfaces.Interface_OnClick;
import com.example.apicomeiateste.model.TempCidadeModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements Interface_OnClick {

    private Button botaoRecuperar;
    private TextView textResultado;
    private EditText edtBarra;
    private TempCidadeModel objeto_inf_cidade;
    private String cidade = "";

    //Para recyclerView
    private RecyclerView recyclerInfoCidades;
    Adapter_InfoCidades adapter_infoCidades;

    ArrayList<TempCidadeModel> listInfoCidades = new ArrayList<TempCidadeModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        botaoRecuperar = findViewById(R.id.btnConsultar);
        textResultado = findViewById(R.id.textResultado);
        edtBarra = findViewById(R.id.edtBarra);
        recyclerInfoCidades = findViewById(R.id.recyclerViewInfoCidades);


        iniciarCidadesPrimarias();
        initRecyclerView();

        botaoRecuperar.setOnClickListener(v -> {
            cidade = edtBarra.getText().toString();
            if (edtBarra.length() <= 0) {
                Toast.makeText(this, "Preencha o nome da cidade", Toast.LENGTH_LONG).show();
                passarValoresDaLista();

            } else {
                MyTask task = new MyTask();
                String urlApi = "https://api.openweathermap.org/data/2.5/weather?q=" + cidade + "&appid=70c7006c98c1a2e32a1cdb95e763da0f&lang=pt_br";
                task.execute(urlApi); // onde é executado a URL
            }
        });

    }

    private void initRecyclerView() {
        adapter_infoCidades = new Adapter_InfoCidades(listInfoCidades,this);

        LinearLayoutManager layout = new LinearLayoutManager(this);
        recyclerInfoCidades.setLayoutManager(layout);
        recyclerInfoCidades.setAdapter(adapter_infoCidades);
    }


    private void passarValoresDaLista(){

    }


    private void iniciarCidadesPrimarias() {


        ArrayList<String> cidadesPrimarias = new ArrayList<>();
        cidadesPrimarias.add("Agrestina");
        cidadesPrimarias.add("Altinho");
        cidadesPrimarias.add("Abreu e Lima");
        cidadesPrimarias.add("Alagoas");
        cidadesPrimarias.add("Brasília");

        // Add cidades
        for (int cont = 0; cont <= cidadesPrimarias.size() - 1; cont++) {
            cidade = cidadesPrimarias.get(cont);
            MyTask task = new MyTask();
            // URL do projeto
            // Id do projeto
            // onde é executado a URL
            String urlApi = "https://api.openweathermap.org/data/2.5/weather?q=" + cidade + "&appid=70c7006c98c1a2e32a1cdb95e763da0f&lang=pt_br";
            task.execute(urlApi);
        }
    }

    @Override
    public void onLongClick(int position) {
        removerItemDaLista(position);

    }


    private void removerItemDaLista(int positionDaLista){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.remover));
        dialog.setMessage(getString(R.string.msg_remover));
        dialog.setIcon(R.drawable.ic_baseline_warning_24);
        dialog.setNegativeButton(getString(R.string.nao), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Operação Cancelada", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.setPositiveButton(getString(R.string.sim), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listInfoCidades.remove(positionDaLista);
                adapter_infoCidades.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Excluído", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();

    }


    class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String stringUrlApi = strings[0];
            InputStream inputStream;
            InputStreamReader inputStreamReader = null;
            StringBuffer buffer = null; // Para montar linha a linha todo o arquivo

            try {
                URL url = new URL(stringUrlApi);
                HttpsURLConnection conexao = (HttpsURLConnection) url.openConnection(); // Requisição da API

                // Recuperando Json em Bytes
                inputStream = conexao.getInputStream(); // recuperando os dados do servidor

                // InputStreamReader Recuperando Json em bytes e convertendo para caracteres
                inputStreamReader = new InputStreamReader(inputStream);

                // Lendo os caracteres do InputStreamReader
                BufferedReader reader = new BufferedReader(inputStreamReader);

                buffer = new StringBuffer();
                String linha = "";
                while ((linha = reader.readLine()) != null) { // vai ler cada linha enquanto for dif de null
                    buffer.append(linha);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return buffer.toString(); // Convertendo para String a requisição
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);

            String temperature = null;
            String temp_min = null;
            String temp_max = null;
            String humidity = null;
            String city = null;
            String country = null;
            String nomePais = null;

            try {
                //         ----- Requisição Cidade -----
                JSONObject jsonObject = new JSONObject(resultado);
                city  = jsonObject.getString("name");

                //         ------ Requisição País -------
                country = jsonObject.getString("sys");
                JSONObject jsonObject2 = new JSONObject(country);
                nomePais = jsonObject2.getString("country");

                //         ----- Requisição temperatura -----
                temperature = jsonObject.getString("main");
                JSONObject jsonObject1 = new JSONObject(temperature);
                humidity = jsonObject1.getString("humidity");
                temp_min = jsonObject1.getString("temp_min");
                temp_max = jsonObject1.getString("temp_max");


                objeto_inf_cidade = new TempCidadeModel(temp_min,temp_max,city,nomePais,humidity);

                listInfoCidades.add(objeto_inf_cidade);
                initRecyclerView();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}