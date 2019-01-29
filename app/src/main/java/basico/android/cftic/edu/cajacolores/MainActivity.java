package basico.android.cftic.edu.cajacolores;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {


    private int color_tocado;
    private int nveces;
    private long tinicial;
    private long tfinal;
    private Puntuacion puntuacion;
    private long total;
    private String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.nveces = 0;
        this.color_tocado = ResourcesCompat.getColor(getResources(), R.color.tocado, null);

        this.nombre = obtenerNombre();

        //LEO EL RECORD Y EL NOMBRE
        long record_actual = Preferencias.leerRecord(this);
        Log.d("MIAPP", "Record actual " + record_actual);

        //DIBUJA LA IMAGEN DE LA FLECHA
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initCrono() {
        Chronometer c = findViewById(R.id.crono);
        c.setBase(SystemClock.elapsedRealtime());
        c.start();
    }

    public void ocultarBoton(View v) {
        Log.d("MIAPP", "Tocó el botón");
        //TODO quitar el botón u ocultarlo
        v.setVisibility(View.GONE);
        this.tinicial = System.currentTimeMillis();
        initCrono();

        Snackbar.make(v, "COMIENZA EL JUEGO !!", Snackbar.LENGTH_LONG).show();

        quitarTituloBarra();
        ocultarActionBar();


    }

    private void cerrar(long tiempo_total) {
        long segundos = tiempo_total / 1000;
        Toast toast = Toast.makeText(this, segundos + " segundos", Toast.LENGTH_SHORT);
        toast.show();//informo


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.finish();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();

    }

    public void cambiaColor(View v) {
        Log.d("MIAPP", "TOCÓ CAJA");
        //v.setVisibility(View.GONE);
        Object tag = v.getTag();//obtengo la info asociada
        if (tag == null) {
            this.nveces++;
            v.setTag(true);//le hago una marquita a la caja tocada
            v.setBackgroundColor(this.color_tocado);
            if (this.nveces == 12) {
                this.tfinal = System.currentTimeMillis();
                total = tfinal - tinicial;
                //GUARDAR RECORD
                Preferencias.guardarRecord(total, this);
                cerrar(total);

            }

        }

    }

    private String obtenerNombre() {
        String nombre = null;

        if (getIntent().getExtras() != null) {
            Log.d("MIAPP", "Trae el nombre del intent");
            nombre = getIntent().getStringExtra("USUARIO");
            Preferencias.guardarNombre(nombre, this);
        } else {
            nombre = Preferencias.leerNombre(this);
        }


        return nombre;
    }


    public boolean onCreateOptionsMenu(Menu menu) {

        //METODO ENCARGADO DE CREAR EL MENU

        getMenuInflater().inflate(R.menu.menu_superior, menu); //recoge el recurso del menu para posteriormete dibujarlo
        return super.onCreateOptionsMenu(menu);

    }

    public boolean onOptionsItemSelected(MenuItem item) { //METODO QUE SALTA CUANDO SE CLICA EN EL MENU

        //View iconoMenu = findViewById(R.id.cambiar_usuario);
        if (R.id.cambiar_usuario == item.getItemId()) {  //SE COMPARA EL ITEM DE LA VENTANA CON EL ID DEL MENU

            String control = "control";
            //PERMITE CAMBIAR EL NOMBRE DEL USUARIO
            Intent intent = new Intent(this, InicioActivity.class);
            intent.putExtra(control, true);
            //startActivity(intent);
            startActivityForResult(intent, 22);
        } else if (android.R.id.home == item.getItemId()) {
            super.onBackPressed();
        }


        return super.onOptionsItemSelected(item);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 22) {
            if (resultCode == RESULT_CANCELED) {

                Toast.makeText(this, "NO SE MODIFICA NOMBRE", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, InicioActivity.class);
                startActivity(intent);
                setResult(RESULT_CANCELED);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void ocultarActionBar() {
        getSupportActionBar().hide();
    }

    private void quitarTituloBarra() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }



}
