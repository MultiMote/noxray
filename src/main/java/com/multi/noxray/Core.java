package com.multi.noxray;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;

@Mod(modid = Defines.MOD_ID, version = Defines.MOD_VERSION, name = Defines.MOD_NAME)
public class Core
{
    public static final HashSet<String> exclusions = new HashSet<String>();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        registerExclusions();
        MinecraftForge.EVENT_BUS.register(new EventListener());
    }

    private void registerExclusions() { // need more maybe
        exclusions.add("team.chisel.block.BlockCarvableGlow");
        exclusions.add("com.cricketcraft.chisel.block.BlockCarvableGlow");
        exclusions.add("team.chisel.block.BlockCarvableLayered");
        exclusions.add("com.cricketcraft.chisel.block.BlockCarvableLayered");
        exclusions.add("binnie.botany.ceramic.BlockCeramicBrick");
        exclusions.add("crazypants.enderio.conduit.facade.BlockConduitFacade");
    }
}
