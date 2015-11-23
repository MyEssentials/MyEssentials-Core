package myessentials;

import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import myessentials.entities.sign.SignClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.util.Map;

/**
 * For autodownloading stuff.
 * This is really unoriginal, mostly ripped off FML, credits to cpw and chicken-bones.
 */
@IFMLLoadingPlugin.SortingIndex(1001)
public class DepLoader implements IFMLLoadingPlugin, IFMLCallHook {
    @Override
    public Void call() throws Exception {
        File mcDir = (File) FMLInjectionData.data()[6];

        new myessentials.deploader.DepLoader(new File(mcDir, "MyEssentials/libs"), (LaunchClassLoader) myessentials.deploader.DepLoader.class.getClassLoader()).load();

        return null;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{
                SignClassTransformer.class.getName()
        };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return getClass().getName();
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}