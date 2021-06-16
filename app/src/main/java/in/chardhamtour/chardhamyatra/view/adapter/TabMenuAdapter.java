package in.chardhamtour.chardhamyatra.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.chardhamtour.chardhamyatra.R;

public class TabMenuAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> allFragments;
    private TextView[] tabs;
    private  List<String> allTitles=new ArrayList<>();
    private List<Integer> allIcons=new ArrayList<>();
   
    public TabMenuAdapter(FragmentManager fm) {
        super(fm);
    }


    public void addValues(List<Fragment> frag){
        allFragments=frag;
    }
    public void modifyTabs(Context ctx, String[] tabsName, int[] drawable, TabLayout tabLayout){
        tabs=new TextView[tabsName.length];
        for (int i=0;i<tabsName.length;i++){
            tabs[i]=(TextView) LayoutInflater.from(ctx).inflate(R.layout.custom_tab, null);
            tabs[i].setText(tabsName[i]);
            tabs[i].setCompoundDrawablesWithIntrinsicBounds(0, drawable[i], 0, 0);
            Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(tabs[i]);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return allFragments.get(position);
    }

    @Override
    public int getCount() {
        return allFragments.size();
    }

    public CharSequence getPageTitle(int position){
        return "";
    }

}
