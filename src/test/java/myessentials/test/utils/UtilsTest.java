package myessentials.test.utils;

import myessentials.utils.ClassUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class UtilsTest {

    @Test
    public void shouldClassBeLoaded() {
        Assert.assertTrue(ClassUtils.isClassLoaded("myessentials.MyEssentialsCore"));
    }






}
