package mods.SkipSignGUI;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mods.SkipSignGUI.SkipSignCore;
import mods.SkipSignGUI.SkipSignHelper;
import mods.SkipSignGUI.DrawableApi;

@SideOnly(Side.CLIENT)
public class TileEntityChestRendererEx extends TileEntityChestRenderer
{
    public TileEntityChestRendererEx()
    {
        super();
    }

    @Override
    public void renderTileEntityAt(TileEntityChest entity, double x, double y, double z, float partialTicks, int destroyStage)
    {
        if (Minecraft.getMinecraft().thePlayer == null || entity.getWorld() == null ||
            (isDropOff(entity, x, y, z) && CheckVisibleState(entity)))
        {
            super.renderTileEntityAt(entity, x, y, z, partialTicks, destroyStage);
        }
    }

    public boolean isDropOff(TileEntity tile, double x, double y, double z)
    {
        if (SkipSignCore.ModSetting.DropOffChest.Int() == 1)
        {
            return DrawableApi.isDraw((TileEntityChest)tile, x, y,  z);
        }

        return true;
    }

    public boolean CheckVisibleState(TileEntityChest tileEntityChest)
    {
        if (SkipSignCore.ModSetting.ChestVisible.Int() == 1)
            return true;
        if (SkipSignCore.ModSetting.ChestVisible.Int() == 2)
            return false;

        if (Keyboard.isKeyDown(SkipSignCore.ModSetting.Zoom_Key.Int()))
            return true;

        if (SkipSignHelper.IsInRangeToRenderDist(
                SkipSignHelper.GetDistancePlayerToTileEntity(tileEntityChest),
                SkipSignCore.ModSetting.ChestRange.Int()))
            return true;

        return false;
    }
}
