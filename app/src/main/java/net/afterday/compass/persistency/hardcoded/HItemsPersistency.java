package net.afterday.compass.persistency.hardcoded;

import android.util.Log;

import net.afterday.compass.R;
import net.afterday.compass.core.inventory.items.Item;
import net.afterday.compass.persistency.items.ItemDescriptor;
import net.afterday.compass.persistency.items.ItemDescriptorImpl;
import net.afterday.compass.persistency.items.ItemsPersistency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Justas Spakauskas on 2/3/2018.
 */

public class HItemsPersistency implements ItemsPersistency
{
    private static final String TAG = "HItemsPersistency";
    private Map<String, ItemDescriptor> possibleItems = new HashMap<>();
    private Map<Integer, List<ItemDescriptor>> itemsByLevel = new HashMap<>();
    public HItemsPersistency()
    {
        setupItems();
    }

    @Override
    public Map<String, ItemDescriptor> getItemsByCode()
    {
        return possibleItems;
    }

    @Override
    public ItemDescriptor getItemForCode(String code)
    {
        if(possibleItems.containsKey(code))
        {
            return possibleItems.get(code);
        }
        return null;
    }

    @Override
    public Map<Integer, List<ItemDescriptor>> getItemsAddeWithLevel()
    {
        return itemsByLevel;
    }

    private void setupItems()

