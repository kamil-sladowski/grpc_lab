package io.grpc.examples.imageviewer;

public class ImageDataSample {
    public String imageName;
    public boolean isNegative;
    public boolean isNeutral;
    public boolean isPositive;
    public boolean isBlackScreen;

    public ImageDataSample(String imageName, boolean isNegative, boolean isNeutral, boolean isPositive, boolean isBlackScreen) {
        this.imageName = imageName;
        this.isNegative = isNegative;
        this.isNeutral = isNeutral;
        this.isPositive = isPositive;
        this.isBlackScreen = isBlackScreen;
    }
}
