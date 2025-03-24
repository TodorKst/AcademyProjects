package org.example.enums;

public enum LightingMode {
    DIM(0.5f),
    MEDIUM(0.75f),
    STRONG(1.0f);


    private final float lumenOutputFactor;

    LightingMode(float lumenOutputFactor) {
        this.lumenOutputFactor = lumenOutputFactor;
    }

    public float getLumenOutputFactor() {
        return lumenOutputFactor;
    }

}
