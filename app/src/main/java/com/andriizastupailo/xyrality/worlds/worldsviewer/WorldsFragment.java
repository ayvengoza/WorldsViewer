package com.andriizastupailo.xyrality.worlds.worldsviewer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Handle world items
 */

public class WorldsFragment extends Fragment {
    private RecyclerView mWorldsRecyclerView;
    private WorldAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worlds_list, container, false);
        mWorldsRecyclerView = (RecyclerView) view.findViewById(R.id.worlds_recycler_view);
        mWorldsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        new FetchItemTask().execute();
        updateUI();
        return view;
    }

    private void updateUI(){
        List<World> worlds = new ArrayList<>();
        for(int i=0; i<20; i++){
            worlds.add(new World());
        }
        mAdapter = new WorldAdapter(worlds);
        mWorldsRecyclerView.setAdapter(mAdapter);
    }

    private class WorldHolder extends RecyclerView.ViewHolder{

        public WorldHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_world, parent, false));
        }
    }

    private class WorldAdapter extends RecyclerView.Adapter<WorldHolder>{
        private List<World> mWorlds;

        public WorldAdapter(List<World> worlds){
            mWorlds = worlds;
        }

        @Override
        public WorldHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new WorldHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(WorldHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return mWorlds.size();
        }
    }

    private class FetchItemTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            new WorldsFetchr().fetchItems();
            return null;
        }
    }
}
