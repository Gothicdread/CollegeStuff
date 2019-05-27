package GameLogic.States;

import GameLogic.Exceptions.*;

public interface gState
{
    public gState AwaitPlayerChoice(String name);
    public gState AwaitRestingPlayerChoice(String name);
    public gState StartGame();
    public gState ChooseCrewMember(String name) throws InvalidCrewMemberException, CrewMemberAlreadySelectedException;
    public gState ChooseRound(String round) throws InvalidRoundException;
    public gState setDefaultAdventure();
    public gState clearAdventure();
    public gState clearCrew();
}