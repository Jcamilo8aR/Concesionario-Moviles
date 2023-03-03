package com.example.concesionario;

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

public class VehiculoActivity extends AppCompatActivity {

    ClsOpenHelper admin=new ClsOpenHelper(this,"Concesionario.db",null,1);

    EditText jetplaca,jetmarca,jetmodelo;

    CheckBox jcbactivo;
    String placa,marca,modelo;
    long respuesta;
    byte sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculo);

        getSupportActionBar().hide();
        jetplaca=findViewById(R.id.etplaca);
        jetmarca=findViewById(R.id.etmarca);
        jetmodelo=findViewById(R.id.etmodelo);
        jcbactivo=findViewById(R.id.cbactivo);
        jetplaca.requestFocus();
        sw=0;
    }


    public void Guardar(View view){
        placa=jetplaca.getText().toString();   // EVNIAMOS LO QUE CONTIENE EL OBJETO A LA RAM
        modelo=jetmodelo.getText().toString();
        marca=jetmarca.getText().toString();
        if (placa.isEmpty() || modelo.isEmpty() || marca.isEmpty()){
            Toast.makeText(this, "Los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetplaca.requestFocus();
        }else{
            SQLiteDatabase db=admin.getWritableDatabase();  // ABRIMOS LA BD (es igual a "admin" por como la llamamos al comienzo de este archivo)
            ContentValues registro=new ContentValues();   // CREAMOS UN NUEVO CONTENEDOR
            registro.put("Placa",placa);   // MANDAMOS A LA BD
            registro.put("modelo",modelo);
            registro.put("marca",marca);
            if (sw == 0)
                respuesta=db.insert("TblVehiculo",null,registro);   // 0 = NO GUARDO, 1 o MAS = GUARDO , CREAOMOS UNA VARIABLE LONG PARA QUE NOS DE LA RESPUESTA
            else {
                respuesta = db.update("TblVehiculo", registro, "Placa='" + placa + "'", null);
                sw=0;
            }
            if (respuesta > 0){
                Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
                Limpiar_campos();   //  EJECUTAMOS METODO PARA LIMPIAR
            }else{
                Toast.makeText(this, "Error guardando registro", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }
    }


    public void Consultar(View view){   // NECESITAMOS EL MODIFICAR ANTES DE PODER HACER EL ACTUALIZAR
        placa=jetplaca.getText().toString();
        if (placa.isEmpty()){
            Toast.makeText(this, "Identificacion requerida para consultar", Toast.LENGTH_SHORT).show();
            jetplaca.requestFocus();
        }else{
            SQLiteDatabase db=admin.getReadableDatabase();
            Cursor fila=db.rawQuery("select * from TblVehiculo where Placa='"+placa+"'",null);   // USAMOS ' SI LA INFORMACION ES ALFANUMERICA
            if (fila.moveToNext()){   // SI SE MUEVE ES PORQUE ENCONTRO EL REGISTRO
                sw = 1;
                if (fila.getString(3).equals("Si")){   // SI ES IGUAL A SI LO MOSTRAMO
                    jetmodelo.setText(fila.getString(1));
                    jetmarca.setText(fila.getString(2));
                    jcbactivo.setChecked(true);
                }else{
                    Toast.makeText(this, "Registro anulado, para verlo se debe activar", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Registro no existe", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }
    }


    public void anular(View view){   // SI ESTA ACTIVO O NO
        if (sw == 1){ // SI EL SWITCH ES 0 ES QUE NO HAN CONSULTADO
            SQLiteDatabase db=admin.getWritableDatabase();
            ContentValues registro=new ContentValues();
            registro.put("activo","No");   // CAMBIAMOS EL ESTADO DE ACTIVO A NO
            respuesta= db.update("Tblvehiculo",registro,"Placa='"+placa+"'",null);
            if (respuesta > 0) {  // SI EL VALOR ES 0 ES QUE NO SE PUDO ANULAR, SI ES 1 O MAYOR ES QUE SE ANULO
                Toast.makeText(this, "Registro anulado", Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            }else{
                Toast.makeText(this, "Error anulando registro", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }else{
            Toast.makeText(this, "Debe consultar primero para poder anular", Toast.LENGTH_SHORT).show();
            jetplaca.requestFocus();
        }
    }


    public void activar(View view){   // SI ESTA ACTIVO O NO
        if (sw == 1){ // SI EL SWITCH ES 0 ES QUE NO HAN CONSULTADO
            SQLiteDatabase db=admin.getWritableDatabase();
            ContentValues registro=new ContentValues();
            registro.put("activo","Si");   // CAMBIAMOS EL ESTADO DE ACTIVO A SI
            respuesta= db.update("TblVehiculo",registro,"placa='"+placa+"'",null);
            if (respuesta > 0) {  // SI EL VALOR ES 0 ES QUE NO SE PUDO ANULAR, SI ES 1 O MAYOR ES QUE SE ACTIVO
                Toast.makeText(this, "Registro Activado", Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            }else{
                Toast.makeText(this, "Error activando registro", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }else{
            Toast.makeText(this, "Debe consultar primero para poder activar", Toast.LENGTH_SHORT).show();
            jetplaca.requestFocus();
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
        jetplaca.setText("");
        jetmarca.setText("");
        jetmodelo.setText("");
        jcbactivo.setChecked(false);
        jetplaca.requestFocus();
        sw=0;




}
}