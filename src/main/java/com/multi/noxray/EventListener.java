package com.multi.noxray;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

/**
 * Created by MultiMote on 26.01.2016.
 */

@SideOnly(Side.CLIENT)
public class EventListener {

    // true means bad
    public boolean checkTexture(ResourceLocation loc) {
        Minecraft mc = Minecraft.getMinecraft();
        InputStream inputstream = null;
        try {
            inputstream = mc.getResourceManager().getResource(loc).getInputStream();
            BufferedImage img = ImageIO.read(inputstream);
            for (int x = 0; x < img.getWidth(); x++) {
                for (int y = 0; y < img.getHeight(); y++) {
                    boolean bad = ((img.getRGB(x, y) >> 24) == 0);
                    if (bad) {
                        inputstream.close();
                        img.flush();
                        return true;
                    }
                }
            }
        } catch (IOException ignored) {}
        finally {
            if (inputstream != null) {
                try {
                    inputstream.close();
                } catch (IOException ignored) {}
            }
        }
        return false;
    }

    @SubscribeEvent
    public void textureStitch(TextureStitchEvent.Post event) throws OnNoesException {
        TextureMap map = event.map;
        if (map.getTextureType() == 0) {
            for (Object o : GameData.getBlockRegistry()) {
                Block block = (Block) o;
                HashSet<String> set = new HashSet<String>();
                boolean normalblock = false;

                try{
                    normalblock = block.renderAsNormalBlock() && block.isOpaqueCube() && block.getRenderType() == 0 && !(block instanceof BlockLeavesBase) && block.getRenderBlockPass() == 0;
                } catch (NullPointerException ignored){}

                if (normalblock) {

                    if(Core.exclusions.contains(block.getClass().getName())) continue;

                    for (int side = 0; side < 6; side++){
                        IIcon tex = null;
                        try{
                            tex = block.getBlockTextureFromSide(side);
                        } catch (NullPointerException ignored){}

                        if(tex!=null && tex.getIconName() != null) {
                            set.add(tex.getIconName());
                        }
                    }
                }
                for(String tex : set){
                    String domain;
                    String rname;
                    if(tex.contains(":")){
                        String res[] = tex.split(":");
                        domain = res[0];
                        rname = res[1];
                    } else{
                        domain = "minecraft";
                        rname = tex;
                    }
                    boolean bad = checkTexture(new ResourceLocation(domain  + ":textures/blocks/" + rname + ".png"));
                    if(bad) {
                        OnNoesException ex = new OnNoesException();
                        Minecraft.getMinecraft().crashed(new CrashReport("Your resourcepack is invalid! Remove it!", ex));
                        throw ex;
                    }
                }
            }
        }

    }


}
