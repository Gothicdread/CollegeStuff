package GameLogic;

import java.util.ArrayList;
import GameLogic.CrewMembers.*;
import GameLogic.Exceptions.*;
import GameLogic.Rooms.*;
import GameLogic.Tokens.*;

public class GameData
{
    // GAME STATS
    private int HP, MAX_HP;
    private int Hull, MAX_Hull;
    private int round;
    private int AP, MAX_AP;
    private int IP, MAX_IP;
    
    // GAME ELEMENTS
    private ArrayList<CrewMember> crew;
    private ArrayList<Room> ship;
    private ArrayList<Integer> rounds;
    private ArrayList<Token> tokens;
    private ArrayList<Alien> aliens;
    
    public GameData()
    {
        crew = new ArrayList<>();
        ship = new ArrayList<>();
        rounds = new ArrayList<>();
        tokens = new ArrayList<>();
        aliens = new ArrayList<>();
        
        CreateShip();
        
        MAX_AP = 5;
        MAX_HP = MAX_Hull = MAX_IP = 12;
    }
    
    //Not sure if here or in Game. Decide Later.
    //Return value: [1; (6*n)]
    private int DiceRoller(int n)
    {
        return (int) ((Math.random() * (6 * n)) + 1);
    }
    
    // =========== START GAME ===========
    //idk what to put in here
    public void StartGame()
    {
        round = 1;
        HP = 8;
        Hull = 8;
        IP = 0;
        
        CheckCrewMembers();
        
        AP = MAX_AP;
    }
    
    // =========== CREATE SHIP ===========
    
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
        ship.add(new Engineering());
        ship.add(new Astrometrics());
        ship.add(new Holodeck());
        ship.add(new Hydroponics());
    }
    
    public char getNextRound()
    {
        if(rounds.get(round - 1) == 0)
            return 'R';
        else if(rounds.get(round - 1) >= 1 && rounds.get(round - 1) <= 8)
            return 'A';
        else if(rounds.get(round - 1) >= 11 && rounds.get(round - 1) <= 18)
            return '*';
        else
            return 'R';
    }
    
    /* ==================================== */
    /* =========== CREW MEMBERS =========== */
    /* ==================================== */
    
    // =========== ADD CREW MEMBER ===========
    
    public void addCrewMember(CrewMember cm)
    {
        if(isCrewComplete())
            return;
        
        crew.add(cm);
    }
    
    // =========== MOVE CREW MEMBER ===========
    
    public void moveCrewMember(CrewMember cm, Room room, boolean flag) //incomplete
    {
        if(flag)
        {
            
            AP--;
        }
        else 
        {
            
            AP--;
        }
        
    }
    
    // =========== CHECK CREW MEMBER ===========
    
    private void CheckCrewMembers()
    {
        for (CrewMember cm : crew)
        {
            if(cm instanceof Commander)
                MAX_AP = 6;
            else if(cm instanceof MoralOfficer)
                IP = 5;
            else if(cm instanceof ShuttlePilot)
                HP += 4;
        }
    }
    
    private boolean isTransporterChief(CrewMember cm)
    { return (cm instanceof TransporterChief); }
    
    // =========== GET CREW MEMBERS ===========
    
    public String getCrewMembers()
    {
        String aux = "";
        
        if(crew.isEmpty())
            return aux;
        
        for (int i = 0; i < crew.size(); i++)
        {
            aux += crew.get(i).toString();
            
            if(!(i == (crew.size() - 1))) aux += ", ";
        }
        
        return aux;
    }
    
    // =========== GET CREW MEMBER WITH LOCATION ===========
    
    public String getCrewMembersWithLocation()
    {
        String aux = "";
        
        if(crew.isEmpty())
            return aux;
        
        for (CrewMember cm : crew)
        {
            aux += cm.toString() + " Location: " + cm.getRoom() + "\n";
        }
        
        return aux;
    }
    
    public boolean isCrewComplete() { return (crew.size() == 2); }

    /* ============================= */
    /* =========== ALIEN =========== */
    /* ============================= */
    
    // =========== CHECK ALIEN LOCATION =========== 
    
    public String checkAlienLocation()
    {
        String aux = "";
        
        if(aliens.isEmpty())
            return aux;
        
        return aux;
    }
    
    
    /* ================================= */
    /* =========== ADVENTURE =========== */
    /* ================================= */
    
    //  SET DEFAULT ADVENTURE
    
    public void setDefaultAdventure()
    {
        rounds.clear();
        rounds.add(2);
        rounds.add(3);
        rounds.add(4);
        rounds.add(15);
        rounds.add(0);
        rounds.add(4);
        rounds.add(5);
        rounds.add(16);
        rounds.add(0);
        rounds.add(6);
        rounds.add(17);
        rounds.add(0);
        rounds.add(8);
    }
        
    // =========== GET ADVENTURE ===========
    
    public String getAdventure()
    {
        String aux = "";
        
        if(rounds.isEmpty())
            return aux;
        
        for (int i = 0; i < rounds.size(); i++)
        {
            if(i+1 == round) aux += "(";
            
            if(rounds.get(i) == 0) aux += "R";
            else if(rounds.get(i) >= 1 && rounds.get(i) <= 8)
                aux += rounds.get(i) + "A";
            else if(rounds.get(i) >= 11 && rounds.get(i) <= 18)
                aux += (rounds.get(i) - 10) + "A*";
            
            if(i+1 == round) aux += ")";
            
            if(!(i == (rounds.size() - 1))) aux += ", ";
        }
        
        return aux;
    }
    
    // =========== CLEAR ADVENTURE ===========
    
    public void clearAdventure() { rounds.clear(); }
    
    // =========== ADD ROUND ===========
    
    //0 - Resting Round
    //[1; 8] - Number of Aliens to Spawn
    //[11; 18] - Number of Aliens to Spawn and Retreat
    public void addRound(int round) throws InvalidRoundException
    {
        if(isAdventureComplete())
            return;
        
        if(round == 0 || (round >= 1 && round <= 8) || (round >= 11 && round <= 18))
            rounds.add(round);
        else
            throw new InvalidRoundException();
    }
    
    public boolean isAdventureComplete() { return (rounds.size() == 13); }

    // =========== VERIFY IF EXITS =========== 
    
    public boolean hasEngineer() { return hasX("Engineer"); }
    
    public boolean hasDoctor() { return hasX("Doctor"); }
    
    private boolean hasX(String name)
    {
        for (CrewMember cm : crew)
        {
            if(cm.toString().equalsIgnoreCase(name))
                return true;
        }
        
        return false;
    }
    
    public boolean hasSetParticleDispenser()
    {
        for (Token token : tokens)
        {
            if(token instanceof ParticleDispenser)
                return token.isSet();
        }
        
        return false;
    }
    
    public boolean hasMoreAP() { return (getAP() > 0); }
    
    // =========== GAME VARIABLE GETTERS =========== 
    
    public int getAP() {return AP;}
    
    public final int getRound() { return round; }
    
    public String getStats()
    {
        String aux = "";
        
        aux += "HP: " + HP + "\tHull: " + Hull + "\tIP: " + IP + "\n";
        aux += checkAlienLocation();
        
        return aux;
    }
}
