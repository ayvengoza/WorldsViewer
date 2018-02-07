package com.andriizastupailo.xyrality.worlds.worldsviewer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Handle world items
 */

public class WorldsFragment extends Fragment {
    private RecyclerView mWorldsRecyclerView;
    private WorldAdapter mAdapter;
    private Toolbar mToolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worlds_list, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.toolbar_title);
        mWorldsRecyclerView = (RecyclerView) view.findViewById(R.id.worlds_recycler_view);
        mWorldsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new FetchItemTask().execute();
    }

    private void updateUI(){
        List<World> worlds = WorldBank.get().getWorlds();
        mAdapter = new WorldAdapter(worlds);
        mWorldsRecyclerView.setAdapter(mAdapter);
    }

    private class WorldHolder extends RecyclerView.ViewHolder{
        private TextView mNameTextView;
        private TextView mInfoTextView;
        private World mWorld;

        public WorldHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_world, parent, false));
            mNameTextView = (TextView) itemView.findViewById(R.id.world_name);
            mInfoTextView = (TextView) itemView.findViewById(R.id.world_info);
        }

        public void bind(World world){
            mWorld = world;
            mNameTextView.setText(mWorld.getName());
            mInfoTextView.setText(mWorld.getCountry());
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
            World world = mWorlds.get(position);
            holder.bind(world);
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

        @Override
        protected void onPostExecute(Void aVoid) {
            updateUI();
        }
    }
}
