package mods.SkipsignGUI;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class DrawableApi
{
    public static Frustrum frustum = new Frustrum();

    public static double DX = 0, DY = 0, DZ = 0;

    public static void beginFrustum()
    {
        float f = SkipsignCore.renderPartialTicks;

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        DX = player.prevPosX + (player.posX - player.prevPosX) * (double)f;
        DY = player.prevPosY + (player.posY - player.prevPosY) * (double)f;
        DZ = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)f;

        frustum.setPosition(DX, DY, DZ);

        // Minecraft.getMinecraft().renderGlobal.clipRenderersByFrustum(frustum, f);
    }

    public static boolean isDraw(World w, int x, int y, int z)
    {
        Block chest = Block.getBlockFromName("chest");
        AxisAlignedBB bb = chest.getCollisionBoundingBoxFromPool(w, x, y, z);
        boolean ignoreFrustumCheck = false;

        return DrawableApi.isDraw1(w, x, y, z, DX, DY, DZ) && (ignoreFrustumCheck || frustum.isBoundingBoxInFrustum(bb));
    }

    public static boolean isDraw(TileEntityChest tileEntity, double x, double y, double z)
    {
        Block chest = Block.getBlockFromName("chest");
        AxisAlignedBB bb = chest.getCollisionBoundingBoxFromPool(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
        boolean ignoreFrustumCheck = false;

        return DrawableApi.isDraw1(tileEntity, DX, DY, DZ) && (ignoreFrustumCheck || frustum.isBoundingBoxInFrustum(bb));
    }

    public static boolean isDraw(TileEntitySign tileEntity, double x, double y, double z)
    {
        Block sign = Blocks.stone;
        AxisAlignedBB bb = sign.getCollisionBoundingBoxFromPool(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
        boolean ignoreFrustumCheck = false;

        return DrawableApi.isDraw1(tileEntity, DX, DY, DZ) && (ignoreFrustumCheck || frustum.isBoundingBoxInFrustum(bb));
    }

    @SideOnly(Side.CLIENT)
    public static boolean isDraw1(World w, int x, int y, int z, double dx, double dy, double dz)
    {
        double d3 = x - dx;
        double d4 = y - dy;
        double d5 = z - dz;
        double d6 = d3 * d3 + d4 * d4 + d5 * d5;
        return isInRangeToRenderDist(w, x, y, z, d6);
    }

    @SideOnly(Side.CLIENT)
    public static boolean isDraw1(TileEntity chest, double dx, double dy, double dz)
    {
        double d3 = (double)chest.xCoord - dx;
        double d4 = (double)chest.yCoord - dy;
        double d5 = (double)chest.zCoord - dz;
        double d6 = d3 * d3 + d4 * d4 + d5 * d5;
        return isInRangeToRenderDist(chest, d6);
    }

    @SideOnly(Side.CLIENT)
    public static boolean isInRangeToRenderDist(World w, int x, int y, int z, double par1)
    {
        Block c = Block.getBlockFromName("chest");
        AxisAlignedBB bb = c.getCollisionBoundingBoxFromPool(w, x, y, z);
        double d1 = bb.getAverageEdgeLength();
        d1 *= 64.0D * 1.0D;
        return par1 < d1 * d1;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isInRangeToRenderDist(TileEntity chest, double par1)
    {
        Block c = Block.getBlockFromName("chest");
        AxisAlignedBB bb = c.getCollisionBoundingBoxFromPool(chest.getWorldObj(),
                                                             chest.xCoord,
                                                             chest.yCoord,
                                                             chest.zCoord);
        double d1 = bb.getAverageEdgeLength();
        d1 *= 64.0D * 1.0D;
        return par1 < d1 * d1;
    }
}
