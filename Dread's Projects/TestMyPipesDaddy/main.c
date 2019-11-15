#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <ctype.h>

int main(int argc, char** argv)
{
    pid_t child_pid;
    int fd_in[2], fd_out[2];
    char * msg = "teste";    
    char resposta[50] = { 0 };
    int num;
    
    pipe(fd_in); // filho pra pai
    pipe(fd_out); // pai para filho
    
    child_pid = fork();
    
    /////////////////
    ///PIPE HANDLE///
    /////////////////
    
    if(child_pid != 0)  //father
    {
        close(fd_in[1]); // escrita off
        close(fd_out[1]); // leitura off
    }
    else    //child
    {
        close(fd_in[0]);
        close(fd_out[0]);
        
        close(STDIN_FILENO);
        dup(fd_in[1]);
        close(fd_in[1]);
        
        close(STDOUT_FILENO);
        dup(fd_out[1]);
        close(fd_out[1]);
    }
    
    //////////////////////
    ///READ\WRITE PIPES///
    //////////////////////
    
    if(child_pid != 0)  //father
    {
        printf("FATHER: Message to son: ");
        fgets(resposta, 49, stdin);
        resposta[strlen(resposta) - 1] = '\0';
        write(fd_in[0], resposta, strlen(resposta)); //escreve "teste" para o filho
        
        read(fd_out[0], resposta, 49);  // 6 fixo para atamancar o teste
        printf("\nrecebi [%s]\n",resposta);
    }
    else    //child
    {
        //num = read(fd_in[1],resposta,49);
        //printf("FILHO: %s\n", resposta);
        
        //printf("Resposta para o pai: ");
        //fgets(resposta, 50, stdin);
        
        //resposta[strlen(resposta) - 1] = '\0';
        
        //write(fd_out[1], resposta, strlen(resposta));
        
        num = read(STDIN_FILENO, resposta, 49);
        
        //char father_message[15] = "Hello Dad!\0";
        
        /*num = strlen(resposta);
        
        resposta[num] = ' ';
        resposta[num + 1] = '1';
        resposta[num + 2] = '2';
        resposta[num + 3] = '3';
        resposta[num + 4] = '4';
        resposta[num + 5] = '\0';*/
        
        char son_resposta[50] = "I have recieved your message father!\0";
        
        
        write(STDOUT_FILENO, son_resposta, strlen(son_resposta));
    }
    
    exit(0);
}