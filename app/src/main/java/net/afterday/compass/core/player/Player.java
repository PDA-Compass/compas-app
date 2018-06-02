package net.afterday.compass.core.player;

import net.afterday.compass.core.events.PlayerEventsListener;
import net.afterday.compass.core.gameState.Frame;
import net.afterday.compass.core.inventory.Inventory;
import net.afterday.compass.core.inventory.items.Item;

/**
 * Created by spaka on 4/18/2018.
 */

public interface Player
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

    enum STATE {
        ALIVE(Player.ALIVE, Player.W_ABDUCTED),   //+
        DEAD_CONTROLLER(Player.DEAD),  //-
        DEAD_ANOMALY(Player.DEAD),  //-
        DEAD_RADIATION(Player.DEAD),  //-
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

        @Override
        public String toString()
        {
            String ps = "PlayerState: ";
            switch (this)
            {
                case ALIVE: return ps + "ALIVE";
                case DEAD_CONTROLLER: return ps + "DEAD_CONTROLLER";
                case DEAD_ANOMALY: return ps + "DEAD_ANOMALY";
                case DEAD_RADIATION: return ps + "DEAD_RADIATION";
                case DEAD_BURER: return ps + "DEAD_BURER";
                case DEAD_MENTAL: return ps + "DEAD_MENTAL";
                case CONTROLLED: return ps + "CONTROLLED";
                case MENTALLED: return ps + "MENTALLED";
                case W_MENTALLED: return ps + "W_MENTALLED";
                case W_CONTROLLED: return ps + "W_CONTROLLED";
                case W_DEAD_BURER: return ps + "W_DEAD_BURER";
                case W_DEAD_RADIATION: return ps + "W_DEAD_RADIATION";
                case W_DEAD_ANOMALY: return ps + "W_DEAD_ANOMALY";
                case W_ABDUCTED: return ps + "W_ABDUCTED";
                case ABDUCTED: return ps + "ABDUCTED";
            }
            return "Unknown player state!";
        }
    }


    enum ENV_STATE {

    }

    boolean acceptsInfluence(int inflId);
    PlayerProps getPlayerProps();
    Inventory getInventory();
    boolean addItem(String code);
    boolean dropItem(Item item);
    Frame useItem(Item item);
    void addPlayerEventsListener(PlayerEventsListener playerEventsListener);
    Frame setState(STATE state);

}
