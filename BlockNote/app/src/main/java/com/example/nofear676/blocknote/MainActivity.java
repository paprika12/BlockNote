package com.example.nofear676.blocknote;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int SALIR = Menu.FIRST;

    ListView Lista;
    TextView textLista;
    AdaptadorBD DB;
    List<String> item = null;
    String getTitle;
    ImageButton Add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textLista = (TextView) findViewById(R.id.textView_Lista);
        Lista = (ListView) findViewById(R.id.listView_Lista);
        Lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getTitle = (String) Lista.getItemAtPosition(position);
                actividad("");
            }
        });
        Add = (ImageButton)findViewById(R.id.imageButton_add);
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actividad("add");
            }
        });
        mostrarNotas();


    }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            super.onCreateOptionsMenu(menu);

            menu.add(1, SALIR, 0, R.string.menu_salir);

            return true;
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //Mediante getItemId se obtiene el valor del boton pulsado
        switch (id) {
            //Si el boton pulsao es salir, finaliza la app
            case SALIR:
                /*El CookieSyncManager se utilizara para sincronizar el almacen de cookies navegador entre la
                 * memoria ram y el almacenamiento permanente. Para obtener el mejor rendimiento, las cookies del
                  * navegador se guardan en la memoria ram. Un hilo separado guarda las cookies entre impulsaor por un temporizador*/
                CookieSyncManager.createInstance(this);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeAllCookie();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                finish();
                return true;
            //break
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void actividad(String act) {
        String type = "", content = "";
        if (act.equals("add")) {
            type = "add";
            Intent intent = new Intent(MainActivity.this, AgregarNota.class);
            intent.putExtra("type", type);
            startActivity(intent);
        }
        else{
           // if(act.equals("edit")){
                type="edit";
                content =obtenerNota();
                Intent intent = new Intent(MainActivity.this,VerNota.class);
                intent.putExtra("title",getTitle);
                intent.putExtra("content",content);
                startActivity(intent);
            //}
        }

    }


    public void mostrarNotas() {
        DB = new AdaptadorBD(this);
        Cursor c = DB.getNotes();
        item = new ArrayList<String>();
        String title = "";
        if (c.moveToFirst() == false) {
            textLista.setText("Tu lista esta vacia.");
        } else {
            do {
                title = c.getString(1);
                item.add(title);
            } while (c.moveToNext());
        }


    ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, item);
        Lista.setAdapter(adaptador);

}
    public String obtenerNota(){
        String type="",content ="";
        DB = new AdaptadorBD(this);
        Cursor c= DB.getNote(getTitle);
    if(c.moveToFirst()){
        do{
            content = c.getString(2);
        }
        while(c.moveToNext());
    }
        return  content;
    }

}