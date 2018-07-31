package com.coresun.powerbank.base;

import android.support.v4.app.Fragment;

public class AdvBaseFragment extends Fragment {
   private int TYPE=1;

    public int getTYPE() {
        return TYPE;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }
}
