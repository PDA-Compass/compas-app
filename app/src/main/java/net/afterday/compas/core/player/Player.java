package net.afterday.compas.core.player;

import net.afterday.compas.core.events.PlayerEventsListener;
import net.afterday.compas.core.gameState.Frame;
import net.afterday.compas.core.inventory.Inventory;
import net.afterday.compas.core.inventory.items.Item;
import net.afterday.compas.core.serialization.Jsonable;
import net.afterday.compas.persistency.items.ItemDescriptor;

/**
 * Created by spaka on 4/18/2018.
 */

public interface Player extends Jsonable
{
    static final int ALIVE = 1;
    //static final int HEALING = 2;
    static final int NON_HUMAN = 2;
    static final int ZOMBIFIED = 2;
    static final int CONTROLLED = 3;
    static final int DEAD = 4;
    int INSTANT_DEATH = 1;
    int ABDUCTED = 2;
    int W_ABDUCTED = 3;
    int SUICIDE_NOT_ALLOWED = 4;

    long SEC30 = 30 * 1;
    long MIN1 = 60 * 1;
    long MIN2 = 60 * 2;
    long MIN3 = 60 * 3;
    long MIN5 = 60 * 5;
    long MIN7 = 60 * 7;
    long MIN15 = 60 * 15;
    long MIN30 = 60 * 30;
    long MIN60 = 60 * 60;

    enum STATE {
        ALIVE(Player.ALIVE, Player.W_ABDUCTED),   //+
        DEAD_CONTROLLER(Player.DEAD),  //-
        DEAD_ANOMALY(Player.DEAD),  //-
        DEAD_SPRINGBOARD(Player.DEAD),  //-
        DEAD_FUNNEL(Player.DEAD),  //-
        DEAD_CAROUSEL(Player.DEAD),  //-
        DEAD_ELEVATOR(Player.DEAD),  //-
        DEAD_FRYING(Player.DEAD),  //-
        DEAD_ELECTRA(Player.DEAD),  //-
        DEAD_MEATGRINDER(Player.DEAD),  //-
        DEAD_KISSEL(Player.DEAD),  //-
        DEAD_SODA(Player.DEAD),  //-
        DEAD_ACIDFOG(Player.DEAD),  //-
        DEAD_BURNINGFLUFF(Player.DEAD),  //-
        DEAD_RUSTYHAIR(Player.DEAD),  //-
        DEAD_SPATIALBUBBLE(Player.DEAD),  //-
        DEAD_RADIATION(Player.DEAD),  //-
        DEAD_EMISSION(Player.DEAD),  //+ Staigi mirtis
        DEAD_BURER(Player.DEAD),  //-
        DEAD_MENTAL(Player.DEAD),  //-
        CONTROLLED(Player.DEAD, Player.INSTANT_DEATH),  //+   Staigi mirtis
        MENTALLED(Player.DEAD, Player.INSTANT_DEATH),  //+   Staigi mirtis
        W_CONTROLLED(Player.DEAD), //-
        W_MENTALLED(Player.DEAD), //-
        W_DEAD_BURER(Player.DEAD),  //-
        W_DEAD_RADIATION(Player.DEAD),  //-
        W_DEAD_ANOMALY(Player.DEAD),  //-
        W_DEAD_SPRINGBOARD(Player.DEAD),  //-
        W_DEAD_FUNNEL(Player.DEAD),  //-
        W_DEAD_CAROUSEL(Player.DEAD),  //-
        W_DEAD_ELEVATOR(Player.DEAD),  //-
        W_DEAD_FRYING(Player.DEAD),  //-
        W_DEAD_ELECTRA(Player.DEAD),  //-
        W_DEAD_MEATGRINDER(Player.DEAD),  //-
        W_DEAD_KISSEL(Player.DEAD),  //-
        W_DEAD_SODA(Player.DEAD),  //-
        W_DEAD_ACIDFOG(Player.DEAD),  //-
        W_DEAD_BURNINGFLUFF(Player.DEAD),  //-
        W_DEAD_RUSTYHAIR(Player.DEAD),  //-
        W_DEAD_SPATIALBUBBLE(Player.DEAD),  //-
        W_ABDUCTED(Player.DEAD, Player.ABDUCTED),  //+ Jei paspaudzia kolba, pereina i abducted
        ABDUCTED(Player.ALIVE, Player.INSTANT_DEATH);  //+ Staigi mirtis

