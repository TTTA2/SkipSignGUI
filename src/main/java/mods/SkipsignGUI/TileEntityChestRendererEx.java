package mods.SkipsignGUI;

import java.util.Iterator;
import java.util.List;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mods.SkipsignGUI.SkipsignCore;
import mods.SkipsignGUI.DrawableApi;

@SideOnly(Side.CLIENT)
public class TileEntityChestRendererEx extends TileEntityChestRenderer
{
    public TileEntityChestRendererEx()
    {
        super();
    }

    public void renderTileEntityAt(TileEntity entity, double posX, double posZ, double p_180535_6_, float p_180535_8_, int p_180535_9_)
    {
        this.func_180538_a((TileEntityChest)entity, posX, posZ, p_180535_6_, p_180535_8_, p_180535_9_);
    }

    @Override
    public void func_180538_a(TileEntityChest entity, double posX, double posZ, double p_180538_6_, float p_180538_8_, int p_180538_9_)
    {
        if (Minecraft.getMinecraft().thePlayer == null || entity.getWorld() == null || 
            (isDropOff(entity, posX, posZ, p_180538_6_) && CheckVisibleState(entity)))
        {
            super.func_180538_a(entity, posX, posZ, p_180538_6_, p_180538_8_, p_180538_9_);
        }
    }

    public boolean isDropOff(TileEntity tile, double x, double y, double z)
    {
        if (SkipsignCore.ModSetting.DropOffChest.Int() == 1)
        {
            return DrawableApi.isDraw((TileEntityChest)tile, x, y,  z);
        }
        
        return true;
    }

    public boolean CheckVisibleState(TileEntityChest tileEntityChest)
    {
        switch (SkipsignCore.ModSetting.ChestVisible.Int())
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
        int range = SkipsignCore.ModSetting.ChestRange.Int();

        BlockPos pos = tileEntityChest.getPos();
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
