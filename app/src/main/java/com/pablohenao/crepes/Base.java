package com.pablohenao.crepes;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import static com.pablohenao.crepes.R.layout.*;

/**
 * Created by Home on 01/06/2015.
 */
public class Base extends ActionBarActivity implements View.OnClickListener {
    private DataBaseManager Manager;
    private Cursor cursor;
    private ListView lista;
    private SimpleCursorAdapter adapter;
    private EditText Ednombre;
    //private Button btnbuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(base);

        Manager = new DataBaseManager(this);
        lista = (ListView) findViewById(android.R.id.list);
        Ednombre = (EditText) findViewById(R.id.EdText1);

        String[] from = new String[]{Manager.CN_NAME,Manager.CN_LONGITUD,Manager.CN_LONGITUD};
        int[] to = new int[]{R.id.texto1,R.id.texto2,R.id.texto2,R.id.texto2,};
        cursor = Manager.cargarCursorContactos();
        adapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,cursor,from,to,0);
        lista.setAdapter(adapter);


        Button btnbuscar = (Button) findViewById(R.id.btn1);
        btnbuscar.setOnClickListener(this);
        Button btncargar = (Button) findViewById(R.id.btndb);
        btncargar.setOnClickListener(this);
        Button btninsertar = (Button) findViewById(R.id.btninsertar);
        btninsertar.setOnClickListener(this);
        Button btneliminar = (Button) findViewById(R.id.btneliminar);
        btneliminar.setOnClickListener(this);
        Button btnactualizar = (Button) findViewById(R.id.btnactualizar);
        btnactualizar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn1){
            new BuscarTask().execute();
        }
        if(v.getId()==R.id.btndb){
            lista = (ListView) findViewById(android.R.id.list);
            Ednombre = (EditText) findViewById(R.id.EdText1);

            String[] from = new String[]{Manager.CN_NAME,Manager.CN_LONGITUD,Manager.CN_LONGITUD};
            int[] to = new int[]{R.id.texto1,R.id.texto2,R.id.texto3};
            cursor = Manager.cargarCursorContactos();
            adapter = new SimpleCursorAdapter(this,R.layout.list_item,cursor,from,to,0);
            lista.setAdapter(adapter);

        }
        if (v.getId()==R.id.btninsertar){
            EditText nombre = (EditText) findViewById(R.id.EdNombre);
            EditText longitud = (EditText) findViewById(R.id.EdLongitud);
            EditText latitud = (EditText) findViewById(R.id.EdLatitud);
            Manager.insertar(nombre.getText().toString(),longitud.getText().toString(),latitud.getText().toString());
            nombre.setText("");
            longitud.setText("");
            Toast.makeText(getApplicationContext(),"Insertado", Toast.LENGTH_SHORT).show();
        }
        if(v.getId()==R.id.btneliminar){
            EditText nombre = (EditText) findViewById(R.id.EdNombre);
            Manager.eliminar(nombre.getText().toString());
            Toast.makeText(getApplicationContext(),"Eliminado", Toast.LENGTH_SHORT).show();
            nombre.setText("");
        }
        if (v.getId()==R.id.btnactualizar){
            EditText nombre = (EditText) findViewById(R.id.EdNombre);
            EditText longitud = (EditText) findViewById(R.id.EdLongitud);
            EditText latitud = (EditText) findViewById(R.id.EdLatitud);
            Manager.Modificardatos(nombre.getText().toString(),longitud.getText().toString(),latitud.getText().toString());
            Toast.makeText(getApplicationContext(),"Actualizado", Toast.LENGTH_SHORT).show();
            nombre.setText("");
            longitud.setText("");
            latitud.setText("");
        }
    }



    private class BuscarTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(),"Buscando...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            cursor = Manager.buscarContacto(Ednombre.getText().toString());
            return null;
        }



        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getApplicationContext(),"Finalizado", Toast.LENGTH_SHORT).show();
            adapter.changeCursor(cursor);
            obtener();
        }
    }

    public void obtener () {
        TextView Txnombre = (TextView) findViewById(R.id.Txnombre);
        TextView Txlongitud = (TextView) findViewById(R.id.TxLongitud);
        TextView Txlatitud = (TextView) findViewById(R.id.TxLatitud);
        try{
            String dbnombre = cursor.getString(cursor.getColumnIndex(Manager.CN_NAME));
            Txnombre.setText(dbnombre);
            String dblongitud = cursor.getString(cursor.getColumnIndex(Manager.CN_LONGITUD));
            Txlongitud.setText(dblongitud);
            String dblatitud = cursor.getString(cursor.getColumnIndex(Manager.CN_LATITUD));
            Txlatitud.setText(dblatitud);
        }
        catch(CursorIndexOutOfBoundsException e){
            Txnombre.setText("Not Found");
            Txlongitud.setText("Not Found");
            Txlatitud.setText("Not Found");
        }

    }


}