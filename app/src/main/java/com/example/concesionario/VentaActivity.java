package com.example.concesionario;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class VentaActivity extends AppCompatActivity {

    ClsOpenHelper admin = new ClsOpenHelper(this, "Concesionario.db", null, 1);

    EditText jetcodigo, jetidentificacion, jetfecha, jetplaca;
    CheckBox jcbactivo;
    String codigo, identificacion, fecha, placa;
    long respuesta;
    byte sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta);

        getSupportActionBar().hide();
        jetidentificacion = findViewById(R.id.etidentificacion);
        jetcodigo = findViewById(R.id.etcodigo);
        jetfecha = findViewById(R.id.etfecha);
        jetplaca = findViewById(R.id.etplaca);
        jcbactivo = findViewById(R.id.cbactivo);
        jetcodigo.requestFocus();
        sw = 0;
    }



    public void Guardar(View view){
        codigo=jetcodigo.getText().toString();
        fecha=jetfecha.getText().toString();
        identificacion=jetidentificacion.getText().toString();
        placa=jetplaca.getText().toString();
        if(codigo.isEmpty() || fecha.isEmpty() || identificacion.isEmpty() || placa.isEmpty()){
            Toast.makeText(this, "Datos requeridos", Toast.LENGTH_SHORT).show();
        }else{
            SQLiteDatabase db=admin.getWritableDatabase();
            ContentValues registro=new ContentValues();
            registro.put("codigo",codigo);
            registro.put("fecha",fecha);
            registro.put("Identificacion",identificacion);
            registro.put("Placa",placa);
            if (sw == 0)
                respuesta=db.insert("TblVenta",null,registro);
        else{
            respuesta=db.update("Tblventa",registro,"codigo='"+codigo+"'",null );
            sw=0;
        }
        if(respuesta > 0){
            Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        db.close();
        }
    }


    public void consultar(View view) {
        codigo = jetcodigo.getText().toString();
        if (codigo.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos para continuar", Toast.LENGTH_SHORT).show();
        } else {
            SQLiteDatabase db = admin.getReadableDatabase();
            Cursor fila = db.rawQuery("select * from TblVenta where codigo='" + codigo + "'", null);
            if (fila.moveToNext()) {
                sw = 1;
                if (fila.getString(3).equals("Si")) {
                    jetfecha.setText((fila.getString(1)));
                    jetidentificacion.setText(fila.getString(2));
                    jetplaca.setText(fila.getString(3));
                    jcbactivo.setChecked(true);
                } else {
                    Toast.makeText(this, "Registro no esta activo", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Registro no existe", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }
    }





    public void anular(View  view){
        if (sw == 1){
            SQLiteDatabase db=admin.getWritableDatabase();
            ContentValues registro=new ContentValues();
            registro.put("activo","No");
            respuesta= db.update("TblVenta",registro,"codigo='"+codigo+"'",null);
            registro.put("activo","Si");  // CAMBIAMOS EL ESTADO DEL VEHICULO A ACTIVO
            respuesta= db.update("TblVehiculo",registro,"placa='"+placa+"'",null);
            if (respuesta > 0) {
                Toast.makeText(this, "Registro anulado", Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            }else{
                Toast.makeText(this, "Error anulando registro", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }else{
            Toast.makeText(this, "Debe consultar primero para poder anular", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }
    }


    public void activar(View view){   // SI ESTA ACTIVO O NO
        if (sw == 1){ // SI EL SWITCH ES 0 ES QUE NO HAN CONSULTADO
            SQLiteDatabase db=admin.getWritableDatabase();
            ContentValues registro=new ContentValues();
            registro.put("activo","Si");   // CAMBIAMOS EL ESTADO DE ACTIVO A SI
            respuesta= db.update("tblVenta",registro,"codigo='"+codigo+"'",null);
            if (respuesta > 0) {  // SI EL VALOR ES 0 ES QUE NO SE PUDO ANULAR, SI ES 1 O MAYOR ES QUE SE ACTIVO
                Toast.makeText(this, "Registro Activado", Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            }else{
                Toast.makeText(this, "Error activando registro", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }else{
            Toast.makeText(this, "Debe consultar primero para poder activar", Toast.LENGTH_SHORT).show();
            jetidentificacion.requestFocus();
        }
    }




    public void cancelar(View view){
        Limpiar_campos(); //HACEMOS QUE LLAME A LIMPIAR CAMPOS
    }


    public void regresar(View view){
        Intent intmain=new Intent(this,MainActivity.class);
        startActivity(intmain);
    }


    private void Limpiar_campos(){   // METODO PRIVADO YA QUE SOLO LO NECESITAMOS EN UNA CLASE
        jetcodigo.setText("");
        jetidentificacion.setText("");
        jetplaca.setText("");
        jetfecha.setText("");
        jcbactivo.setChecked(false);
        jetcodigo.requestFocus();
        sw=0;
    }
}
