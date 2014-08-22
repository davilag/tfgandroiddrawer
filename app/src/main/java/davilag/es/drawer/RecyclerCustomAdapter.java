package davilag.es.drawer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu;

import java.util.ArrayList;

/**
 * Created by davilag on 18/08/14.
 */
public class RecyclerCustomAdapter extends RecyclerView.Adapter<RecyclerCustomAdapter.ViewHolder> implements View.OnClickListener,
        View.OnLongClickListener{

    private ArrayList<String> mDataset;
    private static Context sContext;

    // Adapter's Constructor
    public RecyclerCustomAdapter(Context context, ArrayList<String> myDataset) {
        mDataset = myDataset;
        sContext = context;
    }

    // Create new views. This is invoked by the layout manager.
    @Override
    public RecyclerCustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // Create a new view by inflating the row item xml.
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);

        // Set the view to the ViewHolder
        ViewHolder holder = new ViewHolder(v);
        holder.row.setOnClickListener(RecyclerCustomAdapter.this);
        holder.row.setOnLongClickListener(RecyclerCustomAdapter.this);

        holder.row.setTag(holder);

        holder.menuButton.setOnClickListener(new onMenuClickListener(parent.getContext()));

        return holder;
    }

    // Replace the contents of a view. This is invoked by the layout manager.
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // Get element from your dataset at this position and set the text for the specified element
        holder.mNameTextView.setText(mDataset.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Implement OnClick listener. The clicked item text is displayed in a Toast message.
    @Override
    public void onClick(View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (view.getId() == holder.row.getId()) {
            Toast.makeText(sContext, holder.mNameTextView.getText(), Toast.LENGTH_SHORT).show();
        }
    }

    // Implement OnLongClick listener. Long Clicked items is removed from list.
    @Override
    public boolean onLongClick(View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        Log.v("Adapter",holder.row.getId()+"");
        Log.v("Adapter",view.getId()+"");
        if (view.getId() == holder.row.getId()) {
            mDataset.remove(holder.getPosition());

            // Call this method to refresh the list and display the "updated" list
            notifyDataSetChanged();

            Toast.makeText(sContext, "Item " + holder.mNameTextView.getText() + " has been removed from list",
                    Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    // Create the ViewHolder class to keep references to your views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout row;
        public TextView mNameTextView;
        public ImageButton menuButton;

        /**
         * Constructor
         * @param v The container view which holds the elements from the row item xml
         */
        public ViewHolder(View v) {
            super(v);
            row = (LinearLayout) v.findViewById(R.id.row);
            mNameTextView = (TextView) v.findViewById(R.id.nombre_row);
            menuButton = (ImageButton) v.findViewById(R.id.menu_button);
        }
    }

    public void addItem(int position, String content){
        mDataset.add(position, content);
        notifyItemInserted(position);
    }

    private class onMenuClickListener implements View.OnClickListener{
        Context context;
        public onMenuClickListener(Context c){
            this.context = c;
        }
        @Override
        public void onClick(View view) {
            PopupMenu popup = new PopupMenu(context, view);
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.action_example:
                            Toast.makeText(context,"Pulsado action example",Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(context,"He pulsado algo",Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.main, popup.getMenu());
            popup.show();
        }
    }
}
