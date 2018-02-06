package com.andriizastupailo.xyrality.worlds.worldsviewer;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton for store list of worlds
 */

public class WorldBank {
    private static WorldBank sWorldBank;
    private List<World> mWorlds;

    public static WorldBank get(){
        if(sWorldBank == null){
            sWorldBank = new WorldBank();
        }
        return sWorldBank;
    }

    private WorldBank(){
        mWorlds = new ArrayList<>();
    }

    public List<World> getWorlds(){
        return mWorlds;
    }

    public void clearWorlds(){
        mWorlds.clear();
    }

    public void addWorld(World world){
        mWorlds.add(world);
    }
}
