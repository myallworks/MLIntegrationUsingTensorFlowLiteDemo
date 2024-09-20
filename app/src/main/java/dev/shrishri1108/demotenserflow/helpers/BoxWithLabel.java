package dev.shrishri1108.demotenserflow.helpers;

import android.graphics.Rect;

public class BoxWithLabel {
    public Rect rect;
    public String label;

    public BoxWithLabel(Rect rect, String label) {
        this.rect = rect;
        this.label = label;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
