package classExample.BitmapPinch;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MainActivity extends Activity {

    SimpleFragment mSimpleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // without this check, we would create a new fragment at each orientation change!
        if (null == savedInstanceState)
            createFragment();
    }

    private void createFragment()
    {
        FragmentManager fManager = getFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();

        mSimpleFragment = new SimpleFragment();

        // Adding the new fragment
        fTransaction.add(R.id.mainContainer, mSimpleFragment);
        fTransaction.commit();
    }

}
