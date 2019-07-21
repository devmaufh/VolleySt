package com.example.sqlite.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.sqlite.API.VolleySingleton
import com.example.sqlite.Database.adBD
import com.example.sqlite.R
import com.example.sqlite.Utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    var control:String=""
    var carrera:String=""
    var nom:String=""
    var edad:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }



    //Cambia a la siguiente actividad
    fun navigateToList(v:View){
        startActivity(Intent(this, ListaActivity::class.java))
    }


    fun searchStudent(v: View){
        if(!etcontrol.text.isEmpty()){
            control=etcontrol.text.toString()
            val database = adBD(this)
            val tupla=database.consulta("SELECT noControl,nomEst,carrera,edadEst FROM Estudiante where noControl='$control'")
            if(tupla!!.moveToFirst()){
                etnombre.setText(tupla.getString(0))
                etnombre.setText(tupla.getString(1))
                etcarrera.setText(tupla.getString(2))
                etedad.setText(tupla.getString(3))
            }else{
                Toast.makeText(this, "No hay registros almacenados", Toast.LENGTH_SHORT).show();
                etcontrol.requestFocus()
            }
        }else{
            Toast.makeText(this, "No puedes dejar los campos vacios", Toast.LENGTH_SHORT).show();
        }
    }
    fun addStudent(v:View) {
        if (!etcontrol.text.isEmpty() && !etcarrera.text.isEmpty() && !etedad.text.isEmpty() && !etnombre.text.isEmpty()) {
            getValues()
            val database= adBD(this)

            var jsonStudent=JSONObject()
            jsonStudent.put("noControl",control)
            jsonStudent.put("nomEst",nom)
            jsonStudent.put("carrera",carrera)
            jsonStudent.put("edad",edad)

            sendRequest(jsonStudent)

            val tupla=database.Ejecuta("INSERT INTO Estudiante(noControl,nomEst,carrera,edadEst) VALUES(" +
                    "'$control'," +
                    "'$nom'," +
                    "'$carrera'," +
                    "'$edad')")
            if(tupla==1){
                Toast.makeText(this, "DB: Registro insertado", Toast.LENGTH_SHORT).show()
                clearFields()
            }else{
                Toast.makeText(this, "Error al insertar", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "No puedes dejar ningún campo de texto vacio", Toast.LENGTH_SHORT).show()
        }
    }
    fun updateStudent(v:View){
        if (!etcontrol.text.isEmpty() && !etcarrera.text.isEmpty() && !etedad.text.isEmpty() && !etnombre.text.isEmpty()) {
            getValues()
            val database= adBD(this)
            val tupla=database.Ejecuta("update Estudiante set nomEst='$nom',carrera='$carrera',edadEst=$edad     WHERE noControl='$control'")
            if(tupla==1){
                Toast.makeText(this, "Registro actualizado", Toast.LENGTH_SHORT).show()
                clearFields()
            }else{
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
        }else{
        }
    }
    fun deleteStudent(v:View){
        if(!etcontrol.text.isEmpty()){
            control=etcontrol.text.toString()
            val database = adBD(this)
            val tupla=database.Ejecuta("DELETE from Estudiante where noControl='$control'")
            if(tupla==1){
                Toast.makeText(this, "Registro eliminado", Toast.LENGTH_SHORT).show()
                clearFields()
            }else{
                Toast.makeText(this, "Error al eliminado", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "No puedes dejar los campos vacios", Toast.LENGTH_SHORT).show();
        }
    }
    fun getValues(){
        control=etcontrol.text.toString()
        nom=etnombre.text.toString()
        carrera=etcarrera.text.toString()
        edad=etedad.text.toString()
    }
    fun clearFields(){
        etnombre.setText("")
        etnombre.setText("")
        etcarrera.setText("")
        etedad.setText("")
        etcontrol.setText("")
        etcontrol.requestFocus()
    }

    fun sendRequest(jsonObject: JSONObject){
        val url = Utils.SERVER_IP+Utils.SITE_NAME+"insertStudent.php"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            Response.Listener { response ->
                Toast.makeText(this, "API: Server response:${response["message"]}", Toast.LENGTH_SHORT).show();
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "${error.message}", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "API: Error al insertar registro ):", Toast.LENGTH_SHORT).show();
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}
