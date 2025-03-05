package monge.eliana.myfeelings.utilities

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import monge.eliana.myfeelings.R

class CustomBarDrawable: Drawable {

    var coordenadas: RectF? = null
    var context: Context? = null
    var emocion : Emociones? = null

    constructor(context: Context, emocion : Emociones){
        this.context = context
        this.emocion = emocion
    }

    override fun draw(canvas: Canvas) {
        val fondo = Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            color = context?.getColor(R.color.gray) ?: R.color.gray
        }

        val ancho: Float = (canvas.width - 10).toFloat()
        val alto: Float = (canvas.height - 10).toFloat()

        val coordenadas = RectF(0.0F, 0.0F, ancho, alto)
        canvas.drawRect(coordenadas, fondo)

        if (emocion != null) { // No es necesario 'this.'
            val porcentaje: Float = emocion!!.porcentaje * (canvas.width - 10) / 100
            val coordenadas2 = RectF(0.0F, 0.0F, porcentaje, alto)

            val seccion = Paint().apply {
                style = Paint.Style.FILL
                isAntiAlias = true
                color = ContextCompat.getColor(context!!, emocion!!.color) // Acceder al contexto correctamente
            }

            canvas.drawRect(coordenadas2, seccion)
        }
    }





    override fun setAlpha(alpha: Int) {
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }
}