package mods.SkipsignGUI;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.RenderItemFrame;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mods.SkipsignGUI.SkipsignCore;
import mods.SkipsignGUI.SkipSignHelper;

@SideOnly(Side.CLIENT)
public class RenderItemFrameEx extends RenderItemFrame
{
    public RenderItemFrameEx(RenderManager renderManager, RenderItem renderItem)
    {
        super(renderManager, renderItem);
    }

    @Override
    public void doRender(EntityItemFrame entity, double x, double y, double z, float entityYaw, float partialTicks)
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
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }

        if (frameItemStack != null)
        {
            entity.setDisplayedItem(frameItemStack);
        }
    }

    public boolean CheckVisibleState(EntityItemFrame entityItemFrame)
    {
        if (SkipsignCore.ModSetting.FrameVisible.Int() == 1)
            return true;
        if (SkipsignCore.ModSetting.FrameVisible.Int() == 2)
            return false;

        if (Keyboard.isKeyDown(SkipsignCore.ModSetting.Zoom_Key.Int()))
            return true;

        if (SkipSignHelper.IsInRangeToRenderDist(
                SkipSignHelper.GetDistancePlayerToEntity(entityItemFrame),
                SkipsignCore.ModSetting.ChestRange.Int()))
            return true;
        return false;
    }
}
