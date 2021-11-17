package com.pierre44.go4lunch.ui.listWorkmates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pierre44.go4lunch.R;
import com.pierre44.go4lunch.models.Workmate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pmeignen on 22/10/2021.
 */
public class ListViewWorkmatesAdaptor extends RecyclerView.Adapter<ListViewWorkmatesAdaptor.WorkmatesViewHolder> {

    private List<Workmate> workmates = new ArrayList<>();
    private final Context context;
    private final boolean isUsingInWorkmatesFragment;

    public ListViewWorkmatesAdaptor(Context context, boolean isUsingInWorkmatesFragment) {
        this.context = context;
        this.isUsingInWorkmatesFragment = isUsingInWorkmatesFragment;
    }

    @NonNull
    @Override
    public WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_workmate, parent, false);
        return new WorkmatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesViewHolder holder, int position) {
        holder.bind(context, workmates.get(position), isUsingInWorkmatesFragment);
    }

    @Override
    public int getItemCount() {
        return workmates.size();
    }

    protected static class WorkmatesViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout item;
        private final ImageView icon;
        private final TextView text;
        private boolean isUsingInWorkmatesFragment;

        private Workmate workmate;

        public WorkmatesViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.item_workmate_icon);
            text = itemView.findViewById(R.id.item_workmate_text);
            item = itemView.findViewById(R.id.item_workmate_item);
        }

        public void bind(Context context, final Workmate workmate, boolean isUsingInWorkmatesFragment) {
            this.workmate = workmate;
            this.isUsingInWorkmatesFragment = isUsingInWorkmatesFragment;
            text.setText(workmate.getNameWorkmate());
        }
    }
}
