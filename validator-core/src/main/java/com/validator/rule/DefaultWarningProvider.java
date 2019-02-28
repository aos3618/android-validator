package com.validator.rule;

import com.validator.Utils;

/**
 * Created by AoS on 2019/2/25 0025.
 */

public class DefaultWarningProvider implements IWarningProvider {
    @Override
    public void show(String s) {
        Utils.Log(s);
    }
}
