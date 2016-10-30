package myessentials.test.utils;

import myessentials.utils.MathUtils;
import org.junit.Assert;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void shouldSun() {
        Assert.assertEquals(110, MathUtils.sumFromNtoM(5, 15));
    }

}
