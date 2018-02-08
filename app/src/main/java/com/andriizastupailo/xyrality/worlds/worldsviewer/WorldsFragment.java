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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worlds_list, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.toolbar_title);
        mWorldsRecyclerView = (RecyclerView) view.findViewById(R.id.worlds_recycler_view);
        mWorldsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sendRequest();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.worlds_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.log_out_menu:
                moveToLoginFragment();
                return true;
            default:
                return false;
        }
    }

    private void updateUI(){
        List<World> worlds = WorldBank.get().getWorlds();
        mAdapter = new WorldAdapter(worlds);
        mWorldsRecyclerView.setAdapter(mAdapter);
    }

    private void sendRequest(){
        String login = PreferenceStore.getLogin(getActivity());
        String password = PreferenceStore.getPassword(getActivity());
        new FetchItemTask().execute(login, password);
    }

    private void moveToLoginFragment(){
        PreferenceStore.cleanUser(getActivity());
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.fragment_container, new LoginFragment())
                .commit();
    }

    private class WorldHolder extends RecyclerView.ViewHolder{
        private TextView mNameTextView;
        private TextView mInfoTextView;
        private TextView mOnlineTextView;
        private World mWorld;

        public WorldHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_world, parent, false));
            mNameTextView = (TextView) itemView.findViewById(R.id.world_name);
            mInfoTextView = (TextView) itemView.findViewById(R.id.world_info);
            mOnlineTextView = (TextView) itemView.findViewById(R.id.online_text_view);
        }

        public void bind(World world){
            mWorld = world;
            mNameTextView.setText(mWorld.getName());
            mInfoTextView.setText(mWorld.getCountry());
            mOnlineTextView.setText(mWorld.getDescription());
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

    private class FetchItemTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            return new WorldsFetchr().fetchItems(strings[0], strings[1]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                updateUI();
            } else {
                moveToLoginFragment();
            }
        }
    }
}
