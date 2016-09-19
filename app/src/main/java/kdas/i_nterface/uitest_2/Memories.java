package kdas.i_nterface.uitest_2;

//Link the memories button to this page, then intitally itself it should fetch
// the calander view based on the tab and not inside the day

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class Memories extends AppCompatActivity {

    TabLayout mtabLayout;       //declaring the tab bar with options all|friends|work|family
    ViewPager mviewPager;       // declaring the pager to slide among the above tabs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memories);

        mviewPager = (ViewPager) findViewById(R.id.meetslide);      //initializing the viewpager
        mviewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(), getApplicationContext()));

        mtabLayout = (TabLayout) findViewById(R.id.tabselector);    // initializing the tab bar
        mtabLayout.setupWithViewPager(mviewPager);

        mtabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //what to do when tab is selected or basically linking the tab to the view pager
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mviewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                mviewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                mviewPager.setCurrentItem(tab.getPosition());
            }
        }
        );


    }
    //defining all the pages to be shown while sliding
    private class CustomAdapter extends FragmentPagerAdapter {
        private String fragments [] ={"Fragment 1","Fragment 2","Fragment 3", "Fragment 4"};

        public CustomAdapter(FragmentManager supportFragmentManager, Context applicationContext) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new Fragall(); //shows a calander with all events
                case 1:
                    return new Fragpers(); //shows a calander view only with personal/friends
                case 3:
                    return new Fragwork();  //shows a calander view only related to work
                case 4:
                    return new Fragfami();  //shows a calander view only with family
                default:
                    return null;
            }
        }

        public int getCount() {

            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return fragments[position];
        }
    }
}



