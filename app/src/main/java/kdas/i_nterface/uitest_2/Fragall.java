package kdas.i_nterface.uitest_2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Hp-PC on 18-09-2016.
 */
public class Fragall extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //linking the layout file fragall (containing all list
        return inflater.inflate(R.layout.fragall,container,false);
    }
}
