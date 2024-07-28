package com.software.aplicacionia;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ImageView imgFotografia, imgGuardar;
    private ImageButton btnCamara, btnGaleria;
    private static final int CAMARA_REQUEST_CODE = 100;
    private static final int GALERIA_REQUEST_CODE = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imgFotografia = findViewById(R.id.ivFotografia);

        imgGuardar = findViewById(R.id.ibGuardar);
        btnCamara = findViewById(R.id.ibCamara);
        btnGaleria = findViewById(R.id.ibGaleria);

        btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camara = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
                startActivityForResult(camara, CAMARA_REQUEST_CODE);


            }
        });

        btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galeria = new Intent(Intent.ACTION_PICK);
                galeria.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galeria, GALERIA_REQUEST_CODE);
            }
        });
    }

    //METODOS


    @Override

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMARA_REQUEST_CODE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imgFotografia.setImageBitmap(imageBitmap);

                //GUARDAR IMAGEN
                guardarImagenGaleria(imageBitmap);
            }

            if (requestCode == GALERIA_REQUEST_CODE) ;
            imgFotografia.setImageURI(data.getData());
        }

    }

    private void guardarImagenGaleria(Bitmap imageBitmap) {
        File directorio;
        directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String codigo = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date());

        String nombre_archivo = "IMG_" + codigo + ".jpg";
        File imageFile = new File(directorio, nombre_archivo);

        if (!directorio.exists()){
            directorio.mkdir();
        }

        imageBitmap = imageBitmap.copy(Bitmap.Config.ARGB_8888, true);

        try {

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(Uri.fromFile(imageFile));
            sendBroadcast(mediaScanIntent);

            Toast.makeText(this, "Imagen guardad correctamente", Toast.LENGTH_SHORT).show();

        }catch (FileNotFoundException e){
            throw new RuntimeException(e);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

}