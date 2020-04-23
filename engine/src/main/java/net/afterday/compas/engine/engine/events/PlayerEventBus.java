package net.afterday.compas.engine.engine.events;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import net.afterday.compas.engine.core.player.Player;

public class PlayerEventBus
{
    public static final String SUICIDE = "suicide";

    private static PlayerEventBus instance;
    private static final Subject<Integer> playerLevelChanges = PublishSubject.create();
    private static final Subject<Integer> playerInfluences = PublishSubject.create();
    private static final Subject<String> playerCommands = PublishSubject.create();
    private static final Subject<Player.STATE> playerStates = BehaviorSubject.create();
    private static final Subject<Player.FRACTION> playerFractionStream = BehaviorSubject.create();

    private PlayerEventBus()
    {

    }

    public static PlayerEventBus instance()
    {
        if(instance == null)
        {
            instance = new PlayerEventBus();
        }
        return instance;
    }

    public Subject<Player.STATE> getPlayerStateStream()
    {
        return playerStates;
    }

    public Subject<Player.FRACTION> getPlayerFractionStream()
    {
        return playerFractionStream;
    }

    public void setPlayerFraction(Player.FRACTION fraction)
    {
        playerFractionStream.onNext(fraction);
    }

    public void setPlayerState(Player.STATE playerState)
    {
        playerStates.onNext(playerState);
    }

    public void suicide()
    {
        playerCommands.onNext(SUICIDE);
    }

    public Observable<String> getPlayerCommandsStream()
    {
        return playerCommands;
    }
}
