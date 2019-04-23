package Kazuki.moneyislife;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager pager;
    private TabLayout tabLayout;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tabLayout);
        pager = findViewById(R.id.viewPager);

        MainPagesAdapter adapter = new MainPagesAdapter(getSupportFragmentManager(), this);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(this);
        tabLayout.setupWithViewPager(pager);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener((View v) -> {

            int currentPage = pager.getCurrentItem();
            String type = null;

            if (currentPage == MainPagesAdapter.PAGE_INCOMES)
                type = Item.TYPE_INCOMES;
            else if (currentPage == MainPagesAdapter.PAGE_EXPENSES)
                type = Item.TYPE_EXPENSES;
            Intent intent = new Intent(MainActivity.this, AddAct.class);
            intent.putExtra(AddAct.TYPE_KEY, type);
            startActivityForResult(intent, ItemsFragment.ADD_ITEM_CODE);
        });


    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        switch (i) {
            case MainPagesAdapter.PAGE_INCOMES:
            case MainPagesAdapter.PAGE_EXPENSES:
                fab.show();
                break;
            case MainPagesAdapter.PAGE_BALANCE:
                fab.hide();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

        switch (i) {
            case ViewPager.SCROLL_STATE_IDLE:
                fab.setEnabled(true);
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:
            case ViewPager.SCROLL_STATE_SETTLING:
                fab.setEnabled(false);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
