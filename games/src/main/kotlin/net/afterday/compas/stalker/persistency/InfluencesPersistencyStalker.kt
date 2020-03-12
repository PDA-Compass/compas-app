package net.afterday.compas.stalker.persistency

import net.afterday.compas.engine.core.influences.Emission
import net.afterday.compas.engine.core.influences.Influence
import net.afterday.compas.engine.persistency.influences.InfluencesPersistency
import net.afterday.compas.stalker.setup.SetupEmissionsStalker

class InfluencesPersistencyStalker : InfluencesPersistency {
    override fun getPossibleInfluences(): List<Influence> {
        TODO("Not yet implemented")
    }

    override fun getEmissions(): List<Emission> {
        return SetupEmissionsStalker().emissions
    }

    // Not use
    override fun getRegisteredWifiModules(): List<String> {
        TODO("Not yet implemented")
    }
}