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
        KILL
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
