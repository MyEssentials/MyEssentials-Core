package myessentials.test.localization;

import myessentials.Localization;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStreamReader;

/**
 * Test the localization system
 */
public class LocalizationTest {
    private static Localization local = null;

    @BeforeClass
    public static void setup() {
        local = new Localization("", "test", "/myessentials/localization/", LocalizationTest.class);
    }

    @Test
    public void shouldRetrieveValuesFromLocalization() throws Exception {
        Assert.assertEquals("There is too many key-value pairs!", 5, local.getLocalizationMap().size());
        Assert.assertEquals("Test Value 1...", local.getLocalization("mytown.core.test.value1"));
        Assert.assertEquals("Test Value 2...", local.getLocalization("mytown.core.test.value2"));
        Assert.assertEquals("Test Value 3...", local.getLocalization("mytown.core.test.value3"));
        Assert.assertEquals("Test Value 4...", local.getLocalization("mytown.core.test.value4"));
        Assert.assertEquals("Test Value 5...", local.getLocalization("mytown.core.test.value5"));
    }
}
