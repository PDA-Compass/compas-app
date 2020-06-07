package net.afterday.compas.app.pda.geiger

import android.content.Context
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import net.afterday.compas.engine.core.EventBus
import net.afterday.compas.engine.engine.system.damage.DamageEvent
import net.afterday.compas.engine.engine.system.damage.DamageType

open class GeigerService(val view: Geiger, val context: Context) {
    private val sound = GeigerSound(context)

    init {
        EventBus.damage()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { d: DamageEvent ->
                    when (d.type) {
                        DamageType.RADIATION -> {
                            if (d.value > 0) {
                                view.toSvh(d.value, 10000)
                                sound.playRad(d.value.toDouble())
                            }
                        }
                        DamageType.MENTAL -> {
                                view.mental(d.value)
                        }
                        DamageType.PHISICAL,
                            DamageType.FIRE,
                            DamageType.ELECTRIC -> {
                                view.anomaly(d.value)
                        }
                        DamageType.MONOLITH -> {
                                view.monolith(d.value)
                        }

                    }
                }
    }
}