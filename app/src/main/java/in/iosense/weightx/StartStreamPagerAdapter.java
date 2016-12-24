package in.iosense.weightx;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import in.iosense.Fragments.weightx.InfoInputFragment;
import in.iosense.Fragments.weightx.InfoNfcFragment;
import in.iosense.Fragments.weightx.InfoQrCodeFragment;

public class StartStreamPagerAdapter extends FragmentPagerAdapter {

    public StartStreamPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new InfoInputFragment();
            case 1:
                return new InfoQrCodeFragment();
            case 2:
                return new InfoNfcFragment();
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
