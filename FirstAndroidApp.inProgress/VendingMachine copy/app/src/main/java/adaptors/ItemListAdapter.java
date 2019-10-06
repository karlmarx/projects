package adaptors;

import android.app.LauncherActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vendingmachine.Item;
import com.example.vendingmachine.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemListViewHolder> {
    List<Item> listItems;
    public ItemListAdapter(List<Item> _listItems) {
        listItems = _listItems;
    }

    @NonNull
    @Override
    public ItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_layout, parent, false);
        ItemListViewHolder itemListViewHolder = new ItemListViewHolder(viewItem);
        return itemListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder holder, int position) {
        holder.textViewItemList.setText(listItems.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ItemListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cardViewItemList)
        CardView cardViewItemList;

        @BindView(R.id.textViewItemList)
        TextView textViewItemList;
        ItemListViewHolder(View viewItem) {
            super(viewItem);
            ButterKnife.bind(viewItem);

        }
    }
}
