package mods.SkipsignGUI;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mods.SkipsignGUI.SkipsignCore;
import mods.SkipsignGUI.SkipSignHelper;
import mods.SkipsignGUI.DrawableApi;

@SideOnly(Side.CLIENT)
public class TileEntitySkullRendererEx extends TileEntitySkullRenderer
{
    public TileEntitySkullRendererEx()
    {
        super();
    }

    @Override
    public void renderTileEntityAt(TileEntitySkull entity, double x, double y, double z, float partialTicks, int destroyStage)
    {
        if (Minecraft.getMinecraft().thePlayer == null || entity.getWorld() == null ||
            (isDropOff(entity, x, y, z) && CheckVisibleState(entity)))
        {
            super.renderTileEntityAt(entity, x, y, z, partialTicks, destroyStage);
        }
    }

    public boolean isDropOff(TileEntity tile, double x, double y, double z)
    {
        if (SkipsignCore.ModSetting.DropOffSkull.Int() == 1)
        {
            return DrawableApi.isDraw((TileEntitySkull)tile, x, y,  z);
        }

        return true;
    }

    public boolean CheckVisibleState(TileEntitySkull tileEntitySkull)
    {
        if (SkipsignCore.ModSetting.SkullVisible.Int() == 1)
            return true;
        if (SkipsignCore.ModSetting.SkullVisible.Int() == 2)
            return false;

        if (Keyboard.isKeyDown(SkipsignCore.ModSetting.Zoom_Key.Int()))
            return true;

        if (SkipSignHelper.IsInRangeToRenderDist(
                SkipSignHelper.GetDistancePlayerToTileEntity(tileEntitySkull),
                SkipsignCore.ModSetting.SkullRange.Int()))
            return true;

        return false;
    }
}
