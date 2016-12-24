package in.iosense.weightx;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.HashMap;

import in.iosense.Fragments.weightx.NfcReaderFragment;
import in.iosense.Fragments.weightx.QrCodeScannerFragment;
import in.iosense.Fragments.weightx.TypeIpFragment;

public class ViewStreamPagerAdapter extends FragmentPagerAdapter {

    public ViewStreamPagerAdapter(FragmentManager fm){
        super(fm);
    }
    private HashMap<Integer, Fragment> fragmentHashMap = new HashMap<>();

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new TypeIpFragment();
            case 1:
                return new QrCodeScannerFragment();
            case 2:
                if (fragmentHashMap.get(position) != null) {
                    return fragmentHashMap.get(position);
                }
                NfcReaderFragment nfcReaderFragment = new NfcReaderFragment();
                fragmentHashMap.put(position, nfcReaderFragment);
                return nfcReaderFragment;
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
