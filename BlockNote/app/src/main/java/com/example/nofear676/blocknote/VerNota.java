package com.example.nofear676.blocknote;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by NoFear676 on 5/23/2016.
 */
public class VerNota  extends AppCompatActivity {
    private static final int SALIR = Menu.FIRST + 1;
    private static final int ELIMINAR = Menu.FIRST;
    String title,content;
    TextView TITLE;
    EditText CONTENT;
    ImageButton SAVE,ELIMINA;
    AdaptadorBD DB;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_nota);

        Bundle bundle = this.getIntent().getExtras();

        title = bundle.getString("title");
        content = bundle.getString("content");

        TITLE = (TextView) findViewById(R.id.textView_Titulo);
        CONTENT = (EditText) findViewById(R.id.editText_content);

        SAVE = (ImageButton) findViewById(R.id.imageButton_save);
        ELIMINA= (ImageButton) findViewById(R.id.imageButton_Elimina);
        TITLE.setText(title);
        CONTENT.setText(content);
        TITLE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TITLE.setCursorVisible(true);
                TITLE.setFocusableInTouchMode(true);
                TITLE.setInputType(InputType.TYPE_CLASS_TEXT);
                TITLE.requestFocus(); //to trigger the soft input

            }
        });


        SAVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUpdateNotes();
            }
        });
        ELIMINA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote();
            }
        });
    }
        private void addUpdateNotes () {
            DB = new AdaptadorBD(this);
            String title2, msj;
            title2 = TITLE.getText().toString();
            content = CONTENT.getText().toString();
            Cursor c = DB.getNote(title2);
            String gettitle = "";
            if (title.equals(title2)) {
                DB.updateNote(title, content, title2);
                actividad();
                msj = "nota guardada.";
                Mensaje(msj);
            } else {
                if(title2.equals("")){
                    msj = "El titulo no puede estar vacio.";
                    Mensaje(msj);
                }else {
                    if (c.moveToFirst()) {
                        //Recorremos el cursor hasta que no haya mas registros
                        do {
                            gettitle = c.getString(1);
                        }
                        while (c.moveToNext());
                    }
                    if (gettitle.equals(title2)) {
                        TITLE.requestFocus();
                        msj = "El titulo de la nota ya existe.";
                        Mensaje(msj);
                    } else {
                        DB.updateNote(title, content, title2);
                        actividad();
                        msj = "nota guardada.";
                        Mensaje(msj);
                    }
                }
            }
        }
    private void deleteNote () {
        String msj;
        DB = new AdaptadorBD(this);
        actividad();
        DB.deleteNote(title);
        msj = "nota eliminada.";
        Mensaje(msj);
    }
    public void actividad()
    {
        Intent intent = new Intent(VerNota.this,MainActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("content",content);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu);
       // menu.add(1, ELIMINAR, 0, R.string.menu_eliminar);
        menu.add(2, SALIR, 0, R.string.menu_Inicio);

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
                Intent intent = new Intent(VerNota.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;

            case ELIMINAR:
                deleteNote();

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public void Mensaje (String msj)
    {
        Toast toast= Toast.makeText(this, msj, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.show();

    }

}
