package org.matthelliwell.minecraftosloader.feature;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.matthelliwell.minecraftosloader.file.Road;
import org.matthelliwell.minecraftosloader.file.RoadFileLoader;
import org.opengis.referencing.FactoryException;

public class RoadTunnelGenerator {

    private static final GeometryFactory GEOMETRY_FACTORY = JTSFactoryFinder.getGeometryFactory(null);

    private final FeatureGrid featureGrid;

    public RoadTunnelGenerator(final FeatureGrid featureGrid) {
        this.featureGrid = featureGrid;
    }

    public void generate(final Path path, final String gridSquare) throws IOException, FactoryException {
        final File file = path.resolve(gridSquare.toUpperCase() + "_RoadTunnel.shp").toFile();
        new RoadFileLoader(file, featureGrid.getBounds(), this::onNewRoad).processFile();
    }

    private void onNewRoad(final Road road) {
        final Geometry result = road.getFullWidthRoad();
        long minX = Integer.MAX_VALUE;
        long minY = Integer.MAX_VALUE;
        long maxX = Integer.MIN_VALUE;
        long maxY = Integer.MIN_VALUE;
        for ( final Coordinate c: result.getCoordinates() ) {
            minX = Math.min(Math.round(c.x), minX);
            maxX = Math.max(Math.round(c.x), maxX);
            minY = Math.min(Math.round(c.y), minY);
            maxY = Math.max(Math.round(c.y), maxY);
        }

        for (long x = minX; x <= maxX; ++x) {
            for (long y = minY; y <= maxY; ++y) {
                final Point p = GEOMETRY_FACTORY.createPoint(new Coordinate(x, y));
                if (result.intersects(p)) {
                    featureGrid.setFeature((int)x, (int)y, FeatureGrid.GLASSHOUSE);
                }
            }
        }
    }
}
