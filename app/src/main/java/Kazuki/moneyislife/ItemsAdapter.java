package Kazuki.moneyislife;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> data = new ArrayList<>();
    private ItemsAdapterListener listener = null;

    public void setListener(ItemsAdapterListener listener) {
        this.listener = listener;
    }

    public void setData(List<Item> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addItem(Item item) {
        data.add(item);
        notifyItemInserted(data.size());
    }

    @NonNull
    @Override
    public ItemsAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter.ItemViewHolder viewHolder, int position) {
        Item record = data.get(position);
        viewHolder.applyData(record, position, listener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView price;
        private final TextView title;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
        }

        public void applyData(final Item item, final int position, final ItemsAdapterListener listener) {
            title.setText(item.name);
            price.setText(item.price);

            itemView.setOnClickListener((View v) -> {
                if (listener != null) {
                    listener.onItemClick(item, position);
                }
            });
            itemView.setOnLongClickListener((View v) -> {
                if (listener != null) {
                    listener.onItemLongClick(item, position);
                }
                return true;
            });
        }
    }
}

