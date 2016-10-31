package com.example.nofear676.blocknote;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by NoFear676 on 5/23/2016.
 */
public class AgregarNota extends AppCompatActivity {
    ImageButton Add;
    EditText TITLE, CONTENT;
    String type, getTitle,msj;
    private static final int SALIR = Menu.FIRST;
    AdaptadorBD DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_nota);

        Add = (ImageButton) findViewById(R.id.button_Add);
        TITLE = (EditText) findViewById(R.id.editText_Titulo);
        CONTENT = (EditText) findViewById(R.id.editText_Nota);

        Bundle bundle = this.getIntent().getExtras();//Captura el id de la variable "type"
        String content;
        getTitle = bundle.getString("title");
        content = bundle.getString("content");


        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarNota();
                msj = "nota creada.";
                Mensaje(msj);
            }
        });


    }

    //Metodo sobreescrito de la clase listaactivity que se encarga de crear el menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu);

        menu.add(1, SALIR, 0, R.string.menu_Inicio);

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
                Intent intent = new Intent(AgregarNota.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            //break
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void agregarNota() {
        DB = new AdaptadorBD(this);
        String title, content, msj;
        title = TITLE.getText().toString();
        content = CONTENT.getText().toString();

        if (title.equals("")) {
            msj = "El titulo no puede estar vacio";
            TITLE.requestFocus();
            Mensaje(msj);
        } else {

                Cursor c = DB.getNote(title);
                String gettitle = "";
                if (c.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya mas registros
                    do {
                        gettitle = c.getString(1);
                    }
                    while (c.moveToNext());
                }
                if (gettitle.equals(title)) {
                    TITLE.requestFocus();
                    msj = "El titulo de la nota ya existe.";
                    Mensaje(msj);
                } else {
                    DB.addNote(title, content);
                    actividad(title, content);
                }

        }
    }



    public void Mensaje (String msj)
    {
        Toast toast= Toast.makeText(this, msj, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.show();

    }
    public void actividad(String title, String content)
    {
        Intent intent = new Intent(AgregarNota.this,MainActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("content",content);
        startActivity(intent);
    }
}
