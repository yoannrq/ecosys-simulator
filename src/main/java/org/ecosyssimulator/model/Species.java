package org.ecosyssimulator.model;

public enum Species {
    CAROTTE(0, 0.1, 10, "\uD83E\uDD55"),
    LAPIN(1, 0.3, 5, "\uD83D\uDC30"),
    RENARD(2, 0.2, 8, "\uD83E\uDD8A"),
    LOUP(3, 0.1, 12, "\uD83D\uDC3A");

    private final int trophicLevel;
    private final double reproductionRate;
    private final int lifespan;
    private final String emoticon;

    Species(int trophicLevel, double reproductionRate, int lifespan, String emoticon) {
        this.trophicLevel = trophicLevel;
        this.reproductionRate = reproductionRate;
        this.lifespan = lifespan;
        this.emoticon = emoticon;
    }

    // Getters
    public int getTrophicLevel() { return trophicLevel; }
    public double getReproductionRate() { return reproductionRate; }
    public int getLifespan() { return lifespan; }
    public String getEmoticon() { return emoticon; }
}
