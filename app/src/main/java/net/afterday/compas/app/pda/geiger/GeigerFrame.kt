package net.afterday.compas.app.pda.geiger

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import net.afterday.compas.app.settings.Constants
import net.afterday.compas.app.settings.Settings


open class GeigerFrame : FrameLayout {
    private val geigerService: GeigerService
    private val compass: Compass
    private val geiger: Geiger
    private val tube: Tube

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        compass = Compass(context, attrs, defStyleAttr)
        compass.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
        compass.compassOn()
        this.addView(compass)

        geiger = Geiger(context, attrs, defStyleAttr)
        geiger.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
        this.addView(geiger)

        tube = Tube(context, attrs, defStyleAttr)
        tube.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
        this.addView(tube)

        if(Settings.instance().getBoolSetting(Constants.COMPASS))
        {
            compass.compassOn();
        }else
        {
            compass.compassOff();
        }

        geigerService = GeigerService(geiger, tube, context)
    }
}
