package org.matthelliwell.minecraftosloader.writer;

class HeightScaler {
    private static final float LOWEST = 1;
    private static final float HIGHEST = 240;
    private final float offset;
    private final float scale;

    public HeightScaler(final float minHeight, final float maxHeight) {
        scale = (float) Math.min(1.0, (HIGHEST - LOWEST) / (maxHeight - minHeight));
        offset = LOWEST - scale * minHeight;
    }

    public float scale(final float height) {
        return scale * height + offset;
    }

    public float getScale() {
        return scale;
    }
}
