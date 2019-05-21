package TextUI;

import GameLogic.Exceptions.*;
import java.util.Scanner;
import GameLogic.Game;

public class TextMain
{
    Scanner sc;
    Printer pr;
    Game game;
    
    int opt;
    
    boolean quitting;
    
    private class Printer
    {
        public Printer() {}
        
        public void PrintMenuInfo()
        {
            System.out.println("Crew: " + game.getCrewMembers());
            System.out.println("Adventure: ");
            System.out.println("");
        }
        
        public void PrintMainMenu()
        {
            PrintMenuInfo();
            System.out.println("1 - Start Game");
            System.out.println("2 - Select Crew");
            System.out.println("3 - Choose Adventure");
            System.out.println("0 - Quit Game");
        }
        
        public void PrintCrewMenu(int page)
        {
            if(page == 1)
            {
                System.out.println("1 - Captain");
                System.out.println("2 - Commander");
                System.out.println("3 - Communications Officer");
                System.out.println("4 - Doctor");
                System.out.println("5 - Engineer");
                System.out.println("6 - Moral Officer");
                System.out.println("7 - Navigation Officer");
                System.out.println("8 - Red Shirt");
                System.out.println("9 - Next Page");
                System.out.println("0 - Back to Main Menu...");
            }
            else if(page == 2)
            {
                System.out.println("1 - Science Officer");
                System.out.println("2 - Security Officer");
                System.out.println("3 - Shuttle Pilot");
                System.out.println("4 - Transporter Chief");
                System.out.println("9 - Previous Page");
                System.out.println("0 - Back to Main Menu...");
            }
        }
    }
    
    public TextMain()
    {
        sc = new Scanner(System.in);
        pr = new Printer();
        game = new Game();
        quitting = false;
    }
    
    private void MainMenu()
    {
        pr.PrintMainMenu();
        opt = sc.nextInt();
        
        switch(opt)
        {
            case 1:
                game.StartGame();
                break;
            case 2:
                game.ChooseCrew();
                break;
            case 3:
                //game.ChooseAdventure();
                break;
            case 0:
                quitting = true;
                break;
            default:
                break;
        }
    }
    
    private void ChooseCrewMenu()
    {
        int page = 1;

        do
        {
            pr.PrintCrewMenu(page);
            
            opt = sc.nextInt();
            
            if(opt == 9)
                page = (page == 1) ? 2 : 1;
        }
        while(opt == 9);
        
        try
        {
            if(page == 1)
            {
                switch(opt)
                {
                    case 1:
                        game.ChooseCrewMember("Captain");
                        break;
                    case 2:
                        game.ChooseCrewMember("Commander");
                        break;
                    case 3:
                        game.ChooseCrewMember("CommsOfficer");
                        break;
                    case 4:
                        game.ChooseCrewMember("Doctor");
                        break;
                    case 5:
                        game.ChooseCrewMember("Engineer");
                        break;
                    case 6:
                        game.ChooseCrewMember("MoralOfficer");
                        break;
                    case 7:
                        game.ChooseCrewMember("NavigationOfficer");
                        break;
                    case 8:
                        game.ChooseCrewMember("RedShirt");
                        break;
                    default:
                        break;
                }
            }
            else if(page == 2)
            {
                switch(opt)
                {
                    case 1:
                        game.ChooseCrewMember("ScienceOfficer");
                        break;
                    case 2:
                        game.ChooseCrewMember("SecurityOfficer");
                        break;
                    case 3:
                        game.ChooseCrewMember("ShuttlePilot");
                        break;
                    case 4:
                        game.ChooseCrewMember("TransportChief");
                        break;
                    default:
                        break;
                }
            }
        }
        catch(InvalidCrewMemberException ex)
        {
            System.out.println("Trying to be smart eh? No idea how you did it, but no custom strings allowed.");
        }
        catch(CrewMemberAlreadySelectedException ex)
        {
            System.out.println("You're already selected that crew member.");
        }
    }
    
    public void run()
    {
        while(!quitting)
        {
            if(game.getState().equalsIgnoreCase("MainMenu"))
                MainMenu();
            else if(game.getState().equalsIgnoreCase("ChooseCrew"))
                ChooseCrewMenu();
        }
    }
}