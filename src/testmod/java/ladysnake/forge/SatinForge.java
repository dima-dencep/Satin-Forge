package ladysnake.forge;

import ladysnake.satinbasictest.SatinBasicTest;
import ladysnake.satindepthtest.SatinDepthTest;
import ladysnake.satinrenderlayer.SatinRenderLayerTest;
import ladysnake.satintestcore.SatinTestCore;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("satintestcore")
public class SatinForge {
    public SatinForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        new SatinTestCore(bus);

        new SatinBasicTest(bus);
        new SatinDepthTest(bus);

        new SatinRenderLayerTest(bus);
    }
}
