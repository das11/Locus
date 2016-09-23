package kdas.i_nterface.uitest_2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Hp-PC on 22-09-2016.
 */
public class Memories_adapter_pager extends FragmentStatePagerAdapter {

    //intiger to count no of tabs
    int tabCount;

    //Constructor to the class
    public Memories_adapter_pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                Fragall all = new Fragall();
                return all;
            case 1:
                Fragwork work = new Fragwork();
                return work;
            case 2:
                Fragpers personal = new Fragpers();
                return personal;
            case 3:
                Fragfami family = new Fragfami();
                return family;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}