        private final int code;
        private final int suicideType;

        STATE(int code, int suicideType)
        {
            this.code = code;
            this.suicideType = suicideType;
        }

        STATE(int code)
        {
            this.code = code;
            this.suicideType = Player.SUICIDE_NOT_ALLOWED;
        }

        public int getCode()
        {
            return this.code;
        }

        public int getSuicideType()
        {
            return this.suicideType;
        }

        public long getWaitTime()
        {
            switch (this)
            {
                case ALIVE: return 0l;
                case DEAD_CONTROLLER: return 01; //
                case DEAD_ANOMALY: return MIN1;
                case DEAD_SPRINGBOARD: return MIN1;
                case DEAD_FUNNEL: return MIN1;
                case DEAD_CAROUSEL: return MIN1;
                case DEAD_ELEVATOR: return MIN1;
                case DEAD_FRYING: return MIN1;
                case DEAD_ELECTRA: return MIN1;
                case DEAD_MEATGRINDER: return MIN1;
                case DEAD_KISSEL: return MIN1;
                case DEAD_SODA: return MIN1;
                case DEAD_ACIDFOG: return MIN1;
                case DEAD_BURNINGFLUFF: return MIN1;
                case DEAD_RUSTYHAIR: return MIN1;
                case DEAD_SPATIALBUBBLE: return MIN1;
                case DEAD_RADIATION: return 0l;
                case DEAD_BURER: return 0l;
                case DEAD_MENTAL: return MIN1;
                case CONTROLLED: return MIN30; //Под-контроль
                case MENTALLED: return MIN30; //Зомбирование
                case W_MENTALLED: return MIN2; //Мутация
                case W_CONTROLLED: return MIN2; // Мутация
                case W_DEAD_BURER: return MIN5; //Агония
                case W_DEAD_RADIATION: return MIN5; // Агония
                case W_DEAD_ANOMALY: return MIN5; // Агония
                case W_DEAD_SPRINGBOARD: return MIN5; // Агония
                case W_DEAD_FUNNEL: return MIN5; // Агония
                case W_DEAD_CAROUSEL: return MIN5; // Агония
                case W_DEAD_ELEVATOR: return MIN5; // Агония
                case W_DEAD_FRYING: return MIN5; // Агония
                case W_DEAD_ELECTRA: return MIN5; // Агония
                case W_DEAD_MEATGRINDER: return MIN5; // Агония
                case W_DEAD_KISSEL: return MIN5; // Агония
                case W_DEAD_SODA: return MIN5; // Агония
                case W_DEAD_ACIDFOG: return MIN5; // Агония
                case W_DEAD_BURNINGFLUFF: return MIN5; // Агония
                case W_DEAD_RUSTYHAIR: return MIN5; // Агония
                case W_DEAD_SPATIALBUBBLE: return MIN5; // Агония
                case W_ABDUCTED: return MIN5; // Агония
                case ABDUCTED: return MIN60; //Плен
                case DEAD_EMISSION: return MIN1;
            };
            return MIN5;
        }

