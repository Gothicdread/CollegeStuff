#ifndef GAME_H
#define GAME_H

#include "includes.h"
#include "GameData.h"
#include "GameStates/States.h"

class Game
{
    GameData * gData;
    GameState * gState;
    
public:
    
    Game();
    
    void Start();
    void MainGameLoop();
    
    virtual ~Game();
    
private:
    
    template<typename Base, typename T>
    inline bool instanceof(const T *ptr)
    {
        return dynamic_cast<const Base*>(ptr) != nullptr;
    }
};

#endif