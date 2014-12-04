package mods.SkipsignGUI.renderer;

import java.util.Iterator;
import java.util.List;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mods.SkipsignGUI.SkipsignCore;
import mods.SkipsignGUI.DrawableApi;

@SideOnly(Side.CLIENT)
public class TileEntitySignRendererEx extends TileEntitySignRenderer
{
    public TileEntitySignRendererEx()
    {
        super();
    }

    public void renderTileEntityAt(TileEntity entity, double posX, double posZ, double p_180535_6_, float p_180535_8_, int p_180535_9_)
    {
        this.func_180541_a((TileEntitySign)entity, posX, posZ, p_180535_6_, p_180535_8_, p_180535_9_);
    }

    @Override
    public void func_180541_a(TileEntitySign entity, double posX, double posZ, double p_180541_6_, float p_180541_8_, int p_180541_9_)
    {
        if (!isDropOff(entity, posX, posZ, p_180541_6_))
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

        if ((!SkipsignCore.ModSetting.HideBoard.Bool()) ||
            (SkipsignCore.ModSetting.HideBoard.Bool() && CheckVisibleState(entity)))
        {
            super.func_180541_a(entity, posX, posZ, p_180541_6_, p_180541_8_, p_180541_9_);
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
        if (SkipsignCore.ModSetting.DropOffSign.Int() == 1)
        {
            return DrawableApi.isDraw((TileEntitySign)tile, x, y,  z);
        }
        return true;
    }

    public boolean CheckVisibleState(TileEntitySign tileEntitySign)
    {
        switch (SkipsignCore.ModSetting.SignVisible.Int())
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
        int range = SkipsignCore.ModSetting.SignRange.Int();

        BlockPos pos = tileEntitySign.getPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        switch (SkipsignCore.ModSetting.CheckDist.Int())
        {
        case 0:
            // ** VERY SLOW ** in Forge 1.8
            /* 
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
            double dist = player.getDistance((double)x, (double)y, (double)z);

            if (dist < range + 1 && dist > -range)
            {
                return true;
            }
        }
        return false;
    }
}
