package myessentials.test.utils;

import junit.framework.Assert;
import myessentials.test.MECTest;
import myessentials.utils.ClassUtils;
import org.junit.Test;

public class ClassUtilsTest extends MECTest {

    @Test
    public void shouldVerifyIfClassIsLoaded() {

        Assert.assertTrue("Method did not detect that a loaded class is loaded", ClassUtils.isClassLoaded("net.minecraft.block.Block"));
        Assert.assertFalse("Method did not detect that a class that is not loaded is not loaded", ClassUtils.isClassLoaded("invalid.kek"));

    }

    @Test
    public void shouldVerifyIfBukkitIsLoaded() {

        Assert.assertFalse("Method did not detect that bukkit is not loaded", ClassUtils.isBukkitLoaded());

    }

}
