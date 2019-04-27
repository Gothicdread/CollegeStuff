package ui.text;

import java.util.Scanner;
import gameLogic.Game;
import gameLogic.States.*;
import gameLogic.Crew.*;
import gameLogic.Exceptions.*;
import java.util.InputMismatchException;

public class MainText
{
    boolean quit = false;
    int menu_crew_page = 1;
    Scanner sc;
    Game game;
    
    int[] taken = {0,0};
    int count = 0;
    CrewMembersText crew_names;
    
    public MainText(Game game)
    {
        sc = new Scanner(System.in);
        this.game = game;
        crew_names = new CrewMembersText();
    }
    
    private void PrintMainMenu()
    {
        System.out.println("1 - Start Game");
        System.out.println("2 - Choose Adventure");
        System.out.println("3 - Choose Crew");
        System.out.println("0 - Quit Game");
    }
    
    private void PrintCrewOptions(int page)
    {
        crew_names.CrewOptions(taken, page);
    }
    
    public void AddCrewMember(int menu_page, int opt) throws CrewMemberAlreadyPresentException
    {
        if(menu_page == 1)
            switch(opt)
            {
                default:
                    break;
                case 1:
                    game.AddCrewMember(new Captain());                    
                    break;
                case 2:
                    game.AddCrewMember(new Commander());
                    break;
                case 3:
                    game.AddCrewMember(new CommsOfficer());
                    break;
                case 4:
                    game.AddCrewMember(new Doctor());
                    break;
                case 5:
                    game.AddCrewMember(new Engineer());
                    break;
                case 6:
                    game.AddCrewMember(new MoralOfficer());
                    break;
                case 7:
                    game.AddCrewMember(new NavigationOfficer());
                    break;
                case 8:
                    game.AddCrewMember(new RedShirt());
                    break;
                case 9:
                    game.AddCrewMember(new ScienceOfficer());
                    break;
            }
        else if(menu_page == 2)
            switch(opt)
            {
                default:
                    break;
                case 1:
                    game.AddCrewMember(new SecurityOfficer());
                    break;
                case 2:
                    game.AddCrewMember(new ShuttlePilot());
                    break;
                case 3:
                    game.AddCrewMember(new TransporterChief());
                    break;
            }
    }
    
    public void ChooseCrew()
    {
        int opt = -1;
        
        while(opt == 0)
        {
            PrintCrewOptions(menu_crew_page);

            opt = Read();

            menu_crew_page = ChooseCrewProcessing(menu_crew_page, opt);
        }
    }
    
    //menu_page = 
    private int ChooseCrewProcessing(int menu_page, int opt)
    {
        if(verifyOption(opt, menu_page))
        {
            if(opt == 0)
            {
                if(menu_page == 1)
                    return 2;
                else 
                    return 1;
            }
            else
            {
                try
                {
                    AddCrewMember(menu_page, opt);
                }
                catch(CrewMemberAlreadyPresentException ex)
                {
                    System.out.println("Crew Member Already Selected");
                }
            }
        }
        
        return 1;
    }
    
    private boolean verifyOption(int opt, int page)
    {
        return ((page == 1 && (opt >= 0 && opt <= 9)) || (page == 2 && (opt >= 0 && opt <= 3)));
    }
    
    private boolean verifyOption(int opt)
    {
        return (opt >= 0 && opt <= 9);
    }
    
    private void PrintAdventureOptions()
    {
        System.out.println("1 - Spawn Aliens");
        System.out.println("2 - Spawn Aliens with Retreat");
        System.out.println("3 - Rest");
        System.out.println("4 - Default Adventure");
        System.out.println("0 - Exit");
    }
    
    private void PrintSpawnAliensMenu()
    {
        System.out.println("1 - 1 Alien");
        System.out.println("2 - 2 Aliens");
        System.out.println("3 - 3 Aliens");
        System.out.println("4 - 4 Aliens");
        System.out.println("5 - 5 Aliens");
        System.out.println("6 - 6 Aliens");
        System.out.println("7 - 7 Aliens");
        System.out.println("8 - 8 Aliens");
        System.out.println("9 - 9 Aliens");
    }
    
    public void ChooseAdventure()
    {
        int opt = -1;
        
        while(opt != 0 && !game.isAdventureSet())
        {
            System.out.println(game.getRoundsAsString());
            PrintAdventureOptions();

            opt = Read();

            if(opt >= 0 && opt <= 4)
            {
                switch(opt)
                {
                    case 1:
                        SpawnAliens(false);
                        break;
                    case 2:
                        SpawnAliens(true);
                        break;
                    case 3:
                        game.addRestingRound();
                    case 4:
                        game.SetDefaultAdventure();
                        break;
                    default:
                        break;
                }
            }
        }
    }
    
    public void SpawnAliens(boolean retreating)
    {
        int opt;
        PrintSpawnAliensMenu();
        
        opt = Read();
        
        if(verifyOption(opt))
        {
            if(retreating)
            {
                game.addSpawnRetreatingAliensRound(opt);
            }
            else
                game.addSpawnAliensRound(opt);
        }
    }
    
    private int Read()
    {
        ChooseText();
        try
        {
            return sc.nextInt();
        }
        catch(InputMismatchException ex)
        {
            sc.next();
            return -1;
        }
    }
    
    private void ChooseText()
    {
        System.out.print("Escolha\n-> ");
    }
    
    public void run()
    {
        while(!quit)
        {
            States state = game.getGameState();
            
            if(state instanceof MainMenu)
            {
                MainMenu();
            }
            else if(state instanceof ChooseCrewState)
            {
                ChooseCrew();
            }
        }
    }
    
    public void MainMenu()
    {
        int opt;
        
        System.out.println(game.getRoundsAsString());
        System.out.println(game.getCrewMembersAsString());
        PrintMainMenu();

        opt = Read();

        switch(opt)
        {
            case 1:
                game.start();
                break;
            case 2:
                //ChooseAdventure();
                break;
            case 3:
                game.ChooseCrew();
                break;
            case 0:
                quit = true;
                break;
            default: break;
        }
    }
}