package com.andriizastupailo.xyrality.worlds.worldsviewer;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WorldsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        if(PreferenceStore.isUserSet(this)){
            return new WorldsFragment();
        } else {
            return new LoginFragment();
        }
    }
}