            // ITEMS with effects
    {
        Log.w(TAG, "setupItems");
        possibleItems.put ("Bandage", (new ItemDescriptorImpl(R.string.item_bandage))
                //.setTitle(R.string.item_bandage)
                .setImage(R.drawable.item_bandage)
                .setArtefact(false)
                .addModifier(Item.HEALTH_INSTANT, 10d)
                .setDescription(R.string.desc_bandage)
                //.setConsumable(false)
                //.setDescription("Adds 10% of health")

        );
        possibleItems.put("Medkit", (new ItemDescriptorImpl(R.string.item_medkit))
                .setImage(R.drawable.item_medkit)
                .setArtefact(false)
                .addModifier(Item.HEALTH_INSTANT, 20d)
                .setDescription(R.string.desc_medkit)

        );
        possibleItems.put("ArmyMedkit", (new ItemDescriptorImpl(R.string.item_army_medkit))
                .setImage(R.drawable.item_army_medkit)
                .setArtefact(false)
                .addModifier(Item.HEALTH_INSTANT, 30d)
                .setDescription(R.string.desc_army_medkit)
        );
        possibleItems.put("Vinca", (new ItemDescriptorImpl(R.string.item_vinca))
                .setImage(R.drawable.item_vinca)
                .setArtefact(false)
                .addModifier(Item.HEALTH_INSTANT, 40d)
                .addModifier(Item.RADIATION_INSTANT, -1d)
                .setDescription(R.string.desc_vinca)
        );
        possibleItems.put("ScientificMedkit", (new ItemDescriptorImpl(R.string.item_scientific_medkit))
                .setImage(R.drawable.item_scientific_medkit)
                .setArtefact(false)
                .addModifier(Item.HEALTH_INSTANT, 50d)
                .addModifier(Item.RADIATION_INSTANT, -3d)
                .setDescription(R.string.desc_scientific_medkit)
        );
        possibleItems.put("Vodka", (new ItemDescriptorImpl(R.string.item_vodka))
                .setImage(R.drawable.item_vodka)
                .setArtefact(false)
                .addModifier(Item.HEALTH_INSTANT, -10d)
                .addModifier(Item.RADIATION_INSTANT, -1d)
                .setDescription(R.string.desc_vodka)
        );
        possibleItems.put("AntiRad", (new ItemDescriptorImpl(R.string.item_antirad))
                .setImage(R.drawable.item_antirad)
                .setArtefact(false)
                .addModifier(Item.HEALTH_INSTANT, -20d)
                .addModifier(Item.RADIATION_INSTANT, -7d)
                .setDescription(R.string.desc_antirad)
        );
        possibleItems.put("Anabiotic", (new ItemDescriptorImpl(R.string.item_anabiotic))
                .setImage(R.drawable.item_anabiotic)
                .setArtefact(false)
                //.addModifier(Item.) //nuima emissijos kolba, veikia tik emisijos metu
                .addModifier(Item.HEALTH_INSTANT, -30d)
                .setDescription(R.string.desc_anabiotic)
        );

            // BOOSTERS
        possibleItems.put("B190Radioprotectant", (new ItemDescriptorImpl(R.string.item_b190))
                .setImage(R.drawable.item_b190)
                .setArtefact(false)
                .addModifier(Item.RADIATION_MODIFIER, 0d)
                .setBooster(true)
                .setDuration(600L * 1000L)
                .setDescription(R.string.desc_b190)
        );
        possibleItems.put("PsyBlock", (new ItemDescriptorImpl(R.string.item_psyblock))
                .setImage(R.drawable.item_psy_block)
                .setArtefact(false)
                .addModifier(Item.MENTAL_MODIFIER, 0d)
                .setBooster(true)
                .setDuration(60L * 1000L)
                .setDescription(R.string.desc_psyblock)
        );
        possibleItems.put("IP2Antidote", (new ItemDescriptorImpl(R.string.item_ip2))
                .setImage(R.drawable.item_ip2_antidote)
                .setArtefact(false)
                .addModifier(Item.ANOMALY_MODIFIER, 0d)
                .setBooster(true)
                .setDuration(60L * 1000L)
                .setDescription(R.string.desc_ip2)
        );
        possibleItems.put("Hercules", (new ItemDescriptorImpl(R.string.item_hercules))
                .setImage(R.drawable.item_hercules)
                .setArtefact(false)
                .setBooster(true)
                .setDuration(30L * 60L * 1000L)
                .addModifier(Item.HEALTH_MODIFIER, 1.5d)
                .setDescription(R.string.desc_hercules)
        );
        possibleItems.put("EnergyDrink", (new ItemDescriptorImpl(R.string.item_energy_drink))
                .setImage(R.drawable.item_energy_drink)
                .setArtefact(false)
                .setBooster(true)
                .setDuration(30L * 60L * 1000L)
                .addModifier(Item.HEALTH_MODIFIER, 2d)
                .setDescription(R.string.desc_energy_drink)
        );
        possibleItems.put("GreenSvobodaSerum", (new ItemDescriptorImpl(R.string.item_green_svoboda))
                .setImage(R.drawable.item_green_svoboda_serum)
                .setArtefact(false)
                .setBooster(true)
                .setDuration(120L * 60L * 1000L)
                .addModifier(Item.RADIATION_MODIFIER, 0.1d)
                .setDescription(R.string.desc_green_svoboda)
        );
        possibleItems.put("YellowSvobodaSerum", (new ItemDescriptorImpl(R.string.item_yellow_svoboda))
                .setImage(R.drawable.item_yellow_svoboda_serum)
                .setArtefact(false)
                .setBooster(true)
                .setDuration(120L * 60L * 1000L)
                .addModifier(Item.MENTAL_MODIFIER, 0.1d)
                .setDescription(R.string.desc_yellow_svoboda)
        );

            //SUITS Wornable
        possibleItems.put("LeatherJacket", (new ItemDescriptorImpl(R.string.item_leather_jacket))
                .setImage(R.drawable.item_leather_jacket)
                .setArtefact(false)
                .setArmor(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.9d)
                .setDuration(60L * 60L * 1000L)
                .setDescription(R.string.desc_leather_jacket)
        );
        possibleItems.put("MercSuit", (new ItemDescriptorImpl(R.string.item_merc_suit))
                .setImage(R.drawable.item_merc_suit)
                .setArtefact(false)
                .setArmor(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.8d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.9d)
                .setDuration(60L * 60L * 1000L)
                .setDescription(R.string.desc_merc_suit)
        );
        possibleItems.put("SSP99Ecologist", (new ItemDescriptorImpl(R.string.item_ssp99))
                .setImage(R.drawable.item_ssp_99_ecologist)
                .setArtefact(false)
                .setArmor(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.2d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.4d)
                .addModifier(Item.MENTAL_MODIFIER, 0.6d)
                .setDuration(60L * 60L * 1000L)
                .setDescription(R.string.desc_ssp99)
        );

            // SUITS Longwornable
        possibleItems.put("StalkerSuit", (new ItemDescriptorImpl(R.string.item_stalker_suit))
                .setImage(R.drawable.item_stalker_suit)
                .setArtefact(false)
                .setArmor(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.7d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.8d)
                .addModifier(Item.MENTAL_MODIFIER, 0.9d)
                //.setDuration(-1)
                .setDuration(120L * 60L * 1000L)
                .setDescription(R.string.desc_stalker_suit)
        );
        possibleItems.put("SEVASuit", (new ItemDescriptorImpl(R.string.item_seva_suit))
                .setImage(R.drawable.item_seva_suit)
                .setArtefact(false)
                .setArmor(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.5d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.6d)
                .addModifier(Item.MENTAL_MODIFIER, 0.7d)
                //.addModifier(Item.HEALTH_MODIFIER, 2d)
                //.setDuration(-1)
                .setDuration(120L * 60L * 1000L)
                .setDescription(R.string.desc_seva_suit)
        );
        possibleItems.put("Exoskeleton", (new ItemDescriptorImpl(R.string.item_exoskeleton))
                .setImage(R.drawable.item_exoskeleton)
                .setArtefact(false)
                .setArmor(true)
                //.setXpPoints(50)
                .addModifier(Item.RADIATION_MODIFIER, 0.7d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.6d)
                .addModifier(Item.MENTAL_MODIFIER, 0.9d)
                .addModifier(Item.HEALTH_MODIFIER, 2d)
                //.setDuration(-1)
                .setDuration(120L * 60L * 1000L)
                .setDescription(R.string.desc_exoskeleton)
        );
        // SUITS Fractions Ultralongwornable
        possibleItems.put("BerillSuit", (new ItemDescriptorImpl(R.string.item_berill_suit))
                .setImage(R.drawable.item_berill)
                .setArtefact(false)
                .setArmor(true)
                .setXpPoints(50)
                .addModifier(Item.RADIATION_MODIFIER, 0.8d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.8d)
                .addModifier(Item.MENTAL_MODIFIER, 0.8d)
                .addModifier(Item.HEALTH_MODIFIER, 3d)
                //.setDuration(-1)
                .setDuration(180L * 60L * 1000L)
                .setDescription(R.string.desc_berill_suit)
        );
        possibleItems.put("DutySuit", (new ItemDescriptorImpl(R.string.item_duty_suit))
                .setImage(R.drawable.item_duty)
                .setArtefact(false)
                .setArmor(true)
                .setXpPoints(50)
                .addModifier(Item.RADIATION_MODIFIER, 0.7d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.8d)
                .addModifier(Item.MENTAL_MODIFIER, 0.9d)
                //.setDuration(-1)
                .setDuration(180L * 60L * 1000L)
                .setDescription(R.string.desc_duty_suit)
        );
        possibleItems.put("FreedomSuit", (new ItemDescriptorImpl(R.string.item_freedom_suit))
                .setImage(R.drawable.item_freedom)
                .setArtefact(false)
                .setArmor(true)
                .setXpPoints(50)
                .addModifier(Item.RADIATION_MODIFIER, 0.7d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.8d)
                .addModifier(Item.MENTAL_MODIFIER, 0.9d)
                //.setDuration(-1)
                .setDuration(180L * 60L * 1000L)
                .setDescription(R.string.desc_freedom_suit)
        );
        possibleItems.put("ClearSkySuit", (new ItemDescriptorImpl(R.string.item_clearsky_suit))
            .setImage(R.drawable.item_clear_sky)
            .setArtefact(false)
            .setArmor(true)
            .setXpPoints(50)
            .addModifier(Item.RADIATION_MODIFIER, 0.7d)
            .addModifier(Item.ANOMALY_MODIFIER, 0.8d)
            .addModifier(Item.MENTAL_MODIFIER, 0.9d)
            //.setDuration(-1)
            .setDuration(180L * 60L * 1000L)
            .setDescription(R.string.desc_clearsky_suit)
    );
        possibleItems.put("BanditSuit", (new ItemDescriptorImpl(R.string.item_bandit_suit))
                .setImage(R.drawable.item_bandit)
                .setArtefact(false)
                .setArmor(true)
                .setXpPoints(50)
                .addModifier(Item.RADIATION_MODIFIER, 0.7d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.8d)
                .addModifier(Item.MENTAL_MODIFIER, 0.9d)
                //.setDuration(-1)
                .setDuration(180L * 60L * 1000L)
                .setDescription(R.string.desc_bandit_suit)
        );
        possibleItems.put("MonolithSuit", (new ItemDescriptorImpl(R.string.item_monolith_suit))
                .setImage(R.drawable.item_monolith)
                .setArtefact(false)
                .setArmor(true)
                .setXpPoints(50)
                .addModifier(Item.RADIATION_MODIFIER, 0.7d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.8d)
                .addModifier(Item.MENTAL_MODIFIER, 0d)
                //.setDuration(-1)
                .setDuration(180L * 60L * 1000L)
                .setDescription(R.string.desc_monolith_suit)
        );

            //ARTEFACTS RAD group
        possibleItems.put("Droplets", (new ItemDescriptorImpl(R.string.item_droplets))
                .setImage(R.drawable.item_droplets)
                .setArtefact(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.9d)
                .setXpPoints(2)
                .setDescription(R.string.desc_droplets)
        );
        possibleItems.put("Bubble", (new ItemDescriptorImpl(R.string.item_bubble))
                .setImage(R.drawable.item_bubble)
                .setArtefact(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.8d)
                .setXpPoints(5)
                .setDescription(R.string.desc_bubble)
        );
        possibleItems.put("Fireball", (new ItemDescriptorImpl(R.string.item_fireball))
                .setImage(R.drawable.item_fireball)
                .setArtefact(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.7d)
                .setXpPoints(10)
                .setDescription(R.string.desc_fireball)
        );
        possibleItems.put("Crystal", (new ItemDescriptorImpl(R.string.item_crystal))
                .setImage(R.drawable.item_crystal)
                .setArtefact(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.6d)
                .setXpPoints(10)
                .setDescription(R.string.desc_crystal)
        );
        possibleItems.put("Thorn", (new ItemDescriptorImpl(R.string.item_thorn))
                .setImage(R.drawable.item_thorn)
                .setArtefact(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.5d)
                .addModifier(Item.MENTAL_MODIFIER, 1.25d)
                .addModifier(Item.ANOMALY_MODIFIER, 1.25d)
                .setXpPoints(10)
                .setDescription(R.string.desc_thorn)
        );

            //ARTEFACTS MED group
        possibleItems.put("Jellyfish", (new ItemDescriptorImpl(R.string.item_jellyfish))
                .setImage(R.drawable.item_jellyfish)
                .setArtefact(true)
                .addModifier(Item.HEALTH_MODIFIER, 1.5d)
                .setXpPoints(1)
                .setDescription(R.string.desc_jellyfish)
        );
        possibleItems.put("Eye", (new ItemDescriptorImpl(R.string.item_eye))
                .setImage(R.drawable.item_eye)
                .setArtefact(true)
                .addModifier(Item.HEALTH_MODIFIER, 2d)
                .setXpPoints(2)
                .setDescription(R.string.desc_eye)
        );
        possibleItems.put("Kolobok", (new ItemDescriptorImpl(R.string.item_kolobok))
                .setImage(R.drawable.item_kolobok)
                .setArtefact(true)
                .addModifier(Item.HEALTH_MODIFIER, 3d)
                .setXpPoints(5)
                .setDescription(R.string.desc_kolobok)
        );
        possibleItems.put("Goldfish", (new ItemDescriptorImpl(R.string.item_goldfish))
                .setImage(R.drawable.item_goldfish)
                .setArtefact(true)
                .addModifier(Item.HEALTH_MODIFIER, 4d)
                .addModifier(Item.CONTROLLER_MODIFIER, 0.1d)
                .setXpPoints(10)
                .setDescription(R.string.desc_goldfish)
        );
        possibleItems.put("MamasBeads", (new ItemDescriptorImpl(R.string.item_mamas_beads))
                .setImage(R.drawable.item_mamas_beads)
                .setArtefact(true)
                .addModifier(Item.HEALTH_MODIFIER, 5d)
                .addModifier(Item.BURER_MODIFIER, 0.1d)
                .setXpPoints(15)
                .setDescription(R.string.desc_mamas_beads)
        );
        possibleItems.put("Mica", (new ItemDescriptorImpl(R.string.item_mica))
                .setImage(R.drawable.item_mica)
                .setArtefact(true)
                .addModifier(Item.HEALTH_MODIFIER, 6d)
                .addModifier(Item.BURER_MODIFIER, 0.1d)
                .addModifier(Item.CONTROLLER_MODIFIER, 0.1d)
                .addModifier(Item.RADIATION_MODIFIER, 1.25d)
                .addModifier(Item.MENTAL_MODIFIER, 1.25d)
                .addModifier(Item.ANOMALY_MODIFIER, 1.25d)
                .setXpPoints(20)
                .setDescription(R.string.desc_mica)
        );

            //ANOMALY Group
        possibleItems.put("Spring", (new ItemDescriptorImpl(R.string.item_spring))
                .setImage(R.drawable.item_spring)
                .setArtefact(true)
                .addModifier(Item.RADIATION_MODIFIER, 1.1d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.75d)
                .setXpPoints(5)
                .setDescription(R.string.desc_spring)
        );
        possibleItems.put("Moonlight", (new ItemDescriptorImpl(R.string.item_moonlight))
                .setImage(R.drawable.item_moonlight)
                .setArtefact(true)
                .addModifier(Item.RADIATION_MODIFIER, 1.2d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.5d)
                .setXpPoints(10)
                .setDescription(R.string.desc_moonlight)
        );

            //PSY Group
        possibleItems.put("StoneFlower", (new ItemDescriptorImpl(R.string.item_stone_flower))
                .setImage(R.drawable.item_stone_flower)
                .setArtefact(true)
                .addModifier(Item.RADIATION_MODIFIER, 1.2d)
                .addModifier(Item.MENTAL_MODIFIER, 0.8d)
                //.addModifier(Item.ANOMALY_MODIFIER, 0.01d)
                .setXpPoints(5)
                .setDescription(R.string.desc_stone_flower)
        );
        possibleItems.put("Pellicle", (new ItemDescriptorImpl(R.string.item_pellicle))
                .setImage(R.drawable.item_pellicle)
                .setArtefact(true)
                .addModifier(Item.RADIATION_MODIFIER, 1.4d)
                .addModifier(Item.MENTAL_MODIFIER, 0.6d)
                //.addModifier(Item.ANOMALY_MODIFIER, 0.01d)
                .setXpPoints(10)
                .setDescription(R.string.desc_pellicle)
        );
        possibleItems.put("NightStar", (new ItemDescriptorImpl(R.string.item_night_star))
                .setImage(R.drawable.item_night_star)
                .setArtefact(true)
                .addModifier(Item.RADIATION_MODIFIER, 1.6d)
                .addModifier(Item.MENTAL_MODIFIER, 0.4d)
                //.addModifier(Item.ANOMALY_MODIFIER, 0.01d)
                .setXpPoints(15)
                .setDescription(R.string.desc_night_star)
        );
        possibleItems.put("Compass", (new ItemDescriptorImpl(R.string.item_compass))
                .setImage(R.drawable.item_compass)
                .setArtefact(true)
                .addModifier(Item.RADIATION_MODIFIER, 2d)
                .addModifier(Item.MENTAL_MODIFIER, 0.4d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.01d)
                .setXpPoints(30)
                .setDescription(R.string.desc_compass)
        );

            //NEGATIVE RADIATION Group
        possibleItems.put("Shell", (new ItemDescriptorImpl(R.string.item_shell))
                .setImage(R.drawable.item_shell)
                .setArtefact(true)
                .addModifier(Item.RADIATION_EMMITER, 1d)
                .setDescription(R.string.desc_shell)
        );
        possibleItems.put("AlteredInsulator", (new ItemDescriptorImpl(R.string.item_altered_insulator))
                .setImage(R.drawable.item_altered_insulator)
                .setArtefact(true)
                .addModifier(Item.RADIATION_EMMITER, 7d)
                .setDescription(R.string.desc_altered_insulator)
        );
        possibleItems.put("BlackEnergy", (new ItemDescriptorImpl(R.string.item_black_energy))
                .setImage(R.drawable.item_black_energy)
                .setArtefact(true)
                .addModifier(Item.RADIATION_EMMITER, 14d)
                .setDescription(R.string.desc_black_energy)
        );

        //ITEMS without effects (Unlocks)
        possibleItems.put("Optics", (new ItemDescriptorImpl(R.string.item_optics))
                .setImage(R.drawable.item_optics)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(0)
                .setDescription(R.string.desc_optics)
        );
        possibleItems.put("Collimator", (new ItemDescriptorImpl(R.string.item_collimator))
                .setImage(R.drawable.item_collimator)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(0)
                .setDescription(R.string.desc_collimator)
        );
        possibleItems.put("NightVision", (new ItemDescriptorImpl(R.string.item_night_vision))
                .setImage(R.drawable.item_night_vision)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(0)
                .setDescription(R.string.desc_night_vision)
        );
        possibleItems.put("WalkieTalkie", (new ItemDescriptorImpl(R.string.item_walkie_talkie))
                .setImage(R.drawable.item_walkie_talkie)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(0)
                .setDescription(R.string.desc_walkie_talkie)
        );
        possibleItems.put("GrenadeLauncher", (new ItemDescriptorImpl(R.string.item_grenade_launcher))
                .setImage(R.drawable.item_grenade_launcher)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(0)
                .setDescription(R.string.desc_grenade_launcher)
        );

        //ITEMS (other) (for quests?)
        possibleItems.put("DeadRat", (new ItemDescriptorImpl(R.string.item_dead_rat))
                .setImage(R.drawable.item_rat)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(0)
                .setDescription(R.string.desc_dead_rat)
        );
        possibleItems.put("Bread", (new ItemDescriptorImpl(R.string.item_bread))
                .setImage(R.drawable.item_bread)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(1)
                .setDescription(R.string.desc_bread)
        );
        possibleItems.put("Sausage", (new ItemDescriptorImpl(R.string.item_sausage))
                .setImage(R.drawable.item_sausage)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(2)
                .setDescription(R.string.desc_sausage)
        );
        possibleItems.put("CannedMeat", (new ItemDescriptorImpl(R.string.item_canned_meat))
                .setImage(R.drawable.item_can)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(3)
                .setDescription(R.string.desc_canned_meat)
        );
        possibleItems.put("RedDevil", (new ItemDescriptorImpl(R.string.item_red_devil))
                .setImage(R.drawable.item_red_devil)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(4)
                .setDescription(R.string.desc_red_devil)
        );
        possibleItems.put("Gasoline", (new ItemDescriptorImpl(R.string.item_gasoline))
                .setImage(R.drawable.item_gasoline)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(10)
                .setDescription(R.string.desc_gasoline)
        );
        possibleItems.put("Guitar", (new ItemDescriptorImpl(R.string.item_guitar))
                .setImage(R.drawable.item_guitar)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(20)
                .setDescription(R.string.desc_guitar)
        );
        possibleItems.put("Instruments", (new ItemDescriptorImpl(R.string.item_instruments))
                .setImage(R.drawable.item_instruments)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(30)
                .setDescription(R.string.desc_instruments)
        );
        possibleItems.put("PsionicDevice", (new ItemDescriptorImpl(R.string.item_psionic_device))
                .setImage(R.drawable.item_psionic_device)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(40)
                .setDescription(R.string.desc_psionic_device)
        );
        possibleItems.put("GaussGun", (new ItemDescriptorImpl(R.string.item_gauss_gun))
                .setImage(R.drawable.item_gauss)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(50)
                .setDescription(R.string.desc_gauss_gun)
        );
        new ItemDescriptorImpl(R.string.item_gauss_gun)
                .setImage(R.drawable.item_gauss)
            .setArtefact(false)
            .setConsumable(false)
            .setXpPoints(50)
            .setDescription(R.string.desc_gauss_gun).toJson();
        setupLevels();
    }

