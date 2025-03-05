package monge.eliana.myfeelings

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import monge.eliana.myfeelings.utilities.CustomBarDrawable
import monge.eliana.myfeelings.utilities.CustomCircleDrawable
import monge.eliana.myfeelings.utilities.Emociones
import monge.eliana.myfeelings.utilities.JSONFile
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    var jsonFile: JSONFile? = null
    var veryHappy = 0.0f
    var happy = 0.0f
    var neutral = 0.0f
    var sad = 0.0f
    var verysad = 0.0f
    var data: Boolean = false
    var lista = ArrayList<Emociones>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val graph = findViewById<View>(R.id.graph)
        val graphVeryHappy = findViewById<View>(R.id.graphVeryHappy)
        val graphHappy = findViewById<View>(R.id.graphHappy)
        val graphNeutral = findViewById<View>(R.id.graphNeutral)
        val graphSad = findViewById<View>(R.id.graphSad)
        val graphVerySad = findViewById<View>(R.id.graphVerySad)
        val guardarBoton = findViewById<View>(R.id.guardarButton)
        val veryHappyButton = findViewById<View>(R.id.veryHappyButton)
        val happyButton = findViewById<View>(R.id.happyButton)
        val neutralButton = findViewById<View>(R.id.neutralButton)
        val sadButton = findViewById<View>(R.id.sadButton)
        val verySadButton = findViewById<View>(R.id.verySadButton)

        guardarBoton.setOnClickListener{
            guardar()
        }

        veryHappyButton.setOnClickListener{
            veryHappy++
            iconoMayoria()
            actualizarGrafica()
        }
        happyButton.setOnClickListener{
            happy++
            iconoMayoria()
            actualizarGrafica()
        }
        neutralButton.setOnClickListener{
            neutral++
            iconoMayoria()
            actualizarGrafica()
        }
        sadButton.setOnClickListener{
            sad++
            iconoMayoria()
            actualizarGrafica()
        }
        verySadButton.setOnClickListener{
            verysad++
            iconoMayoria()
            actualizarGrafica()
        }


        jsonFile = JSONFile()

        fetchingData()
        if(!data){
            var emociones = ArrayList<Emociones>()
            val fondo = CustomCircleDrawable(this, emociones)
            graph.background = fondo
            graphVeryHappy.background = CustomBarDrawable(this, Emociones("Muy feliz", 0.0f, R.color.mustard, veryHappy))
            graphHappy.background = CustomBarDrawable(this,Emociones("Feliz", 0.0f, R.color.orange, happy))
            graphNeutral.background = CustomBarDrawable(this, Emociones("Neutral", 0.0f, R.color.greenie, neutral))
            graphSad.background = CustomBarDrawable(this, Emociones("Triste", 0.0f, R.color.blue, sad))
            graphVerySad.background = CustomBarDrawable(this, Emociones("Muy feliz", 0.0f, R.color.deepBlue, verysad))
        }else{
            actualizarGrafica()
            iconoMayoria()
        }

        }
    fun fetchingData(){
        try{
            var json : String = jsonFile?.getData(this) ?: ""
            if(json != ""){
                this.data = true
                var jsonArray : JSONArray = JSONArray(json)

                this.lista = parseJson(jsonArray)

                for(i in lista){
                    when(i.nombre){
                        "Muy feliz" -> veryHappy = i.total
                        "Feliz" -> happy = i.total
                        "Neutral" -> neutral = i.total
                        "Triste" -> sad = i.total
                        "Muy triste" -> verysad = i.total
                    }
                }
            }else{
                this.data = false
            }
        }catch(exception: JSONException){
            exception.printStackTrace()
        }

    }

    fun iconoMayoria() {
        val icon = findViewById<ImageView>(R.id.icon)

        val context = icon.context

        val drawableRes = when {
            happy > veryHappy && happy > neutral && happy > sad && happy > verysad -> R.drawable.ic_satisfied
            veryHappy > happy && veryHappy > neutral && veryHappy > sad && veryHappy > verysad -> R.drawable.ic_verysatisfied
            neutral > veryHappy && neutral > happy && neutral > sad && neutral > verysad -> R.drawable.ic_neutral
            sad > happy && sad > neutral && sad > veryHappy && sad > verysad -> R.drawable.ic_dissatisfied
            verysad > happy && verysad > neutral && verysad > sad && verysad > veryHappy -> R.drawable.ic_verydissatisfied
            else -> null
        }

        drawableRes?.let {
            icon.setImageDrawable(ContextCompat.getDrawable(context, it)) // Uso correcto
        }
    }



    fun actualizarGrafica(){

        val total = veryHappy+happy+neutral+verysad+sad

        var pVH: Float = (veryHappy * 100/ total).toFloat()
        var pH: Float = (happy * 100/ total).toFloat()
        var pN: Float = (neutral * 100/ total).toFloat()
        var pS: Float = (sad * 100/ total).toFloat()
        var pVS: Float = (verysad * 100/ total).toFloat()

        Log.d("porcentajes", "very happy"+pVH)
        Log.d("porcentajes", "happy"+pH)
        Log.d("porcentajes", "neutral"+pN)
        Log.d("porcentajes", "sad"+pS)
        Log.d("porcentajes", "very sad"+pVS)

        lista.clear()
        lista.add(Emociones("Muy feliz", pVH, R.color.mustard, veryHappy))
        lista.add(Emociones("Feliz", pVH, R.color.orange, happy))
        lista.add(Emociones("Neutral", pVH, R.color.greenie, neutral))
        lista.add(Emociones("Triste", pVH, R.color.blue, sad))
        lista.add(Emociones("Muy feliz", pVH, R.color.deepBlue, verysad))

        val fondo = CustomCircleDrawable(this, lista)

        val graph = findViewById<View>(R.id.graph)
        val graphVeryHappy = findViewById<View>(R.id.graphVeryHappy)
        val graphHappy = findViewById<View>(R.id.graphHappy)
        val graphNeutral = findViewById<View>(R.id.graphNeutral)
        val graphSad = findViewById<View>(R.id.graphSad)
        val graphVerySad = findViewById<View>(R.id.graphVerySad)

        graphVeryHappy.background = CustomBarDrawable(this, Emociones("Muy feliz", pVH, R.color.mustard, veryHappy))
        graphHappy.background = CustomBarDrawable(this,Emociones("Feliz", pVH, R.color.orange, happy))
        graphNeutral.background = CustomBarDrawable(this, Emociones("Neutral", pVH, R.color.greenie, neutral))
        graphSad.background = CustomBarDrawable(this, Emociones("Triste", pVH, R.color.blue, sad))
        graphVerySad.background = CustomBarDrawable(this, Emociones("Muy feliz", pVH, R.color.deepBlue, verysad))

        graph.background = fondo

    }

    fun parseJson(jsonArray: JSONArray): ArrayList<Emociones>{

        var lista = ArrayList<Emociones>()

        for (i in 0..jsonArray.length()){
            try{
                val nombre = jsonArray.getJSONObject(i).getString("nombre")
                val porcentaje = jsonArray.getJSONObject(i).getDouble("porcentaje").toFloat()
                val color = jsonArray.getJSONObject(i).getInt("color")
                val total = jsonArray.getJSONObject(i).getDouble("total").toFloat()
                var emocion = Emociones(nombre, porcentaje, color, total)
                lista.add(emocion)
            }catch(exception: JSONException){
                exception.printStackTrace()
            }
        }

        return lista
    }

    fun guardar(){

        var jsonArray = JSONArray()
        var o : Int = 0
        for(i in lista){
            Log.d("objetos", i.toString())
            var j:  JSONObject  = JSONObject()
            j.put("nombre", i.nombre)
            j.put("porcentaje", i.porcentaje)
            j.put("color", i.color)
            j.put("total", i.total)

            jsonArray.put(o, j)
            o++
        }

        jsonFile?.saveData(this, jsonArray.toString())

        Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show()

    }
}