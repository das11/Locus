package kdas.i_nterface.uitest_2;

//Link the memories button to this page, then intitally itself it should fetch
// the calander view based on the tab and not inside the day

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class Memories extends AppCompatActivity {

    private TabLayout mtabLayout;       //declaring the tab bar with options all|friends|work|family
    private ViewPager mviewPager;       // declaring the pager to slide among the above tabs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memories);
        getSupportActionBar().hide();

        mtabLayout = (TabLayout) findViewById(R.id.tabselector);    // initializing the tab bar
        //adding the tabs using addTab() method
        mtabLayout.addTab(mtabLayout.newTab().setText("All"));
        mtabLayout.addTab(mtabLayout.newTab().setText("Work"));
        mtabLayout.addTab(mtabLayout.newTab().setText("Personal"));
        mtabLayout.addTab(mtabLayout.newTab().setText("Family"));

        //mtabLayout.setupWithViewPager(mviewPager);
        mviewPager = (ViewPager) findViewById(R.id.meetslide);//initializing the viewpager
        Memories_adapter_pager adapter = new Memories_adapter_pager(getSupportFragmentManager(), mtabLayout.getTabCount());
        mviewPager.setAdapter(adapter);        //linking the viewpager adapter

        mviewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mtabLayout));
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
        });
    }
}


