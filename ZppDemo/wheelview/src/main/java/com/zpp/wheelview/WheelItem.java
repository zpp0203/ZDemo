package com.zpp.wheelview;

public class WheelItem implements IWheel {

    String label;

    public WheelItem(String label) {
        this.label = label;
    }

    @Override
    public String getShowText() {
        return label;
    }
}