        @Override
        public String toString()
        {
            switch (this)
            {
                case ALIVE: return "ALIVE";
                case DEAD_CONTROLLER: return "DEAD_CONTROLLER";
                case DEAD_ANOMALY: return "DEAD_ANOMALY";
                case DEAD_SPRINGBOARD: return "DEAD_SPRINGBOARD";
                case DEAD_FUNNEL: return "DEAD_FUNNEL";
                case DEAD_CAROUSEL: return "DEAD_CAROUSEL";
                case DEAD_ELEVATOR: return "DEAD_ELEVATOR";
                case DEAD_FRYING: return "DEAD_FRYING";
                case DEAD_ELECTRA: return "DEAD_ELECTRA";
                case DEAD_MEATGRINDER: return "DEAD_MEATGRINDER";
                case DEAD_KISSEL: return "DEAD_KISSEL";
                case DEAD_SODA: return "DEAD_SODA";
                case DEAD_ACIDFOG: return "DEAD_ACIDFOG";
                case DEAD_BURNINGFLUFF: return "DEAD_BURNINGFLUFF";
                case DEAD_RUSTYHAIR: return "DEAD_RUSTYHAIR";
                case DEAD_SPATIALBUBBLE: return "DEAD_SPATIALBUBBLE";
                case DEAD_RADIATION: return "DEAD_RADIATION";
                case DEAD_BURER: return "DEAD_BURER";
                case DEAD_MENTAL: return "DEAD_MENTAL";
                case CONTROLLED: return "CONTROLLED";
                case MENTALLED: return "MENTALLED";
                case W_MENTALLED: return "W_MENTALLED";
                case W_CONTROLLED: return "W_CONTROLLED";
                case W_DEAD_BURER: return "W_DEAD_BURER";
                case W_DEAD_RADIATION: return "W_DEAD_RADIATION";
                case W_DEAD_ANOMALY: return "W_DEAD_ANOMALY";
                case W_DEAD_SPRINGBOARD: return "W_DEAD_SPRINGBOARD";
                case W_DEAD_FUNNEL: return "W_DEAD_FUNNEL";
                case W_DEAD_CAROUSEL: return "W_DEAD_CAROUSEL";
                case W_DEAD_ELEVATOR: return "W_DEAD_ELEVATOR";
                case W_DEAD_FRYING: return "W_DEAD_FRYING";
                case W_DEAD_ELECTRA: return "W_DEAD_ELECTRA";
                case W_DEAD_MEATGRINDER: return "W_DEAD_MEATGRINDER";
                case W_DEAD_KISSEL: return "W_DEAD_KISSEL";
                case W_DEAD_SODA: return "W_DEAD_SODA";
                case W_DEAD_ACIDFOG: return "W_DEAD_ACIDFOG";
                case W_DEAD_BURNINGFLUFF: return "W_DEAD_BURNINGFLUFF";
                case W_DEAD_RUSTYHAIR: return "W_DEAD_RUSTYHAIR";
                case W_DEAD_SPATIALBUBBLE: return "W_DEAD_SPATIALBUBBLE";
                case W_ABDUCTED: return "W_ABDUCTED";
                case ABDUCTED: return "ABDUCTED";
                case DEAD_EMISSION: return "DEAD_EMISSION";
            }
            return "Unknown player state!";
        }

