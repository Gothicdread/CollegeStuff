#include "Player.h"

Player::Player() {}

Player::Player(const Player& orig) {}

Player::Player(string n, int m, Harbor* h):name(n), money(m)
{
    mainharbor = h;
}

int Player::buyShip(int type)
{
    int x, y, i, id;
    istringstream is;
    is.str(mainharbor->getCoord());
    is >> y;
    is >> x;
    
    for(i=0; i<fleet.size(); i++)
    {
        if(i+1 != fleet[i]->getID())
            break;
    }
    
    id = i + 1;
    auto it = fleet.begin() + i;
    
    switch(type)
    {
	case 1:
            fleet.insert(it, new Sailboat(this, id, y, x));
	    return 0;
	case 2:
            fleet.insert(it, new Galeon(this, id, y, x));
	    return 0;
	case 3:
            fleet.insert(it, new Schooner(this, id, y, x));
	    return 0;
	case 4:
            fleet.insert(it, new Frigate(this, id, y, x));
	    return 0;
	case 5:
            fleet.insert(it, new Special(this, id, y, x));
	    return 0;
	default:
	    return -1;
    }
}

int Player::sellShip(int id)
{
    for(int i=0; i<fleet.size(); i++)
    {
        if(fleet[i]->getID() == id)
        {
            auto it = fleet.begin() + i;
            delete fleet[i];
            fleet.erase(it);
            return 0;
        }
    }

    return -1;
}

int Player::spawnship(int type, int x, int y)
{
    int id, i;
    
    for(i=0; i<fleet.size(); i++)
    {
        if(i+1 != fleet[i]->getID())
        {
            id = i + 1;
            break;
        }
    }
    
    auto it = fleet.begin() + i;
    
    if(type == 1)
    {
	fleet.insert(it, new Sailboat(this, id, x, y));
	return 0;
    }
    else if(type == 4)
    {
	fleet.insert(it, new Frigate(this, id, x, y));
	return 0;
    }
    else
	return -1;
	
}

const int Player::getCrew(int id) const
{
    for(auto& it: fleet)
        if(it->getID() == id)
            return it->getCrew();

    return -1;
}

const int Player::getCargo(int id) const
{
    for(auto& it: fleet)
        if(it->getID() == id)
            return it->getCargo();

    return -1;
}

const int Player::getFish(int id) const
{
    for(auto& it: fleet)
        if(it->getID() == id)
            return it->getFish();

    return -1;
}

const string Player::getShipCoord(int id) const
{
    for(auto& it: fleet)
    {
        if(it->getID() == id)
            return it->getCoord();
    }
    
    return "";
}

const string Player::getShipDestCoord(int id) const
{
    for(auto& it: fleet)
    {
        if(it->getID() == id)
        {
            return it->getDestCoord();
        }
    }
    
    return "";
}

const string Player::getShipInfo(int id) const
{
    for(auto& it: fleet)
    {
        if(it->getID() == id)
            return it->getShipInfo();
    }
}

const bool Player::getShipInHarbor(int id) const
{
    //Iterator to look for a ship with matching ID numbers. If found, returns it's state.
    for(auto& it: fleet)
        if(it->getID() == id)
            return it->isInHarbor();
    
    //Even if no ship exists with that ID, we don't want the map to draw non-existant data, so just give back "true"
    return true;    
}

const int Player::getShipType(int id) const
{
    for(auto& it: fleet)
        if(it->getID() == id)
            return it->getType();
    
    //If a ship with that ID number doesn't exist, return the general error value of -1
    return -1;        
}

const int Player::getShipID(int i) const
{
    if(i < fleet.size())
        return fleet[i]->getID();
}

int Player::ShipSetDestination(int id, string destCoord, bool isDestHarbor)
{
    for(auto& it: fleet)
    {
        if(it->getID() == id)
        {
            istringstream is;
            int y, x;
            
            is.str(destCoord);
            
            is >> y;
            is >> x;
            
            it->setDestination(y, x, isDestHarbor);
        }
    }
}

int Player::ShipMove(int id, int yy, int xx)
{
    for(auto& it: fleet)
    {
        if(it->getID() == id)
        {
            it->move(yy, xx);
            return 0;
        }
    }
    
    return -1;
}

Player::~Player()
{
    for(auto& it: fleet)
    {
        delete it;
        it = nullptr;
    }
}