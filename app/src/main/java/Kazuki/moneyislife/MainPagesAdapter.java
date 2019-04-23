package Kazuki.moneyislife;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainPagesAdapter extends FragmentPagerAdapter {

    public final static int PAGE_INCOMES = 0;
    public final static int PAGE_EXPENSES = 1;
    public final static int PAGE_BALANCE = 2;
    private String [] titles;

    public MainPagesAdapter(FragmentManager fm, Context context) {
        super(fm);
        titles = context.getResources().getStringArray(R.array.title_items);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case PAGE_INCOMES:
                return ItemsFragment.createItemsFragment(Item.TYPE_INCOMES);
            case PAGE_EXPENSES:
                return ItemsFragment.createItemsFragment(Item.TYPE_EXPENSES);
            case PAGE_BALANCE:
                return new BalanceFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
