package com.example.nofear676.blocknote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by NoFear676 on 5/23/2016.
 */
public class AdaptadorBD extends SQLiteOpenHelper {
    //Strings finales que contienen las cadenas de lo que se guardara en la BD
    public static  final String TABLE_ID = "id_nota";
    public static  final String TITLE = "titulo";
    public static  final String CONTENT = "contenido";
    //Nombre de la base de datos, de la tabla de la de la base de datos a utilizar
    public static  final String DATABASE = "BlockNota";
    public static  final String TABLE = "Nota";
    public AdaptadorBD(Context context) {
        super(context, DATABASE, null, 1);
    }

    /*Este metodo se encarga de inicializar la base de datos
    * se efectua siempre cuando se crea la clase*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLE +"("+
                TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
        TITLE + " TEXT," + CONTENT +" TEXT);");

    }

    /*Metodo usado en el caso de que haga falta actualizar
    * la version de la base de datos*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST" + TABLE);
        onCreate(db);

    }
    public void addNote (String title, String content){
        ContentValues valores = new ContentValues();
        valores.put(TITLE,title);
        valores.put(CONTENT,content);
        this.getWritableDatabase().insert(TABLE,null,valores);

    }
    public void updateNote (String title, String content,String title2){
        ContentValues valores = new ContentValues();
        valores.put(TITLE,title2);
        valores.put(CONTENT,content);
        String[] args = new String[] {title};
        this.getWritableDatabase().update(TABLE,valores,TITLE+"=?",args);

    }
    //Meotodo que devuelve una nota.
    public Cursor getNote (String condition)
    {
        String columnas[]={TABLE_ID,TITLE,CONTENT};
        String[] args = new String[] {condition};
        Cursor c= this.getReadableDatabase().query(TABLE, columnas,TITLE+"=?",args,null,null,null);
        return c;
    }

    public Cursor getNotes ()
    {
        String columnas[]={TABLE_ID,TITLE,CONTENT};
        Cursor c= this.getReadableDatabase().query(TABLE, columnas,null,null,null,null,null);
        return c;
    }
    public void deleteNote (String condition)
    {
        String[] args = new String[] {condition};
        this.getWritableDatabase().delete(TABLE,TITLE+"=?",args);
    }
}
