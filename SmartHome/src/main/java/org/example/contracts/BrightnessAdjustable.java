package org.example.contracts;

public interface BrightnessAdjustable {
//    interface is currently implemented in one class but exists to allow future proofing for anything that can have its brightness adjusted: TVs, Lamps, Projectors...
    void increaseBrightness();

    void decreaseBrightness();

}
