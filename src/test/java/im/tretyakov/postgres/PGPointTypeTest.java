package im.tretyakov.postgres;

import org.postgresql.geometric.PGpoint;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test for PGPointType class.
 *
 * @author Dmitry Tretyakov (dmitry@tretyakov.im)
 * Created on 30.07.15
 */
public class PGPointTypeTest extends Assert {

    @Test
    public void testEquals() throws Exception {
        final PGpoint point = new PGpoint(1.0, 1.0);
        final PGpoint other = new PGpoint(1.0, 1.0);
        assertTrue(new PGPointType().equals(point, other));
    }
}