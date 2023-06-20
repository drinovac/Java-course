package hr.fer.zemris.java;

public class TrigonometricModel {
    public String x;
    public String sinX;
    public String cosX;

    public TrigonometricModel(int x, double sinX, double cosX) {
        this.x = String.valueOf(x);
        this.sinX = String.format("%.5f", sinX);
        this.cosX = String.format("%.5f", cosX);
    }

    public String getX() {
        return x;
    }

    public String getSinX() {
        return sinX;
    }

    public String getCosX() {
        return cosX;
    }
}
