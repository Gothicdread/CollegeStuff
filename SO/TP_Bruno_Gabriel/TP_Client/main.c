#include "client.h"

int main(int argc, char** argv)
{
    ////////////////////////////////////////
    ///// Processamento dos argumentos /////
    ////////////////////////////////////////
    
    char username[MAXNAME];
    
    if(argc != 1)
    {
        char c;
        while((c = getopt(argc, argv, "u:p:")) != -1)
        {
            switch(c)
            {
                case 'u':
                    memcpy(username, optarg, 8*sizeof(char));
                    username[MAXNAME-1] = '\0';
                    break;
                case 'p':
                    break;
                default:
                    fprintf(stderr, "Unknown parameters -%c\n", c);
            }
        }
    }
    
    ///////////////////////////////////////////////////////////////////////
    ///// Preparação das variáveis necessárias para a edição de texto /////
    ///////////////////////////////////////////////////////////////////////
    
    char line[15][46]; //15 linhas, 45 colunas + \0. Valores por defeito até ser implementada comunicação
    char curline[46]; //Linha atual, 45 colunas + \0.
    char preline[46]; //Linha antes de entrar no modo de edição
    char linen[5]; //O numero que precede a linha, exemplo: "(03) ...", com 1 espaço extra para '\0'
    //char lineu[10][MAXNAME]; //15 linhas, 8 colunas. Serve para mostrar o username na linha se estiver a ser editada
    int x, newx, y, newy;
    int c;
    int mode = 0; //Var. para determinar se estamos em modo de edição ou de seleção. 0=Seleção, 1=Edição, 2=Exit
    
    /////////////////////////////////////
    ///// Inicialização das strings /////
    /////////////////////////////////////
    
    for(int y=0; y<15; y++)
    {
        for(int x=0; x<45; x++)
            line[y][x] = ' ';
        line[y][45] = '\0';
    }
    
    ////////////////////////////////////////////////////////////
    ///// Inicio da janela de ncurses para edição de texto /////
    ////////////////////////////////////////////////////////////
    
    initscr();
    noecho();
    keypad(stdscr, TRUE);
    
    for(y=0; y<20; y++)
        for(x=0; x<50; x++)
            mvaddch(y, x, ' ');
    
    for(y=1; y<=9; y++)
    {
        sprintf(linen, "(0%i)", y);
        mvaddstr(y-1, 0, linen);
    }
    for(y=10; y<=15; y++)
    {
        sprintf(linen, "(%i)", y);
        mvaddstr(y-1, 0, linen);
    }
    refresh();

    move(0, 4);
    
    do
    {
        if(mode == 0)
        {
            mode = mvModeLoop();
            
            if(mode == 4)
            {
                getyx(stdscr, y, x);
                for(x=0; x<46; x++)
                {
                    preline[x] = line[y][x];
                }
                mode = 1;
            }
        }
        else if(mode == 1)
        {
            mode = edModeLoop();
            
            if(mode >= 32 && mode <= 126)
            {
                getyx(stdscr, y, x);
                newx = x;
                newy = y;
                line[y][x-4] = mode;
                mode = 1;
                move(newy, newx+1);
            }
            if(mode == 3)
            {
                getyx(stdscr, y, x);
                newx=x;
                x-=4;
                newy=y;
                line[y][45] = ' ';
                for(;x<46; x++)
                {
                    line[y][x-1] = line[y][x];
                }
                line[y][45] = '\0';
                move(newy, newx-1);
                mode = 1;
            }
            else if(mode == 5)
            {
                getyx(stdscr, y, x);
                for(x=0; x<46; x++)
                {
                    line[y][x] = preline[x];
                }
                mode = 0;
            }

            getyx(stdscr, y, x);
            newx=x;
            newy=y;
            for(y=0; y<15; y++)
            {
                for(x=0; x<46; x++)
                {
                    curline[x] = line[y][x];
                }
                mvwaddstr(stdscr, y, 4, curline);
            }
            move(newy, newx);
        }
        refresh();
    }
    while(mode != 2);
    
    endwin();
    return (EXIT_SUCCESS);
}

