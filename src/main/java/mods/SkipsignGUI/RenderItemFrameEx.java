package mods.SkipsignGUI;

import java.util.Iterator;
import java.util.List;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.RenderItemFrame;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import mods.SkipsignGUI.SkipsignCore;

@SideOnly(Side.CLIENT)
public class RenderItemFrameEx extends RenderItemFrame
{
    public RenderItemFrameEx()
    {
        super();
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        this.doRender((EntityItemFrame)entity, x, y, z, p_76986_8_, partialTicks);
    }

    @Override
    public void doRender(EntityItemFrame entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        ItemStack frameItemStack = null;

        if (!CheckVisibleState(entity))
        {
            frameItemStack = entity.getDisplayedItem();
            entity.setDisplayedItem(null);
        }

        if ((!SkipsignCore.ModSetting.HideBoard.Bool()) ||
            (SkipsignCore.ModSetting.HideBoard.Bool() && CheckVisibleState(entity)))
        {
            super.doRender(entity, x, y, z, p_76986_8_, partialTicks);
        }

        if (frameItemStack != null)
        {
            entity.setDisplayedItem(frameItemStack);
        }
    }

    public boolean CheckVisibleState(EntityItemFrame entityItemFrame)
    {
        switch (SkipsignCore.ModSetting.FrameVisible.Int())
        {
        case 1:
            return true;
        case 2:
            return false;
        }

        if (Keyboard.isKeyDown(SkipsignCore.ModSetting.Zoom_Key.Int())) return true;

        Minecraft mc = Minecraft.getMinecraft();
        World world = mc.theWorld;
        EntityPlayer player = mc.thePlayer;
        int range = SkipsignCore.ModSetting.FrameRange.Int();

        switch (SkipsignCore.ModSetting.CheckDist.Int())
        {
        case 0:
            // ** VERY SLOW ** in Forge 1.8
            /* 
            double x = entityItemFrame.posX;
            double y = entityItemFrame.posY;
            double z = entityItemFrame.posZ;

            AxisAlignedBB aabb = AxisAlignedBB.fromBounds(x - range, y - range, z - range, x + (range + 1), y + (range + 1), z + (range + 1));
            for (Iterator iterator = world.getEntitiesWithinAABB(EntityPlayer.class, aabb).iterator(); iterator.hasNext();)
            {
                Entity entity = (Entity)iterator.next();
                EntityPlayer entityPlayer = (EntityPlayer)entity;

                if (entityPlayer.getDisplayName().equals(player.getDisplayName()))
                {
                    return false;
                }
            }
            break;
            */
        case 1:
            float dist = player.getDistanceToEntity(entityItemFrame);

            if (dist < range + 1 &&  dist > -range)
            {
                return true;
            }
        }

        return false;
    }
}
