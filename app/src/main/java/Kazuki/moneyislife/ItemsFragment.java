package Kazuki.moneyislife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import Kazuki.moneyislife.api.AddItemResult;
import Kazuki.moneyislife.api.Api;
import Kazuki.moneyislife.api.App;
import Kazuki.moneyislife.api.RemoveItemResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemsFragment extends Fragment {

    private static final String TYPE_KEY = "type";
    public static final int ADD_ITEM_REQUEST_CODE = 123;

    private String type;
    private RecyclerView recycler;
    private SwipeRefreshLayout refresh;
    private ItemsAdapter adapter;
    private Api api;
    private App app;
    private ActionMode actionMode = null;

    public static ItemsFragment createItemsFragment(String type) {
        ItemsFragment fragment = new ItemsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ItemsFragment.TYPE_KEY, type);
        bundle.putBoolean("key", true);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemsAdapter();
        adapter.setListener(new AdapterListener());
        Bundle bundle = getArguments();
        type = Objects.requireNonNull(bundle).getString(TYPE_KEY, Item.TYPE_EXPENSES);
        if (type.equals(Item.TYPE_UNKNOWN)) {
            throw new IllegalArgumentException("Unknown type");
        }
        app = (App) Objects.requireNonNull(getActivity()).getApplication();
        api = app.getApi();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_items, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler = view.findViewById(R.id.list);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);

        refresh = view.findViewById(R.id.refresh);
        refresh.setColorSchemeColors(getResources().getColor(R.color.sec_yellow));
        refresh.setOnRefreshListener(this::loadItems);
        loadItems();
    }

    private void loadItems() {
        Call<List<Item>> call = api.getItems(type);
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(@NotNull Call<List<Item>> call, @NotNull Response<List<Item>> response) {
                adapter.setData(response.body());
                refresh.setRefreshing(false);
            }

            @Override
            public void onFailure(@NotNull Call<List<Item>> call, @NotNull Throwable t) {
                refresh.setRefreshing(false);
            }
        });
    }

    private void addItem(final Item item) {
        Call<AddItemResult> call = api.addItem(item.getPrice(), item.getName(), item.getType());
        call.enqueue(new Callback<AddItemResult>() {
            @Override
            public void onResponse(@NotNull Call<AddItemResult> call, @NotNull Response<AddItemResult> response) {
                AddItemResult result = response.body();
                if (Objects.requireNonNull(result).getStatus().equals("success")) {
                    item.setId(result.getId());
                    adapter.addItem(item);
                }
            }

            @Override
            public void onFailure(@NotNull Call<AddItemResult> call, @NotNull Throwable t) {
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_ITEM_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Item item = data.getParcelableExtra("item");
            if (item.getType().equals(type)) {
                addItem(item);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void removeSelectedItems() {
        for (int i = adapter.getSelectedItems().size() - 1; i >= 0; i--) {
            Item item = adapter.remove(adapter.getSelectedItems().get(i));
            Call<RemoveItemResult> call = api.removeItem(item.getId());
            call.enqueue(new Callback<RemoveItemResult>() {
                @Override
                public void onResponse(@NotNull Call<RemoveItemResult> call, @NotNull Response<RemoveItemResult> response) {
                }

                @Override
                public void onFailure(@NotNull Call<RemoveItemResult> call, @NotNull Throwable t) {
                }
            });
        }
        actionMode.finish();
    }

    private class AdapterListener implements ItemsAdapterListener {

        @Override
        public void onItemClick(Item item, int position) {
            if (isInActionMode()) {
                toggleSelection(position);
            }
        }

        @Override
        public void onItemLongClick(Item item, int position) {
            if (isInActionMode()) {
                return;
            }
            actionMode = ((AppCompatActivity) Objects.requireNonNull(getActivity())).startSupportActionMode(actionModeCallback);
            toggleSelection(position);
        }

        private boolean isInActionMode() {
            return actionMode != null;
        }

        private void toggleSelection(int position) {
            adapter.toggleSelection(position);
        }
    }

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = new MenuInflater(getContext());
            inflater.inflate(R.menu.items_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.remove)
                showDialog();
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelections();
            actionMode = null;
        }
    };

    private void showDialog() {
        DialogFragmentConfirm dialog = new DialogFragmentConfirm();
        dialog.show(Objects.requireNonNull(getFragmentManager()), "ConfirmationDialog");
        dialog.setDialogListener(new DialogListener() {
            @Override
            public void positiveClick() {
                removeSelectedItems();
            }

            @Override
            public void negativeClick() {
                actionMode.finish();
            }
        });
    }
}


