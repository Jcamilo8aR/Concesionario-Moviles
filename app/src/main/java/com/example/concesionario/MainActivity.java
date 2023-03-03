package com.example.concesionario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void Clientes(View view){
        Intent intclientes=new Intent(this,ClientesActivity.class);  // DICE QUE HACE
        startActivity(intclientes);   // DICE QUE LA EJECUTE
    }


    public void Vehiculo(View view){
        Intent intvehiculo=new Intent(this,VehiculoActivity.class);
        startActivity(intvehiculo);
    }

    public void Ventas(View view){
        Intent intventas=new Intent(this,VentaActivity.class);
        startActivity(intventas);
    }
}