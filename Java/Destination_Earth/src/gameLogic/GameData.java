package gameLogic;

import gameLogic.Crew.*;
import gameLogic.Ship.*;
import static gameLogic.Dice.diceRoller;
import gameLogic.Exceptions.*;
import gameLogic.Ship.Ship;
import gameLogic.Tokens.Alien;
import java.util.ArrayList;

public class GameData
{
    static int MAX_POINTS = 12; //o max é sempre 12
    
    private final int DEFAULT_IP = 0;
    private final int DEFAULT_HP = 8;
    private final int DEFAULT_AP = 5;
    
    private int round = 1;
    private int HP;
    private int AP;
    private int IP;
    
    private ArrayList<CrewMembers> crew;
    private ArrayList<String> adventure;
    private ArrayList<Ship> ship;
    private ArrayList<Alien> aliens;
    
    private boolean hasDoctor = false;
    private boolean hasEngineer = false;
    private boolean hasCommander = false;
    
    private boolean AliensRetreatAfterRound = false;
    
    public GameData()
    {
        HP = DEFAULT_HP;
        AP = DEFAULT_AP;
        IP = DEFAULT_IP;
        
        crew = new ArrayList<>();
        adventure = new ArrayList<>();
        ship = new ArrayList<>();
        aliens = new ArrayList<>();
    }
    
    /**
     * (static) returns maximum hit points
     * @return MAX_POINTS
     */
    public static int getMaxPoints() { return MAX_POINTS; }
    
    public void AddCrewMember(CrewMembers crewmember) throws CrewMemberAlreadyPresentException
    {
        if(crew.isEmpty())
            crew.add(crewmember);
        else
        {
            for (CrewMembers it : crew)
            {
                if(crewmember.getClass() == it.getClass())
                {
                    throw new CrewMemberAlreadyPresentException();
                }
            }
            
            crew.add(crewmember);
        }
    }
    
    public void ClearCrew() { crew.clear(); }
    public void ClearAdventure() { adventure.clear(); }
    
    public boolean isAdventureSet() { return (adventure.size() >= 13); }
    public boolean isCrewComplete() { return (crew.size() == 2); }
    
    public void addRound(String round) { adventure.add(round); }
    
    /*
     * create a default adventure
     */
    public void SetDefaultAdventure()
    {
        ClearAdventure();
        
        adventure.add("2A");
        adventure.add("3A");
        adventure.add("4A");
        adventure.add("5A*");
        adventure.add("R");
        adventure.add("4A");
        adventure.add("5A");
        adventure.add("6A*");
        adventure.add("R");
        adventure.add("6A");
        adventure.add("7A*");
        adventure.add("R");
        adventure.add("8A");
    }
    
    public String getAdjacentRooms(int room) { return ship.get(room - 1).getDoors(); }
    
    public String getRoundsAsString() { return adventure.toString(); }
    public String getCrewMembersAsString()
    {
        int i = 1;
        String str = "[";
        
        for (CrewMembers it : crew)
        {
            str += it.toString();
            if(i < crew.size())
            {
                str += ", ";
                i++;
            }
        }
        
        str += "]";
        
        return str;
    }
    
    public String getCrewMembersInfo()
    {
        String info = "";
        
        for (CrewMembers cm : crew)
        {
            info += cm.toString() + " Location: " + ship.get(cm.getCurrentPosition() - 1).getName() + "\n";
        }
        
        return info;
    }
    
    public CrewMembers getCrewMember(int index)
    {
        if(crew.get(index) == null)
            return null;
        else
            return crew.get(index);
    }
    
    public String getRound(int i) { return adventure.get(i-1); }
    
    public int getRoundNumber() { return round; }
    
    public int DieRoll() { return diceRoller(1); }
    
    private void SetupCrew()
    {
        for (CrewMembers it : crew)
        {
            if(it instanceof Commander)
            {
                hasCommander = true;
                AP = 6;
            }
            else if(it instanceof Doctor)
                hasDoctor = true;
            else if(it instanceof Engineer)
                hasEngineer = true;
            else if(it instanceof MoralOfficer)
                IP = 5;
            else if(it instanceof ShuttlePilot)
                HP += 4;
            
            it.setNewPosition(diceRoller(2));
            if(!ship.isEmpty())
                ship.get(it.getCurrentPosition() - 1).MoveCrewToHere(it);
        }
    }
    
    private void CreateShip()
    {
        ship.add(new Bridge());
        ship.add(new SickBay());
        ship.add(new Brig());
        ship.add(new CrewQuarters());
        ship.add(new ConferenceRoom());
        ship.add(new ShuttleBay());
        ship.add(new WeaponsBay());
        ship.add(new MessHall());
        ship.add(new Enginnering());
        ship.add(new Astrometrics());
        ship.add(new Holodeck());
        ship.add(new Hydroponics());
    }
    
    private void SpawnAliens()
    {
        String curRound = adventure.get(round - 1);
        
        if(curRound.length() == 1)
            return;
        
        if(curRound.length() == 3)
            AliensRetreatAfterRound = true;
        
        int numToSpawn = Character.getNumericValue(curRound.charAt(0));
        
        for(int i = 0; i < numToSpawn; i++)
        {
            int room = diceRoller(2);
            Alien alien = new Alien(room);
            aliens.add(alien);
            ship.get(room - 1).MoveAlienToHere(alien);
        }
    }
    
    public void startgame()
    {
        CreateShip();
        SetupCrew();
        SpawnAliens();
    }
    
    public void nextRound()
    {
        round++;
        
        if(AliensRetreatAfterRound)
        {
            for (Ship room : ship)
            {
                room.clearAliens();
            }
            aliens.clear();;
            AliensRetreatAfterRound = false;
        }
        
        if(hasCommander)
            AP = 6;
        else
            AP = 5;
        SpawnAliens();
    }
    
    public void CrewMemberAttack(CrewMembers cm, int room) throws InvalidRoomException, NoAliensToAttackException
    {
        if(!(cm instanceof ScienceOfficer) && (room != cm.getCurrentPosition()))
            throw new InvalidRoomException();
        
        int roll = diceRoller(cm.getDiceNumber());
        
        if(cm instanceof Captain)
        {
            if(roll + 3 > 6 * cm.getDiceNumber())
                roll = 6 * cm.getDiceNumber();
            else
                roll += 3;
        }
        
        if(!ship.get(room - 1).hasAliens())
            throw new NoAliensToAttackException();
        
        if(roll > 5)
        {
            if(cm instanceof ScienceOfficer)
            {
                if(!CheckIfScienceOfficerCanAttack(cm, room))
                    throw new InvalidRoomException();
            }
            
            aliens.remove(ship.get(room - 1).killAlien());
            IP++;
        }
        
        AP--;
    }
    
    public boolean CheckIfScienceOfficerCanAttack(CrewMembers cm, int AttackRoom)
    {
        return ship.get(cm.getCurrentPosition()).hasDoor(AttackRoom);
    }
    
    public boolean hasDoctor() { return this.hasDoctor; }
    public boolean hasEngineer() { return this.hasEngineer; }
    
    public int getHP() { return HP; }
    public int getAP() { return AP; }
    public int getIP() { return IP; }
}