        public static STATE fromString(String s)
        {
            switch (s)
            {
                case "ALIVE": return Player.STATE.ALIVE;
                case "DEAD_CONTROLLER": return Player.STATE.DEAD_CONTROLLER;
                case "DEAD_ANOMALY": return Player.STATE.DEAD_ANOMALY;
                case "DEAD_SPRINGBOARD": return Player.STATE.DEAD_SPRINGBOARD;
                case "DEAD_FUNNEL": return Player.STATE.DEAD_FUNNEL;
                case "DEAD_CAROUSEL": return Player.STATE.DEAD_CAROUSEL;
                case "DEAD_ELEVATOR": return Player.STATE.DEAD_ELEVATOR;
                case "DEAD_FRYING": return Player.STATE.DEAD_FRYING;
                case "DEAD_ELECTRA": return Player.STATE.DEAD_ELECTRA;
                case "DEAD_MEATGRINDER": return Player.STATE.DEAD_MEATGRINDER;
                case "DEAD_KISSEL": return Player.STATE.DEAD_KISSEL;
                case "DEAD_SODA": return Player.STATE.DEAD_SODA;
                case "DEAD_ACIDFOG": return Player.STATE.DEAD_ACIDFOG;
                case "DEAD_BURNINGFLUFF": return Player.STATE.DEAD_BURNINGFLUFF;
                case "DEAD_RUSTYHAIR": return Player.STATE.DEAD_RUSTYHAIR;
                case "DEAD_SPATIALBUBBLE": return Player.STATE.DEAD_SPATIALBUBBLE;
                case "DEAD_RADIATION": return Player.STATE.DEAD_RADIATION;
                case "DEAD_EMISSION": return STATE.DEAD_EMISSION;
                case "DEAD_BURER": return Player.STATE.DEAD_BURER;
                case "DEAD_MENTAL": return Player.STATE.DEAD_MENTAL;
                case "CONTROLLED": return Player.STATE.CONTROLLED;
                case "MENTALLED": return Player.STATE.MENTALLED;
                case "W_MENTALLED": return Player.STATE.W_MENTALLED;
                case "W_CONTROLLED": return Player.STATE.W_CONTROLLED;
                case "W_DEAD_BURER": return Player.STATE.W_DEAD_BURER;
                case "W_DEAD_RADIATION": return Player.STATE.W_DEAD_RADIATION;
                case "W_DEAD_ANOMALY": return Player.STATE.W_DEAD_ANOMALY;
                case "W_DEAD_SPRINGBOARD": return Player.STATE.W_DEAD_SPRINGBOARD;
                case "W_DEAD_FUNNEL": return Player.STATE.W_DEAD_FUNNEL;
                case "W_DEAD_CAROUSEL": return Player.STATE.W_DEAD_CAROUSEL;
                case "W_DEAD_ELEVATOR": return Player.STATE.W_DEAD_ELEVATOR;
                case "W_DEAD_FRYING": return Player.STATE.W_DEAD_FRYING;
                case "W_DEAD_ELECTRA": return Player.STATE.W_DEAD_ELECTRA;
                case "W_DEAD_MEATGRINDER": return Player.STATE.W_DEAD_MEATGRINDER;
                case "W_DEAD_KISSEL": return Player.STATE.W_DEAD_KISSEL;
                case "W_DEAD_SODA": return Player.STATE.W_DEAD_SODA;
                case "W_DEAD_ACIDFOG": return Player.STATE.W_DEAD_ACIDFOG;
                case "W_DEAD_BURNINGFLUFF": return Player.STATE.W_DEAD_BURNINGFLUFF;
                case "W_DEAD_RUSTYHAIR": return Player.STATE.W_DEAD_RUSTYHAIR;
                case "W_DEAD_SPATIALBUBBLE": return Player.STATE.W_DEAD_SPATIALBUBBLE;
                case "W_ABDUCTED": return Player.STATE.W_ABDUCTED;
                case "ABDUCTED": return Player.STATE.ABDUCTED;
            }
            return Player.STATE.DEAD_BURER;
        }
    }

    enum FRACTION {
        STALKER(0),
        //Monolito neveikia mentalas, ir emisija, monolitas gydo. Yra vaizdas, bet nera garso
        MONOLITH(1),
        GAMEMASTER(2),
        DARKEN(3);

        FRACTION(int id)
        {

        }

        @Override
        public String toString()
        {
            switch (this)
            {
                case STALKER: return "STALKER";
                case MONOLITH: return "MONOLITH";
                case GAMEMASTER: return "GAMEMASTER";
                case DARKEN: return "DARKEN";
            }
            return "UnknownFraction";
        }

        public static FRACTION fromString(String fraction)
        {
            switch (fraction)
            {
                case "STALKER": return STALKER;
                case "MONOLITH": return MONOLITH;
                case "GAMEMASTER": return GAMEMASTER;
                case "DARKEN": return DARKEN;
            }
            return null;
        }
    }

    enum COMMAND {
        REVIVE,
        KILL,
        ZOMBI;
    }

    PlayerProps getPlayerProps();
    Inventory getInventory();
    boolean addItem(ItemDescriptor itemDescriptor, String code);
    boolean dropItem(Item item);
    Frame useItem(Item item);
    void addPlayerEventsListener(PlayerEventsListener playerEventsListener);
    Frame setState(STATE state);
    boolean setFraction(FRACTION fraction);
    boolean reborn();
}