    private void setupLevels()
    {
        setupLevel1();
        setupLevel2();
        setupLevel3();
        setupLevel4();
        setupLevel5();
    }

    private void setupLevel1()
    {
        List<ItemDescriptor> level1 = new ArrayList<>();
        level1.add(makeLevelItem("Knife", "Unlocks knife", R.drawable.item_knife));
        level1.add(makeLevelItem("Pistol", "Unlocks pistol", R.drawable.item_pistol));
        itemsByLevel.put(1, level1);
    }

    private void setupLevel2()
    {
        List<ItemDescriptor> level2 = new ArrayList<>();
        level2.add(makeLevelItem("Shotgun", "Unlocks any shotgun", R.drawable.item_shotgun));
        itemsByLevel.put(2, level2);
    }
    private void setupLevel3()
    {
        List<ItemDescriptor> level3 = new ArrayList<>();
        level3.add(makeLevelItem("Submachine gun", "Unlocks any submachine gun", R.drawable.item_smg));
        level3.add(makeLevelItem("Assault AK gun", "Unlocks any assault rifle", R.drawable.item_ak));
        itemsByLevel.put(3, level3);
    }
    private void setupLevel4()
    {
        List<ItemDescriptor> level4 = new ArrayList<>();
        level4.add(makeLevelItem("Sniper gun", "Unlocks any sniper weapon", R.drawable.item_sniper));
        itemsByLevel.put(4, level4);
    }
    private void setupLevel5()
    {
        List<ItemDescriptor> level5 = new ArrayList<>();
        level5.add(makeLevelItem("Mashine gun", "Unlocks any machinegun", R.drawable.item_machinegun));
        itemsByLevel.put(5, level5);
    }

    private ItemDescriptor makeLevelItem(String title, String description, int image)
    {
        return (new ItemDescriptorImpl(title).setDescription(description).setImage(image).setDropable(false).setConsumable(false));
    }
}
