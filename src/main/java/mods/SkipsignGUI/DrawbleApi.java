/*
 * SkipSign
 * 
 * (c) 2014 TTTA2
 * https://github.com/TTTA2/SkipSignGUI
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 * この MOD は、Minecraft Mod Public License (MMPL) 1.0 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package mods.SkipsignGUI;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class DrawbleApi
{
	public static Frustrum frustrum = new Frustrum();

	public static double DX = 0, DY = 0, DZ = 0;

	public static void beginFrustrum()
	{
		float f = SkipsignCore.renderPartialTicks;

		EntityPlayer player = Minecraft.getMinecraft().thePlayer;

		DX = player.prevPosX + (player.posX - player.prevPosX) * (double)f;
		DY = player.prevPosY + (player.posY - player.prevPosY) * (double)f;
		DZ = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)f;

		frustrum.setPosition(DX, DY, DZ);

		Minecraft.getMinecraft().renderGlobal.clipRenderersByFrustum(frustrum, f);
	}

	public static boolean isDraw(World w, int x, int y, int z)
	{
		Block chest = Block.getBlockFromName("chest");

		AxisAlignedBB bb = chest.getCollisionBoundingBoxFromPool(w, x, y, z);

		boolean ignoreFrustumCheck = false;

		return DrawbleApi.isDraw1(w, x, y, z, DX, DY, DZ) && (ignoreFrustumCheck || frustrum.isBoundingBoxInFrustum(bb));
	}

	public static boolean isDraw(TileEntityChest tileEntity, double x, double y, double z)
	{
		Block chest = Block.getBlockFromName("chest");

		AxisAlignedBB bb = chest.getCollisionBoundingBoxFromPool(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);

		boolean ignoreFrustumCheck = false;

		return DrawbleApi.isDraw1(tileEntity, DX, DY, DZ) && (ignoreFrustumCheck || frustrum.isBoundingBoxInFrustum(bb));

		//		boolean v = DrawbleApi.isDraw1(tileEntity, DX, DY, DZ) && (ignoreFrustumCheck || frustrum.isBoundingBoxInFrustum(bb));
		//		
		//		if (!v && tileEntity.adjacentChestChecked)
		//		{
		//			if (tileEntity.adjacentChestXPos != null)
		//			{
		//				return isDraw(tileEntity.adjacentChestXPos, tileEntity.adjacentChestXPos.xCoord, tileEntity.adjacentChestXPos.yCoord, tileEntity.adjacentChestXPos.zCoord);
		//			}
		//			else if (tileEntity.adjacentChestZPos != null)
		//			{
		//				return isDraw(tileEntity.adjacentChestZPos, tileEntity.adjacentChestZPos.xCoord, tileEntity.adjacentChestZPos.yCoord, tileEntity.adjacentChestZPos.zCoord);
		//			}
		//		}
		//		else
		//		{
		//			return v; 
		//		}
		//		
		//		return false;

	}

	//	public static boolean isDraw(TileEntityChest tileEntity, int x, int y, int z)
	//	{
	//		Block chest = Block.getBlockFromName("chest");
	//
	//		AxisAlignedBB bb = chest.getCollisionBoundingBoxFromPool(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
	//
	//		boolean ignoreFrustumCheck = false;
	//
	//		boolean v = DrawbleApi.isDraw1(tileEntity, DX, DY, DZ) && (ignoreFrustumCheck || frustrum.isBoundingBoxInFrustum(bb));
	//		
	//		if (!v && tileEntity.adjacentChestChecked)
	//		{
	//			if (tileEntity.adjacentChestXPos != null)
	//			{
	//				return isDraw(tileEntity.adjacentChestXPos, tileEntity.adjacentChestXPos.xCoord, tileEntity.adjacentChestXPos.yCoord, tileEntity.adjacentChestXPos.zCoord);
	//			}
	//		}
	//		else
	//		{
	//			return v; 
	//		}
	//		
	//		return false;
	//	}

	public static boolean isDraw(TileEntitySign tileEntity, double x, double y, double z)
	{
		Block sign = Blocks.stone;

		AxisAlignedBB bb = sign.getCollisionBoundingBoxFromPool(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);

		boolean ignoreFrustumCheck = false;

		return DrawbleApi.isDraw1(tileEntity, DX, DY, DZ) && (ignoreFrustumCheck || frustrum.isBoundingBoxInFrustum(bb));
	}

	@SideOnly(Side.CLIENT)
	public static boolean isDraw1(World w, int x, int y, int z, double p_145770_1_, double p_145770_3_, double p_145770_5_)
	{
		double d3 = x - p_145770_1_;
		double d4 = y - p_145770_3_;
		double d5 = z - p_145770_5_;
		double d6 = d3 * d3 + d4 * d4 + d5 * d5;
		return isInRangeToRenderDist(w, x, y, z, d6);
	}

	@SideOnly(Side.CLIENT)
	public static boolean isDraw1(TileEntity chest, double p_145770_1_, double p_145770_3_, double p_145770_5_)
	{
		double d3 = chest.xCoord - p_145770_1_;
		double d4 = chest.yCoord - p_145770_3_;
		double d5 = chest.zCoord - p_145770_5_;
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
		AxisAlignedBB bb = c.getCollisionBoundingBoxFromPool(chest.getWorldObj(), chest.xCoord, chest.yCoord, chest.zCoord);
		double d1 = bb.getAverageEdgeLength();
		d1 *= 64.0D * 1.0D;
		return par1 < d1 * d1;
	}

	private static double func_110828_a(double par1, double par3, double par5)
	{
		return par1 + (par3 - par1) * par5;
	}
}
