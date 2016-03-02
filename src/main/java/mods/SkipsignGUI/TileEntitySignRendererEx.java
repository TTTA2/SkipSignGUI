package mods.SkipSignGUI;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mods.SkipSignGUI.SkipSignCore;
import mods.SkipSignGUI.SkipSignHelper;
import mods.SkipSignGUI.DrawableApi;

@SideOnly(Side.CLIENT)
public class TileEntitySignRendererEx extends TileEntitySignRenderer
{
    public TileEntitySignRendererEx()
    {
        super();
    }

    @Override
    public void renderTileEntityAt(TileEntitySign entity, double x, double y, double z, float partialTicks, int destroyStage)
    {
        if (!isDropOff(entity, x, y, z))
            return;

        IChatComponent [] tempSignText = null;
        if (!CheckVisibleState(entity))
        {
            tempSignText = new IChatComponent[entity.signText.length];
            for (int i = 0; i < entity.signText.length; i++)
            {
                tempSignText[i] = entity.signText[i];
                entity.signText[i] = null;
            }
        }

        if ((!SkipSignCore.ModSetting.HideBoard.Bool()) ||
            (SkipSignCore.ModSetting.HideBoard.Bool() && CheckVisibleState(entity)))
        {
            super.renderTileEntityAt(entity, x, y, z, partialTicks, destroyStage);
        }

        if (tempSignText != null)
        {
            for (int i = 0; i < entity.signText.length; i++)
            {
                entity.signText[i] = tempSignText[i];
            }
        }
    }

    public boolean isDropOff(TileEntity tile, double x, double y, double z)
    {
        if (SkipSignCore.ModSetting.DropOffSign.Int() == 1)
        {
            return DrawableApi.isDraw((TileEntitySign)tile, x, y,  z);
        }
        return true;
    }

    public boolean CheckVisibleState(TileEntitySign tileEntitySign)
    {
        if (SkipSignCore.ModSetting.SignVisible.Int() == 1)
            return true;
        if (SkipSignCore.ModSetting.SignVisible.Int() == 2)
            return false;

        if (Keyboard.isKeyDown(SkipSignCore.ModSetting.Zoom_Key.Int()))
            return true;

        if (SkipSignHelper.IsInRangeToRenderDist(
                SkipSignHelper.GetDistancePlayerToTileEntity(tileEntitySign),
                SkipSignCore.ModSetting.SignRange.Int()))
            return true;

        return false;
    }
}
