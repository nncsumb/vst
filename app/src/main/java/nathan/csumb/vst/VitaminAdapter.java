package nathan.csumb.vst;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VitaminAdapter extends RecyclerView.Adapter<VitaminAdapter.VitaminViewHolder> {
    private final List<Vitamin> vitamins;
    private final Context context;
    private final OnVitaminClickListener onVitaminClickListener;

    public VitaminAdapter(Context context, List<Vitamin> vitamins, OnVitaminClickListener onVitaminClickListener) {
        this.context = context;
        this.vitamins = vitamins;
        this.onVitaminClickListener = onVitaminClickListener;
    }

    @NonNull
    @Override
    public VitaminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vitamin_item, parent, false);
        return new VitaminViewHolder(view, onVitaminClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull VitaminViewHolder holder, int position) {
        Vitamin vitamin = vitamins.get(position);
        SpannableString spannableName = new SpannableString("Name: " + vitamin.getName());
        spannableName.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.vitaminName.setText(spannableName);
        SpannableString spannableDescription = new SpannableString("Description: " + vitamin.getDescription());
        spannableDescription.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.vitaminDescription.setText(spannableDescription);
        holder.vitaminQuantity.setText(String.valueOf(vitamin.getQuantity()));
        holder.vitaminQuantityLabel.setText("QTY");
        if (vitamin.getTime().equals("Morning")) {
            holder.vitaminTime.setText("☀️");
        } else if (vitamin.getTime().equals("Night")) {
            holder.vitaminTime.setText("🌙");
        } else {
            holder.vitaminTime.setText("🌇");
        }
    }

    @Override
    public int getItemCount() {
        return vitamins.size();
    }

    public interface OnVitaminClickListener {
        void onVitaminClick(int position);
    }

    public static class VitaminViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView vitaminDescription;
        TextView vitaminName;
        TextView vitaminQuantity;
        TextView vitaminQuantityLabel;
        TextView vitaminTime;
        OnVitaminClickListener onVitaminClickListener;

        public VitaminViewHolder(@NonNull View itemView, OnVitaminClickListener onVitaminClickListener) {
            super(itemView);
            vitaminName = itemView.findViewById(R.id.vitaminName);
            vitaminDescription = itemView.findViewById(R.id.vitaminDescription);
            vitaminQuantity = itemView.findViewById(R.id.vitaminQuantity);
            vitaminQuantityLabel = itemView.findViewById(R.id.vitaminQuantityLabel);
            vitaminTime = itemView.findViewById(R.id.vitaminTime);
            this.onVitaminClickListener = onVitaminClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onVitaminClickListener.onVitaminClick(getAdapterPosition());
        }
    }
}




