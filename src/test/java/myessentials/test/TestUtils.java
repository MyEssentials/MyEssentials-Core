package myessentials.test;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.FakePlayer;

import java.util.UUID;

/**
 * All utilities regarding tests can be found here
 */
public class TestUtils {

    public static FakePlayer createFakePlayer(MinecraftServer server) {
        return new FakePlayer(server.worldServerForDimension(0), new GameProfile(UUID.randomUUID(), "HmmmTasty"));
    }
}
