package davilag.es.drawer;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Outline;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private static CharSequence mTitle;
    private String FIRST_TIME_OPEN="davilag.es.drawer.fisrt_open";

    private static String ROW_SELECTED ="es.davilag.row_selected";

    private static RecyclerCustomAdapter RVadapter;
    private static RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);
        boolean firstTime = prefs.getBoolean(FIRST_TIME_OPEN,false);
        if(!firstTime){
            Log.v("TAG","Es la primera vez que me meto en la aplicacion.");
            mNavigationDrawerFragment.openDrawer();
            prefs.edit().putBoolean(FIRST_TIME_OPEN,true).apply();

        }




    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        Fragment fragment = new ContentFragment();
        FragmentManager fm = getFragmentManager();
        Bundle args = new Bundle();
        switch (number) {
            case 1:
                args.putString(ROW_SELECTED,mNavigationDrawerFragment.dataList.get(number-1).getItemName());
                fragment.setArguments(args);
                fm.beginTransaction().replace(R.id.container,fragment).commit();
                break;
            case 2:
                Intent i = new Intent(this,SettingsActivity.class);
                startActivity(i);
                break;
            case 3:
                args.putString(ROW_SELECTED,mNavigationDrawerFragment.dataList.get(number-1).getItemName());
                fragment.setArguments(args);
                fm.beginTransaction().replace(R.id.container,fragment).commit();
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_example) {
            RVadapter.addItem(0,"Nuevo elemento");
            rv.scrollToPosition(0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
    public static class ContentFragment extends Fragment{
        private  class fabOnClickListener implements View.OnClickListener{
            private Context context;
            public fabOnClickListener(Context c){
                this.context = c;
            }

            private Dialog getDialog(){
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();

                builder.setView(inflater.inflate(R.layout.add_dialog,null))
                        .setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                TextView tv = (TextView) getActivity().findViewById(R.id.domain);
                                Toast.makeText(context, tv.getText(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(context,"He pulsado cancelar",Toast.LENGTH_SHORT).show();
                            }
                        });
                return builder.create();
            }
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();

                builder.setView(inflater.inflate(R.layout.add_dialog, null));
                final EditText input = new EditText(getActivity());
                input.setHint("Introduce un dominio");
                input.setTextColor(R.color.black);
                builder.setView(input);
                builder.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, input.getText(), Toast.LENGTH_SHORT).show();
                        RVadapter.addItem(0,input.getText().toString());
                        rv.scrollToPosition(0);
                    }
                })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(context,"He pulsado cancelar",Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.create().show();
            }
        }
        public ContentFragment(){

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.content_fragment, container, false);
            rv = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
            rv.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));
            rv.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            rv.setLayoutManager(mLayoutManager);
            ArrayList<String> contenido = new ArrayList<String>();
            contenido.add(getArguments().getString(ROW_SELECTED));
            for(int i = 0; i<60; i++){
                contenido.add(""+i);
            }
            RVadapter = new RecyclerCustomAdapter(getActivity(),contenido);
            rv.setAdapter(RVadapter);

            //FAB
            int size = getResources().getDimensionPixelSize(R.dimen.fab_size);
            Outline outline = new Outline();
            outline.setOval(0, 0, size, size);
            rootView.findViewById(R.id.fab).setOutline(outline);

            ImageButton buttonAdd = (ImageButton) rootView.findViewById(R.id.fab);
            buttonAdd.setOnClickListener(new fabOnClickListener(rootView.getContext()));
            return  rootView;
        }
    }

    /*
    Cuando se vuelve a la activity de el drawer, por defecto nos ponemos en el primer elemento.
     */
    protected void onRestart(){
        mNavigationDrawerFragment.clickItem(0);
        super.onRestart();
    }


}
