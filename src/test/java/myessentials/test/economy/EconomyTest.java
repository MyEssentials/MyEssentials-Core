package myessentials.test.economy;

import myessentials.economy.Economy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * TODO: Have this be tested when we have a way to run a full minecraft server during the test
 */
public class EconomyTest {

    Economy economy;

    //@Before
    public void shouldInitEcon() {
        economy = new Economy("minecraft:diamond");
    }

    //@Test
    public void shouldProvideProperName() {
        Assert.assertEquals("65 Diamond", economy.getCurrency(65));
    }

}
