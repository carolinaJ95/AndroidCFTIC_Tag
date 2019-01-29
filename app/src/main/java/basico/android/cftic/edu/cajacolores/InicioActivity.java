package basico.android.cftic.edu.cajacolores;

import android.content.Intent;
import android.media.Image;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class InicioActivity extends AppCompatActivity {

    private String nombreString;
    private EditText nombreEntrada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        Intent intent = getIntent();
        Boolean controlEntrada = intent.getBooleanExtra("control", false);
        ImageView iconoCamara = (ImageView) findViewById(R.id.camara);

        if (Preferencias.primeraVez(this) || controlEntrada != false) {
            Preferencias.marcarPrimeraVez(this);
        } else {
            irAMain();
        }
    }

    public void entrar(View v) {
        obtenerNombre();
        irAMain();
    }

    private void irAMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("USUARIO", nombreString);
        startActivity(intent);
    }


    public void obtenerNombre() {
        nombreEntrada = (EditText) findViewById(R.id.cajaNombreEntrada);
        nombreString = nombreEntrada.getText().toString();
    }

    public void cancelar(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        setResult(RESULT_CANCELED);
        finish();
    }


    static final int REQUEST_IMAGE_CAPTURE = 1;

    public void foto(View view) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }


}
