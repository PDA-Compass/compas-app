package net.afterday.compas.app.pda.geiger

import android.content.Context
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import net.afterday.compas.engine.core.EventBus
import net.afterday.compas.engine.core.player.Player
import net.afterday.compas.engine.engine.system.damage.DamageEvent
import net.afterday.compas.engine.engine.system.damage.DamageType

open class GeigerService(val geiger: Geiger, val tube: Tube, val context: Context) {
    class DamagePair(var type: Byte, var value:Float);
    private val sound = GeigerSound(context)
    private val _list = LinkedHashMap<String, DamagePair>();
    private fun recal(){
        val _acum = FloatArray(10)
        for (damage in _list) {
            var type = damage.value.type.toInt();

            if (_acum[type] == null)
            {
                _acum[type] = damage.value.value
            }
            else {
                if (_acum[type]!! < damage.value.value) {
                    _acum[type] = damage.value.value
                }
            }
        }
        var rad = _acum[DamageType.RADIATION.toInt()];

        geiger.toSvh(rad, 5000)
        sound.playRad(rad.toDouble())
        geiger.mental(_acum[DamageType.MENTAL.toInt()])

        //TODO: cal max anomaly
        tube.setParameters(
                _acum[DamageType.RADIATION.toInt()].toDouble(),
                0.0,
                _acum[DamageType.MENTAL.toInt()].toDouble(),
                0.0,
                0.0,
                0.0,
                0.0,
                Player.STATE.ALIVE);

    }
    init {
        tube.setState(Player.STATE.ALIVE)
        EventBus.damage()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { d: DamageEvent ->
                    if (_list[d.id] != null){
                        if (d.value > 0){
                            val value = _list[d.id]
                            value?.value = d.value
                        }
                        else
                        {
                            _list.remove(d.id)
                        }
                    }
                    else
                    {
                        if (d.value > 0){
                            _list[d.id] = DamagePair(d.type, d.value)
                        }
                    }
                    recal()

                    /*when (d.type) {
                        DamageType.RADIATION -> {
                            if (d.value > 0) {
                                geiger.toSvh(d.value, 10000)
                                tube.setParameters(d.value.toDouble(), 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Player.STATE.ALIVE);
                                sound.playRad(d.value.toDouble())
                            }
                        }
                        DamageType.MENTAL -> {
                            geiger.mental(d.value)
                            sound.playRad(d.value.toDouble())
                            tube.setParameters(0.0, 0.0, d.value.toDouble(), 0.0, 0.0, 0.0, 0.0, Player.STATE.ALIVE);
                        }
                        DamageType.PHISICAL,
                            DamageType.FIRE,
                            DamageType.ELECTRIC -> {
                                geiger.anomaly(d.value)
                        }
                        DamageType.MONOLITH -> {
                            geiger.monolith(d.value)
                            tube.setParameters(0.0, 0.0, d.value.toDouble(), 0.0, 0.0, 0.0, 0.0, Player.STATE.ALIVE);
                        }
                    }*/
                }
    }
}