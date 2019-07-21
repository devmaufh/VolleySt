package com.example.sqlite.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.example.sqlite.API.VolleySingleton
import com.example.sqlite.Adapters.StudentListAdapter
import com.example.sqlite.Database.adBD
import com.example.sqlite.R
import com.example.sqlite.Models.Student
import com.example.sqlite.Utils.Utils

class ListaActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)


        val rview=findViewById<RecyclerView>(R.id.lista_rv)
        val adapter= StudentListAdapter(this)

        rview.adapter=adapter
        //rview.layoutManager=GridLayoutManager(this,2) //Muestra los elementos en grillas,
                                                    // si quieren que les muestre grillas de 2,3 o 4 o N numeros
                                                    //Solo cambién el 2 que tiene ahí
        rview.layoutManager=LinearLayoutManager(this) //Muestra los elementos en lista de 1, es alternativo al de arriba,
                                                            //Solo pueden usar uno, por eso comenté el otro v:


        getStudents(adapter)


//        adapter.setStudentList(DbStudentList())
    }

    fun DbStudentList():List<Student>{
        var students= ArrayList<Student>()
        val database= adBD(this)
        val cursor=database.consulta("SELECT noControl,nomEst,carrera,edadEst FROM Estudiante")
        if(cursor!!.moveToFirst())do{
            val stu= Student(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3)
            )
            students.add(stu)
        }
        while(cursor!!.moveToNext())
        cursor.close()
        return students
    }

    fun getStudents(adapter:StudentListAdapter){
        var studentsList= ArrayList<Student>()

        val url = Utils.SERVER_IP+ Utils.SITE_NAME+"getStudents.php"
        val jsonArrayRequest=JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener {response->
                for (i in 0 until response.length()){
                    val studentJson=response.getJSONObject(i)
                    val student=Student(
                        studentJson["nocontrol"].toString(),
                        studentJson["nomest"].toString(),
                        studentJson["carrera"].toString(),
                        studentJson["edad"].toString()
                        )
                    studentsList.add(student)
                    adapter.setStudentList(studentsList)
                }
            },
            Response.ErrorListener { error->

            })
        VolleySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest)
    }
}
