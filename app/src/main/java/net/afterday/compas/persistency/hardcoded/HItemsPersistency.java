package net.afterday.compas.persistency.hardcoded;

import android.util.Log;

import net.afterday.compas.R;
import net.afterday.compas.core.inventory.items.Item;
import net.afterday.compas.persistency.items.ItemDescriptor;
import net.afterday.compas.persistency.items.ItemDescriptorImpl;
import net.afterday.compas.persistency.items.ItemsPersistency;
import net.afterday.compas.core.inventory.items.Item.CATEGORY.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.afterday.compas.core.inventory.items.Item.CATEGORY.ANTIRADS;
import static net.afterday.compas.core.inventory.items.Item.CATEGORY.ARMORS;
import static net.afterday.compas.core.inventory.items.Item.CATEGORY.ARTIFACTS;
import static net.afterday.compas.core.inventory.items.Item.CATEGORY.BOOSTERS;
import static net.afterday.compas.core.inventory.items.Item.CATEGORY.HABAR;
import static net.afterday.compas.core.inventory.items.Item.CATEGORY.MEDKITS;
import static net.afterday.compas.core.inventory.items.Item.CATEGORY.UPGRADES;
import static net.afterday.compas.core.inventory.items.Item.CATEGORY.WEAPONS;
import static net.afterday.compas.core.inventory.items.Item.CATEGORY.FOOD;
import static net.afterday.compas.core.inventory.items.Item.CATEGORY.AMMO;
import static net.afterday.compas.core.inventory.items.Item.CATEGORY.FILTERS;
import static net.afterday.compas.core.inventory.items.Item.CATEGORY.DEVICES;

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

    // INVENTORY (Инвентарь)
    {
        Log.w(TAG, "setupItems");

//MEDKITS (Аптечки)
        possibleItems.put ("Bandage587", (new ItemDescriptorImpl(MEDKITS, R.string.item_bandage)) //Бинт
                .setImage(R.drawable.item_bandage)
                .setArtefact(false)
                .addModifier(Item.HEALTH_INSTANT, 10d)
                .setDescription(R.string.desc_bandage)
        );
        possibleItems.put("Medkit159", (new ItemDescriptorImpl(MEDKITS, R.string.item_medkit)) //Аптечка
                .setImage(R.drawable.item_medkit)
                .setArtefact(false)
                .addModifier(Item.HEALTH_INSTANT, 20d)
                .setDescription(R.string.desc_medkit)
        );
        possibleItems.put("ArmyMedkit589", (new ItemDescriptorImpl(MEDKITS, R.string.item_army_medkit)) //Армейская аптечка
                .setImage(R.drawable.item_army_medkit)
                .setArtefact(false)
                .addModifier(Item.HEALTH_INSTANT, 30d)
                .setDescription(R.string.desc_army_medkit)
        );
        possibleItems.put("Vinca569", (new ItemDescriptorImpl(MEDKITS, R.string.item_vinca)) //Таблетки Барвинок
                .setImage(R.drawable.item_vinca)
                .setArtefact(false)
                .addModifier(Item.HEALTH_INSTANT, 40d)
                .addModifier(Item.RADIATION_INSTANT, -1d)
                .setDescription(R.string.desc_vinca)
        );
        possibleItems.put("ScientificMedkit356", (new ItemDescriptorImpl(MEDKITS, R.string.item_scientific_medkit)) //Научная аптечка
                .setImage(R.drawable.item_scientific_medkit)
                .setArtefact(false)
                .addModifier(Item.HEALTH_INSTANT, 50d)
                .addModifier(Item.RADIATION_INSTANT, -3d)
                .setDescription(R.string.desc_scientific_medkit)
        );
        possibleItems.put("Vodka145", (new ItemDescriptorImpl(ANTIRADS, R.string.item_vodka)) //Водка Казаки
                .setImage(R.drawable.item_vodka)
                .setArtefact(false)
                .addModifier(Item.HEALTH_INSTANT, -10d)
                .addModifier(Item.RADIATION_INSTANT, -1d)
                .setDescription(R.string.desc_vodka)
        );
        possibleItems.put("AntiRad853", (new ItemDescriptorImpl(ANTIRADS, R.string.item_antirad)) //Антирад
                .setImage(R.drawable.item_antirad)
                .setArtefact(false)
                .addModifier(Item.HEALTH_INSTANT, -20d)
                .addModifier(Item.RADIATION_INSTANT, -7d)
                .setDescription(R.string.desc_antirad)
        );
        possibleItems.put("Anabiotic759", (new ItemDescriptorImpl(ANTIRADS, R.string.item_anabiotic)) //Анабиотик
                .setImage(R.drawable.item_anabiotic)
                .setTitle("Anabiotic")
                .setArtefact(false)
                .addModifier(Item.HEALTH_INSTANT, -30d)
                .setDescription(R.string.desc_anabiotic)
        );

//BOOSTERS (Бустеры)
        possibleItems.put("B190Radioprotectant263", (new ItemDescriptorImpl(BOOSTERS, R.string.item_b190)) //Радиопротектор Б190
                .setImage(R.drawable.item_b190)
                .setArtefact(false)
                .addModifier(Item.RADIATION_MODIFIER, 0d)
                .setBooster(true)
                .setDuration(60L * 1000L)
                .setDescription(R.string.desc_b190)
        );
        possibleItems.put("PsyBlock126", (new ItemDescriptorImpl(BOOSTERS, R.string.item_psyblock)) //Пси-блокада
                .setImage(R.drawable.item_psy_block)
                .setArtefact(false)
                .addModifier(Item.MENTAL_MODIFIER, 0d)
                .setBooster(true)
                .setDuration(60L * 1000L)
                .setDescription(R.string.desc_psyblock)
        );
        possibleItems.put("IP2Antidote758", (new ItemDescriptorImpl(BOOSTERS, R.string.item_ip2)) //Антидот ИП2
                .setImage(R.drawable.item_ip2_antidote)
                .setArtefact(false)
                .addModifier(Item.ANOMALY_MODIFIER, 0d)
                .setBooster(true)
                .setDuration(60L * 1000L)
                .setDescription(R.string.desc_ip2)
        );
        possibleItems.put("GreenSvobodaSerum785", (new ItemDescriptorImpl(BOOSTERS, R.string.item_green_svoboda)) //Настойка Радвинок
                .setImage(R.drawable.item_green_svoboda_serum)
                .setArtefact(false)
                .setBooster(true)
                .setDuration(120L * 60L * 1000L)
                .addModifier(Item.RADIATION_MODIFIER, 0.1d)
                .setDescription(R.string.desc_green_svoboda)
        );
        possibleItems.put("YellowSvobodaSerum963", (new ItemDescriptorImpl(BOOSTERS, R.string.item_yellow_svoboda)) //Настойка Менвинок
                .setImage(R.drawable.item_yellow_svoboda_serum)
                .setArtefact(false)
                .setBooster(true)
                .setDuration(120L * 60L * 1000L)
                .addModifier(Item.MENTAL_MODIFIER, 0.1d)
                .setDescription(R.string.desc_yellow_svoboda)
        );

//FILTERS (Фильтры)
        possibleItems.put("Respirator859", (new ItemDescriptorImpl(FILTERS, R.string.item_respirator)) //Респиратор
                .setImage(R.drawable.item_respirator)
                .setArtefact(false)
                .setDevice(true)
                .setDuration(30L * 60L * 1000L)
                .addModifier(Item.RADIATION_MODIFIER, 0.6d)
                .setDescription(R.string.desc_respirator)
        );
        possibleItems.put("Gasmask859", (new ItemDescriptorImpl(FILTERS, R.string.item_gasmask)) //Противогаз
                .setImage(R.drawable.item_gasmask)
                .setArtefact(false)
                .setDevice(true)
                .setDuration(60L * 60L * 1000L)
                .addModifier(Item.RADIATION_MODIFIER, 0.4d)
                .setDescription(R.string.desc_gasmask)
        );
        possibleItems.put("Rebreather284", (new ItemDescriptorImpl(FILTERS, R.string.item_szd)) //Система замкнутого дыхания
                .setImage(R.drawable.item_szd)
                .setArtefact(false)
                .setDevice(true)
                .setDuration(120L * 60L * 1000L)
                .addModifier(Item.RADIATION_MODIFIER, 0.1d)
                .setDescription(R.string.desc_szd)
        );

//DEVICES (Приборы)
        possibleItems.put("PsionicHelmetMark169", (new ItemDescriptorImpl(DEVICES, R.string.item_shlem1)) //Пси-шлем М1
                .setImage(R.drawable.item_shlem1)
                .setArtefact(false)
                .setDevice(true)
                .setDuration(30L * 60L * 1000L)
                .addModifier(Item.MENTAL_MODIFIER, 0.01d)
                .addModifier(Item.CONTROLLER_MODIFIER, 0.7d)
                .setDescription(R.string.desc_shlem1)
        );
        possibleItems.put("PsionicHelmetMark285", (new ItemDescriptorImpl(DEVICES, R.string.item_shlem2)) //Пси-шлем М2
                .setImage(R.drawable.item_shlem2)
                .setArtefact(false)
                .setDevice(true)
                .setDuration(60L * 60L * 1000L)
                .addModifier(Item.MENTAL_MODIFIER, 0.0d)
                .addModifier(Item.MONOLITH_MODIFIER, 0.01d)
                .addModifier(Item.CONTROLLER_MODIFIER, 0.4d)
                .setDescription(R.string.desc_shlem2)
        );

//SUITS (Комбезы)
//SIMPLE (Простые)
        possibleItems.put("LeatherJacket741", (new ItemDescriptorImpl(ARMORS, R.string.item_leather_jacket)) //Кожаная куртка
                .setImage(R.drawable.item_leather_jacket)
                .setArtefact(false)
                .setArmor(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.9d)
                .setDuration(60L * 60L * 1000L)
                .setDescription(R.string.desc_leather_jacket)
        );
        possibleItems.put("SSP99Ecologist951", (new ItemDescriptorImpl(ARMORS, R.string.item_ssp99)) //Комбинезон Эколог
                .setImage(R.drawable.item_ssp_99_ecologist)
                .setArtefact(false)
                .setArmor(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.2d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.4d)
                .addModifier(Item.MENTAL_MODIFIER, 0.6d)
                .setDuration(60L * 60L * 1000L)
                .setDescription(R.string.desc_ssp99)
        );

//ADVANCED (Улучшенные)
        possibleItems.put("StalkerSuit487", (new ItemDescriptorImpl(ARMORS, R.string.item_stalker_suit)) //Комбез сталкера Заря
                .setImage(R.drawable.item_stalker_suit)
                .setArtefact(false)
                .setArmor(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.7d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.8d)
                .addModifier(Item.MENTAL_MODIFIER, 0.9d)
                .setDuration(120L * 60L * 1000L)
                .setDescription(R.string.desc_stalker_suit)
        );
        possibleItems.put("SEVASuit128", (new ItemDescriptorImpl(ARMORS, R.string.item_seva_suit)) //Комбинезон СЕВА
                .setImage(R.drawable.item_seva_suit)
                .setArtefact(false)
                .setArmor(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.5d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.6d)
                .addModifier(Item.MENTAL_MODIFIER, 0.7d)
                .setDuration(120L * 60L * 1000L)
                .setDescription(R.string.desc_seva_suit)
        );
        possibleItems.put("Exoskeleton748", (new ItemDescriptorImpl(ARMORS, R.string.item_exoskeleton)) //Экзоскелет
                .setImage(R.drawable.item_exoskeleton)
                .setArtefact(false)
                .setArmor(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.7d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.6d)
                .addModifier(Item.MENTAL_MODIFIER, 0.9d)
                .addModifier(Item.HEALTH_MODIFIER, 2d)
                .setDuration(120L * 60L * 1000L)
                .setDescription(R.string.desc_exoskeleton)
        );

//FRACTIONAL (Фракционные)
        possibleItems.put("BerillSuit526", (new ItemDescriptorImpl(ARMORS, R.string.item_berill_suit)) //Бронекостюм Берилл
                .setImage(R.drawable.item_berill)
                .setArtefact(false)
                .setArmor(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.8d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.8d)
                .addModifier(Item.MENTAL_MODIFIER, 0.8d)
                .addModifier(Item.HEALTH_MODIFIER, 3d)
                .setDuration(180L * 60L * 1000L)
                .setDescription(R.string.desc_berill_suit)
        );
        possibleItems.put("MercSuit856", (new ItemDescriptorImpl(ARMORS, R.string.item_merc_suit)) //Комбез наёмника
                .setImage(R.drawable.item_merc_suit)
                .setArtefact(false)
                .setArmor(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.7d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.8d)
                .addModifier(Item.MENTAL_MODIFIER, 0.9d)
                .setDuration(180L * 60L * 1000L)
                .setDescription(R.string.desc_merc_suit)
        );
        possibleItems.put("DutySuit358", (new ItemDescriptorImpl(ARMORS, R.string.item_duty_suit)) //Комбез Долга
                .setImage(R.drawable.item_duty)
                .setArtefact(false)
                .setArmor(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.7d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.8d)
                .addModifier(Item.MENTAL_MODIFIER, 0.9d)
                .setDuration(180L * 60L * 1000L)
                .setDescription(R.string.desc_duty_suit)
        );
        possibleItems.put("FreedomSuit428", (new ItemDescriptorImpl(ARMORS, R.string.item_freedom_suit)) //Комбез Свободы
                .setImage(R.drawable.item_freedom)
                .setArtefact(false)
                .setArmor(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.7d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.8d)
                .addModifier(Item.MENTAL_MODIFIER, 0.9d)
                .setDuration(180L * 60L * 1000L)
                .setDescription(R.string.desc_freedom_suit)
        );
        possibleItems.put("ClearSkySuit257", (new ItemDescriptorImpl(ARMORS, R.string.item_clearsky_suit)) //Комбез Чистого Неба
                .setImage(R.drawable.item_clear_sky)
                .setArtefact(false)
                .setArmor(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.7d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.8d)
                .addModifier(Item.MENTAL_MODIFIER, 0.9d)
                .setDuration(180L * 60L * 1000L)
                .setDescription(R.string.desc_clearsky_suit)
        );
        possibleItems.put("BanditSuit458", (new ItemDescriptorImpl(ARMORS, R.string.item_bandit_suit)) //Плащ бандита
                .setImage(R.drawable.item_bandit)
                .setArtefact(false)
                .setArmor(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.7d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.8d)
                .addModifier(Item.MENTAL_MODIFIER, 0.9d)
                .setDuration(180L * 60L * 1000L)
                .setDescription(R.string.desc_bandit_suit)
        );
        possibleItems.put("MonolithSuit369", (new ItemDescriptorImpl(ARMORS, R.string.item_monolith_suit)) //Комбез Монолита
                .setImage(R.drawable.item_monolith)
                .setArtefact(false)
                .setArmor(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.7d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.8d)
                .addModifier(Item.MENTAL_MODIFIER, 0.01d)
                .setDuration(180L * 60L * 1000L)
                .setDescription(R.string.desc_monolith_suit)
        );

//ARTEFACTS (Артефакты)
//ANTIRAD (Антирадные)
        possibleItems.put("Droplets854", (new ItemDescriptorImpl(ARTIFACTS, R.string.item_droplets)) //Капли
                .setImage(R.drawable.item_droplets)
                .setArtefact(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.9d)
                .setXpPoints(2)
                .setDescription(R.string.desc_droplets)
        );
        possibleItems.put("Bubble156", (new ItemDescriptorImpl(ARTIFACTS, R.string.item_bubble)) //Пузырь
                .setImage(R.drawable.item_bubble)
                .setArtefact(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.8d)
                .setXpPoints(5)
                .setDescription(R.string.desc_bubble)
        );
        possibleItems.put("Fireball785", (new ItemDescriptorImpl(ARTIFACTS, R.string.item_fireball)) //Огненный шар
                .setImage(R.drawable.item_fireball)
                .setArtefact(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.7d)
                .setXpPoints(10)
                .setDescription(R.string.desc_fireball)
        );
        possibleItems.put("Crystal478", (new ItemDescriptorImpl(ARTIFACTS, R.string.item_crystal)) //Кристалл
                .setImage(R.drawable.item_crystal)
                .setArtefact(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.6d)
                .setXpPoints(10)
                .setDescription(R.string.desc_crystal)
        );
        possibleItems.put("Thorn963", (new ItemDescriptorImpl(ARTIFACTS, R.string.item_thorn)) //Колючка
                .setImage(R.drawable.item_thorn)
                .setArtefact(true)
                .addModifier(Item.RADIATION_MODIFIER, 0.5d)
                .addModifier(Item.MENTAL_MODIFIER, 1.25d)
                .addModifier(Item.ANOMALY_MODIFIER, 1.25d)
                .setXpPoints(10)
                .setDescription(R.string.desc_thorn)
        );

//MEDICINE (Лечащие)
        possibleItems.put("Jellyfish254", (new ItemDescriptorImpl(ARTIFACTS, R.string.item_jellyfish)) //Медуза
                .setImage(R.drawable.item_jellyfish)
                .setArtefact(true)
                .addModifier(Item.HEALTH_MODIFIER, 1.5d)
                .setXpPoints(1)
                .setDescription(R.string.desc_jellyfish)
        );
        possibleItems.put("Eye965", (new ItemDescriptorImpl(ARTIFACTS, R.string.item_eye)) //Глаз
                .setImage(R.drawable.item_eye)
                .setArtefact(true)
                .addModifier(Item.HEALTH_MODIFIER, 2d)
                .setXpPoints(2)
                .setDescription(R.string.desc_eye)
        );
        possibleItems.put("Kolobok749", (new ItemDescriptorImpl(ARTIFACTS, R.string.item_kolobok)) //Колобок
                .setImage(R.drawable.item_kolobok)
                .setArtefact(true)
                .addModifier(Item.HEALTH_MODIFIER, 3d)
                .setXpPoints(5)
                .setDescription(R.string.desc_kolobok)
        );
        possibleItems.put("Goldfish325", (new ItemDescriptorImpl(ARTIFACTS, R.string.item_goldfish)) //Золотая рыбка
                .setImage(R.drawable.item_goldfish)
                .setArtefact(true)
                .addModifier(Item.HEALTH_MODIFIER, 4d)
                .addModifier(Item.CONTROLLER_MODIFIER, 0.1d)
                .setXpPoints(15)
                .setDescription(R.string.desc_goldfish)
        );
        possibleItems.put("MamasBeads965", (new ItemDescriptorImpl(ARTIFACTS, R.string.item_mamas_beads)) //Мамины бусы
                .setImage(R.drawable.item_mamas_beads)
                .setArtefact(true)
                .addModifier(Item.HEALTH_MODIFIER, 5d)
                .addModifier(Item.BURER_MODIFIER, 0.1d)
                .setXpPoints(15)
                .setDescription(R.string.desc_mamas_beads)
        );
        possibleItems.put("Mica876", (new ItemDescriptorImpl(ARTIFACTS, R.string.item_mica)) //Слюда
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

//ANOMALY (Противоаномальные)
        possibleItems.put("Spring635", (new ItemDescriptorImpl(ARTIFACTS, R.string.item_spring)) //Пружина
                .setImage(R.drawable.item_spring)
                .setArtefact(true)
                .addModifier(Item.RADIATION_MODIFIER, 1.1d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.75d)
                .setXpPoints(5)
                .setDescription(R.string.desc_spring)
        );
        possibleItems.put("Moonlight254", (new ItemDescriptorImpl(ARTIFACTS, R.string.item_moonlight)) //Лунный свет
                .setImage(R.drawable.item_moonlight)
                .setArtefact(true)
                .addModifier(Item.RADIATION_MODIFIER, 1.2d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.5d)
                .setXpPoints(10)
                .setDescription(R.string.desc_moonlight)
        );

//PSY (Противоментальные)
        possibleItems.put("StoneFlower835", (new ItemDescriptorImpl(ARTIFACTS, R.string.item_stone_flower)) //Каменный цветок
                .setImage(R.drawable.item_stone_flower)
                .setArtefact(true)
                .addModifier(Item.RADIATION_MODIFIER, 1.2d)
                .addModifier(Item.MENTAL_MODIFIER, 0.8d)
                .setXpPoints(5)
                .setDescription(R.string.desc_stone_flower)
        );
        possibleItems.put("Pe1licle149", (new ItemDescriptorImpl(ARTIFACTS, R.string.item_pellicle)) //Плёнка
                .setImage(R.drawable.item_pellicle)
                .setArtefact(true)
                .addModifier(Item.RADIATION_MODIFIER, 1.4d)
                .addModifier(Item.MENTAL_MODIFIER, 0.6d)
                .setXpPoints(10)
                .setDescription(R.string.desc_pellicle)
        );
        possibleItems.put("NightStar853", (new ItemDescriptorImpl(ARTIFACTS, R.string.item_night_star)) //Ночная звезда
                .setImage(R.drawable.item_night_star)
                .setArtefact(true)
                .addModifier(Item.RADIATION_MODIFIER, 1.6d)
                .addModifier(Item.MENTAL_MODIFIER, 0.4d)
                .setXpPoints(15)
                .setDescription(R.string.desc_night_star)
        );
        possibleItems.put("Compass478", (new ItemDescriptorImpl(ARTIFACTS, R.string.item_compass)) //Компас
                .setImage(R.drawable.item_compass)
                .setArtefact(true)
                .addModifier(Item.RADIATION_MODIFIER, 2d)
                .addModifier(Item.MENTAL_MODIFIER, 0.4d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.01d)
                .setXpPoints(30)
                .setDescription(R.string.desc_compass)
        );

//NEGATIVE RADIATION (Радиоактивные)
        possibleItems.put("Shell365", (new ItemDescriptorImpl(ARTIFACTS, R.string.item_shell)) //Пустышка
                .setImage(R.drawable.item_shell)
                .setArtefact(true)
                .addModifier(Item.RADIATION_EMMITER, 1d)
                .setDescription(R.string.desc_shell)
        );
        possibleItems.put("AlteredInsulator148", (new ItemDescriptorImpl(ARTIFACTS, R.string.item_altered_insulator)) //Радиоактивный (изменённый) изолятор
                .setImage(R.drawable.item_altered_insulator)
                .setArtefact(true)
                .addModifier(Item.RADIATION_EMMITER, 7d)
                .setDescription(R.string.desc_altered_insulator)
        );
        possibleItems.put("BlackEnergy964", (new ItemDescriptorImpl(ARTIFACTS, R.string.item_black_energy)) //Чёрная (тёмная) энергия
                .setImage(R.drawable.item_black_energy)
                .setArtefact(true)
                .addModifier(Item.RADIATION_EMMITER, 14d)
                .setDescription(R.string.desc_black_energy)
        );

//UPGRADES (Обвес)
        possibleItems.put("Binoculars135", (new ItemDescriptorImpl(UPGRADES, R.string.item_binocl)) //Бинокль
                .setImage(R.drawable.item_binocl)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(0)
                .setDescription(R.string.desc_binocl)
        );
        possibleItems.put("Optics135", (new ItemDescriptorImpl(UPGRADES, R.string.item_optics)) //Оптический прицел
                .setImage(R.drawable.item_optics)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(0)
                .setDescription(R.string.desc_optics)
        );
        possibleItems.put("Collimator854", (new ItemDescriptorImpl(UPGRADES, R.string.item_collimator)) //Коллиматор
                .setImage(R.drawable.item_collimator)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(0)
                .setDescription(R.string.desc_collimator)
        );
        possibleItems.put("NightVision786", (new ItemDescriptorImpl(UPGRADES, R.string.item_night_vision)) //Прибор ночного видения
                .setImage(R.drawable.item_night_vision)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(0)
                .setDescription(R.string.desc_night_vision)
        );
        possibleItems.put("WalkieTalkie259", (new ItemDescriptorImpl(UPGRADES, R.string.item_walkie_talkie)) //Рация
                .setImage(R.drawable.item_walkie_talkie)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(0)
                .setDescription(R.string.desc_walkie_talkie)
        );
        possibleItems.put("GrenadeLauncher548", (new ItemDescriptorImpl(UPGRADES, R.string.item_grenade_launcher)) //Гранатомёт
                .setImage(R.drawable.item_grenade_launcher)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(0)
                .setDescription(R.string.desc_grenade_launcher)
        );

//FOOD (Еда)
        possibleItems.put("DeadRat784", (new ItemDescriptorImpl(FOOD, R.string.item_dead_rat)) //Дохлая крыса
                .setImage(R.drawable.item_rat)
                .setArtefact(false)
                .addModifier(Item.HEALTH_INSTANT, 30d)
                .addModifier(Item.RADIATION_INSTANT, 2d)
                .setDescription(R.string.desc_dead_rat)
        );
        possibleItems.put("Bread965", (new ItemDescriptorImpl(FOOD, R.string.item_bread)) //Батон
                .setImage(R.drawable.item_bread)
                .setArtefact(false)
                .addModifier(Item.HEALTH_INSTANT, 3d)
                .setXpPoints(1)
                .setDescription(R.string.desc_bread)
        );
        possibleItems.put("Sausage324", (new ItemDescriptorImpl(FOOD, R.string.item_sausage)) //Колбаса
                .setImage(R.drawable.item_sausage)
                .setArtefact(false)
                .addModifier(Item.HEALTH_INSTANT, 6d)
                .setXpPoints(2)
                .setDescription(R.string.desc_sausage)
        );
        possibleItems.put("CannedMeat148", (new ItemDescriptorImpl(FOOD, R.string.item_canned_meat)) //Консерва
                .setImage(R.drawable.item_can)
                .setArtefact(false)
                .addModifier(Item.HEALTH_INSTANT, 9d)
                .setXpPoints(3)
                .setDescription(R.string.desc_canned_meat)
        );
        possibleItems.put("Doktorskaja148", (new ItemDescriptorImpl(FOOD, R.string.item_doktorskaja)) //Докторская
                .setImage(R.drawable.item_doktorskaja)
                .setArtefact(false)
                .addModifier(Item.HEALTH_INSTANT, 12d)
                .setXpPoints(5)
                .setDescription(R.string.desc_doktorskaja)
        );
        possibleItems.put("Delikates148", (new ItemDescriptorImpl(FOOD, R.string.item_delikates)) //Деликатес
                .setImage(R.drawable.item_delikates)
                .setArtefact(false)
                .addModifier(Item.HEALTH_INSTANT, 15d)
                .setXpPoints(6)
                .setDescription(R.string.desc_delikates)
        );
        possibleItems.put("Hercules459", (new ItemDescriptorImpl(FOOD, R.string.item_hercules)) //Геркулес
                .setImage(R.drawable.item_hercules)
                .setArtefact(false)
                .setBooster(true)
                .setDuration(30L * 60L * 1000L)
                .addModifier(Item.HEALTH_MODIFIER, 1.5d)
                .setDescription(R.string.desc_hercules)
        );
        possibleItems.put("EnergyDrink263", (new ItemDescriptorImpl(FOOD, R.string.item_energy_drink)) //Энергетик Сталкер
                .setImage(R.drawable.item_energy_drink)
                .setArtefact(false)
                .setBooster(true)
                .setDuration(30L * 60L * 1000L)
                .addModifier(Item.HEALTH_MODIFIER, 2d)
                .setDescription(R.string.desc_energy_drink)
        );
        possibleItems.put("RedDevil634", (new ItemDescriptorImpl(FOOD, R.string.item_red_devil)) //Левый энергетик
                .setImage(R.drawable.item_red_devil)
                .setArtefact(false)
                .setBooster(true)
                .setDuration(15L * 60L * 1000L)
                .addModifier(Item.HEALTH_MODIFIER, 0.5d)
                .addModifier(Item.RADIATION_MODIFIER, 0.5d)
                .setDescription(R.string.desc_red_devil)
        );

//WEAPONS (Оружие)
        possibleItems.put("Knife845", (new ItemDescriptorImpl(WEAPONS, R.string.item_knife)) //Нож
                .setImage(R.drawable.item_knife)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(0)
                .setDescription(R.string.desc_knife)
        );
        possibleItems.put("Pistol845", (new ItemDescriptorImpl(WEAPONS, R.string.item_pistol)) //Пистолет
                .setImage(R.drawable.item_pistol)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(0)
                .setDescription(R.string.desc_pistol)
        );
        possibleItems.put("Shotgun845", (new ItemDescriptorImpl(WEAPONS, R.string.item_shotgun)) //Дробовик
                .setImage(R.drawable.item_shotgun)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(0)
                .setDescription(R.string.desc_shotgun)
        );
        possibleItems.put("SubMachineGun359", (new ItemDescriptorImpl(WEAPONS, R.string.item_submachinegun)) //Пистолет-пулемёт
                .setImage(R.drawable.item_smg)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(0)
                .setDescription(R.string.desc_submachinegun)
        );
        possibleItems.put("AKtype148", (new ItemDescriptorImpl(WEAPONS, R.string.item_ak)) //Калаш
                .setImage(R.drawable.item_ak)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(0)
                .setDescription(R.string.desc_ak)
        );
        possibleItems.put("M4type148", (new ItemDescriptorImpl(WEAPONS, R.string.item_m4)) //М4
                .setImage(R.drawable.item_m4)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(0)
                .setDescription(R.string.desc_m4)
        );
        possibleItems.put("VAL648", (new ItemDescriptorImpl(WEAPONS, R.string.item_val)) //Вал
                .setImage(R.drawable.item_val)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(0)
                .setDescription(R.string.desc_val)
        );
        possibleItems.put("SniperGun358", (new ItemDescriptorImpl(WEAPONS, R.string.item_sniper)) //Снайперская винтовка
                .setImage(R.drawable.item_sniper)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(0)
                .setDescription(R.string.desc_sniper)
        );
        possibleItems.put("MachineGun257", (new ItemDescriptorImpl(WEAPONS, R.string.item_machinegun)) //Пулемёт
                .setImage(R.drawable.item_machinegun)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(0)
                .setDescription(R.string.desc_machinegun)
        );
        possibleItems.put("GaussGun458", (new ItemDescriptorImpl(WEAPONS, R.string.item_gauss_gun)) //Гаусс пушка
                .setImage(R.drawable.item_gauss)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(0)
                .setDescription(R.string.desc_gauss_gun)
        );

//AMMO (Боеприпасы)
        possibleItems.put("9mm784", (new ItemDescriptorImpl(AMMO, R.string.item_9mm)) //Пистолетные 9мм
                .setImage(R.drawable.item_9mm)
                .setXpPoints(1)
                .setDescription(R.string.desc_9mm)
        );
        possibleItems.put("12cal784", (new ItemDescriptorImpl(AMMO, R.string.item_12cal)) //Картечь 12 калибр
                .setImage(R.drawable.item_12cal)
                .setXpPoints(1)
                .setDescription(R.string.desc_12cal)
        );
        possibleItems.put("545ak875", (new ItemDescriptorImpl(AMMO, R.string.item_545ak)) //От АК 5.45
                .setImage(R.drawable.item_545ak)
                .setXpPoints(1)
                .setDescription(R.string.desc_545ak)
        );
        possibleItems.put("556nato875", (new ItemDescriptorImpl(AMMO, R.string.item_556nato)) //От НАТО 5.56
                .setImage(R.drawable.item_556nato)
                .setXpPoints(1)
                .setDescription(R.string.desc_556nato)
        );
        possibleItems.put("VALvintorez875", (new ItemDescriptorImpl(AMMO, R.string.item_9val)) //9мм Вал
                .setImage(R.drawable.item_9val)
                .setXpPoints(1)
                .setDescription(R.string.desc_9val)
        );
        possibleItems.put("762mm875", (new ItemDescriptorImpl(AMMO, R.string.item_762)) //7.62 универсальные
                .setImage(R.drawable.item_762)
                .setXpPoints(1)
                .setDescription(R.string.desc_762)
        );
        possibleItems.put("Gauss875", (new ItemDescriptorImpl(AMMO, R.string.item_atom)) //Гаусс патроны
                .setImage(R.drawable.item_atom)
                .setXpPoints(10)
                .addModifier(Item.RADIATION_INSTANT, 3d)
                .setDescription(R.string.desc_atom)
        );
        possibleItems.put("BigBK875", (new ItemDescriptorImpl(AMMO, R.string.item_bigbk)) //Большой боекомплект
                .setImage(R.drawable.item_bigbk)
                .setXpPoints(20)
                .setDescription(R.string.desc_bigbk)
        );
        possibleItems.put("F1grenade875", (new ItemDescriptorImpl(AMMO, R.string.item_f1)) //Ф1
                .setImage(R.drawable.item_f1)
                .setXpPoints(2)
                .setDescription(R.string.desc_f1)
        );
        possibleItems.put("Limonka875", (new ItemDescriptorImpl(AMMO, R.string.item_limonka)) //Лимонка
                .setImage(R.drawable.item_limonka)
                .setXpPoints(2)
                .setDescription(R.string.desc_limonka)
        );
        possibleItems.put("Zazhigalka875", (new ItemDescriptorImpl(AMMO, R.string.item_incend)) //Зажигательная граната
                .setImage(R.drawable.item_incend)
                .setXpPoints(2)
                .setDescription(R.string.desc_incend)
        );
        possibleItems.put("Podstvolka875", (new ItemDescriptorImpl(AMMO, R.string.item_podgren)) //Выстрел к гранатомёту
                .setImage(R.drawable.item_podgren)
                .setXpPoints(3)
                .setDescription(R.string.desc_podgren)
        );

//HABAR (Хабар)
        possibleItems.put("Garmoshka853", (new ItemDescriptorImpl(HABAR, R.string.item_garmoshka)) //Губная гармошка
                .setImage(R.drawable.item_garmoshka)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(4)
                .setDescription(R.string.desc_garmoshka)
        );
        possibleItems.put("Bag853", (new ItemDescriptorImpl(HABAR, R.string.item_bag)) //Спальный мешок
                .setImage(R.drawable.item_bag)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(5)
                .setDescription(R.string.desc_bag)
        );
        possibleItems.put("Gasoline853", (new ItemDescriptorImpl(HABAR, R.string.item_gasoline)) //Канистра бензина
                .setImage(R.drawable.item_gasoline)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(10)
                .setDescription(R.string.desc_gasoline)
        );
        possibleItems.put("Maps853", (new ItemDescriptorImpl(HABAR, R.string.item_maps)) //Старая карта
                .setImage(R.drawable.item_maps)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(15)
                .setDescription(R.string.desc_maps)
        );
        possibleItems.put("Guitar458", (new ItemDescriptorImpl(HABAR, R.string.item_guitar)) //Потрёпанная гитара
                .setImage(R.drawable.item_guitar)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(20)
                .setDescription(R.string.desc_guitar)
        );
        possibleItems.put("Instruments193", (new ItemDescriptorImpl(HABAR, R.string.item_instruments)) //Ящик с инструментами
                .setImage(R.drawable.item_instruments)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(30)
                .setDescription(R.string.desc_instruments)
        );
        possibleItems.put("UnknownDevice917", (new ItemDescriptorImpl(HABAR, R.string.item_psionic_device)) //Непонятный прибор
                .setImage(R.drawable.item_psionic_device)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(40)
                .setDescription(R.string.desc_psionic_device)
        );
        possibleItems.put("InfoPDA458", (new ItemDescriptorImpl(HABAR, R.string.item_pda)) //ПДА с важной инфой
                .setImage(R.drawable.item_pda)
                .setArtefact(false)
                .setConsumable(false)
                .setXpPoints(50)
                .setDescription(R.string.desc_pda)
        );

        //CALIBRATION (Калибрация)
        possibleItems.put("Calibrating102", (new ItemDescriptorImpl(DEVICES, R.string.item_calibrating_10)) //-10% к некоторым угрозам
                .setImage(R.drawable.item_calibrating_10)
                .setArtefact(true)
                .setConsumable(false)
                .setXpPoints(0)
                .addModifier(Item.RADIATION_MODIFIER, 0.9d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.9d)
                .addModifier(Item.MENTAL_MODIFIER, 0.9d)
                .addModifier(Item.BURER_MODIFIER, 0.9d)
                .addModifier(Item.CONTROLLER_MODIFIER, 0.9d)
                .addModifier(Item.MONOLITH_MODIFIER, 0.9d)
                .setDescription(R.string.desc_calibrating_10)
        );
        possibleItems.put("Calibrating257", (new ItemDescriptorImpl(DEVICES, R.string.item_calibrating_25)) //-25% к некоторым угрозам
                .setImage(R.drawable.item_calibrating_25)
                .setArtefact(true)
                .setConsumable(false)
                .setXpPoints(0)
                .addModifier(Item.RADIATION_MODIFIER, 0.75d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.75d)
                .addModifier(Item.MENTAL_MODIFIER, 0.75d)
                .addModifier(Item.BURER_MODIFIER, 0.75d)
                .addModifier(Item.CONTROLLER_MODIFIER, 0.75d)
                .addModifier(Item.MONOLITH_MODIFIER, 0.75d)
                .setDescription(R.string.desc_calibrating_25)
        );
        possibleItems.put("Calibrating508", (new ItemDescriptorImpl(DEVICES, R.string.item_calibrating_50)) //-50% к некоторым угрозам
                .setImage(R.drawable.item_calibrating_50)
                .setArtefact(true)
                .setConsumable(false)
                .setXpPoints(0)
                .addModifier(Item.RADIATION_MODIFIER, 0.5d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.5d)
                .addModifier(Item.MENTAL_MODIFIER, 0.5d)
                .addModifier(Item.BURER_MODIFIER, 0.5d)
                .addModifier(Item.CONTROLLER_MODIFIER, 0.5d)
                .addModifier(Item.MONOLITH_MODIFIER, 0.5d)
                .setDescription(R.string.desc_calibrating_50)
        );
        possibleItems.put("Calibrating753", (new ItemDescriptorImpl(DEVICES, R.string.item_calibrating_75)) //-99,9% к некоторым угрозам
                .setImage(R.drawable.item_calibrating_75)
                .setArtefact(true)
                .setConsumable(false)
                .setXpPoints(0)
                .addModifier(Item.RADIATION_MODIFIER, 0.25d)
                .addModifier(Item.ANOMALY_MODIFIER, 0.25d)
                .addModifier(Item.MENTAL_MODIFIER, 0.25d)
                .addModifier(Item.BURER_MODIFIER, 0.25d)
                .addModifier(Item.CONTROLLER_MODIFIER, 0.25d)
                .addModifier(Item.MONOLITH_MODIFIER, 0.25d)
                .setDescription(R.string.desc_calibrating_75)
        );

        setupLevels();
    }

    private void setupLevels()
    {
//        setupLevel1();
//        setupLevel2();
//        setupLevel3();
//        setupLevel4();
//        setupLevel5();
    }

//    private void setupLevel1()
//    {
//        List<ItemDescriptor> level1 = new ArrayList<>();
//        level1.add(makeLevelItem(R.string.item_knife, R.string.desc_knife, R.drawable.item_knife));
//        level1.add(makeLevelItem(R.string.item_pistol, R.string.desc_pistol, R.drawable.item_pistol));
//////////////        level1.add(makeLevelItem2(R.string.item_9mm, R.string.desc_9mm, R.drawable.item_9mm));
//        itemsByLevel.put(1, level1);
//    }

//    private void setupLevel2()
//    {
//        List<ItemDescriptor> level2 = new ArrayList<>();
//        level2.add(makeLevelItem(R.string.item_shotgun, R.string.desc_shotgun, R.drawable.item_shotgun));
//        itemsByLevel.put(2, level2);
//    }
//    private void setupLevel3()
//    {
//        List<ItemDescriptor> level3 = new ArrayList<>();
//        level3.add(makeLevelItem(R.string.item_submachinegun, R.string.desc_submachinegun, R.drawable.item_smg));
//        level3.add(makeLevelItem(R.string.item_ak, R.string.desc_ak, R.drawable.item_ak));
//        itemsByLevel.put(3, level3);
//    }
//    private void setupLevel4()
//    {
//        List<ItemDescriptor> level4 = new ArrayList<>();
//        level4.add(makeLevelItem(R.string.item_sniper, R.string.desc_sniper, R.drawable.item_sniper));
//        itemsByLevel.put(4, level4);
//    }
//    private void setupLevel5()
//    {
//        List<ItemDescriptor> level5 = new ArrayList<>();
//        level5.add(makeLevelItem(R.string.item_machinegun, R.string.desc_machinegun, R.drawable.item_machinegun));
//        itemsByLevel.put(5, level5);
//    }

//    private ItemDescriptor makeLevelItem(int title, int description, int image)
//    {
//        return (new ItemDescriptorImpl(WEAPONS, title).setDescription(description).setImage(image).setDropable(false).setConsumable(false));
//    }
///////    private ItemDescriptor makeLevelItem2(int title, int description, int image)
///////    {
///////        return (new ItemDescriptorImpl(AMMO, title).setDescription(description).setImage(image).setDropable(true).setConsumable(true));
///////    }
}
