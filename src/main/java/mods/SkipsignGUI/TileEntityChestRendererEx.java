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

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Calendar;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderLivingEvent;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class TileEntityChestRendererEx extends TileEntitySpecialRenderer
{
	private static final ResourceLocation field_147507_b = new ResourceLocation("textures/entity/chest/trapped_double.png");
	private static final ResourceLocation field_147508_c = new ResourceLocation("textures/entity/chest/christmas_double.png");
	private static final ResourceLocation field_147505_d = new ResourceLocation("textures/entity/chest/normal_double.png");
	private static final ResourceLocation field_147506_e = new ResourceLocation("textures/entity/chest/trapped.png");
	private static final ResourceLocation field_147503_f = new ResourceLocation("textures/entity/chest/christmas.png");
	private static final ResourceLocation field_147504_g = new ResourceLocation("textures/entity/chest/normal.png");
	private ModelChest field_147510_h = new ModelChest();
	private ModelChest field_147511_i = new ModelLargeChest();
	private boolean field_147509_j;
	private static final String __OBFID = "CL_00000965";

	public TileEntityChestRendererEx()
	{
		Calendar calendar = Calendar.getInstance();

		if (calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26)
		{
			this.field_147509_j = true;
		}
	}

    public void renderTileEntityAt(TileEntityChest p_147502_1_, double p_147502_2_, double p_147502_4_, double p_147502_6_, float p_147502_8_)
    {
    	//System.out.println("Render");
    	
        int i;

        if (!p_147502_1_.hasWorldObj())
        {
            i = 0;
        }
        else
        {
            Block block = p_147502_1_.getBlockType();
            i = p_147502_1_.getBlockMetadata();

            if (block instanceof BlockChest && i == 0)
            {
                try
                {
                ((BlockChest)block).func_149954_e(p_147502_1_.getWorldObj(), p_147502_1_.xCoord, p_147502_1_.yCoord, p_147502_1_.zCoord);
                }
                catch (ClassCastException e)
                {
                    FMLLog.severe("Attempted to render a chest at %d,  %d, %d that was not a chest", p_147502_1_.xCoord, p_147502_1_.yCoord, p_147502_1_.zCoord);
                }
                i = p_147502_1_.getBlockMetadata();
            }

            p_147502_1_.checkForAdjacentChests();
        }

        if (p_147502_1_.adjacentChestZNeg == null && p_147502_1_.adjacentChestXNeg == null)
        {
            ModelChest modelchest;

            if (p_147502_1_.adjacentChestXPos == null && p_147502_1_.adjacentChestZPos == null)
            {
                modelchest = this.field_147510_h;

                if (p_147502_1_.func_145980_j() == 1)
                {
                    this.bindTexture(field_147506_e);
                }
                else if (this.field_147509_j)
                {
                    this.bindTexture(field_147503_f);
                }
                else
                {
                    this.bindTexture(field_147504_g);
                }
            }
            else
            {
                modelchest = this.field_147511_i;

                if (p_147502_1_.func_145980_j() == 1)
                {
                    this.bindTexture(field_147507_b);
                }
                else if (this.field_147509_j)
                {
                    this.bindTexture(field_147508_c);
                }
                else
                {
                    this.bindTexture(field_147505_d);
                }
            }

            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslatef((float)p_147502_2_, (float)p_147502_4_ + 1.0F, (float)p_147502_6_ + 1.0F);
            GL11.glScalef(1.0F, -1.0F, -1.0F);
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            short short1 = 0;

            if (i == 2)
            {
                short1 = 180;
            }

            if (i == 3)
            {
                short1 = 0;
            }

            if (i == 4)
            {
                short1 = 90;
            }

            if (i == 5)
            {
                short1 = -90;
            }

            if (i == 2 && p_147502_1_.adjacentChestXPos != null)
            {
                GL11.glTranslatef(1.0F, 0.0F, 0.0F);
            }

            if (i == 5 && p_147502_1_.adjacentChestZPos != null)
            {
                GL11.glTranslatef(0.0F, 0.0F, -1.0F);
            }

            GL11.glRotatef((float)short1, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            float f1 = p_147502_1_.prevLidAngle + (p_147502_1_.lidAngle - p_147502_1_.prevLidAngle) * p_147502_8_;
            float f2;

            if (p_147502_1_.adjacentChestZNeg != null)
            {
                f2 = p_147502_1_.adjacentChestZNeg.prevLidAngle + (p_147502_1_.adjacentChestZNeg.lidAngle - p_147502_1_.adjacentChestZNeg.prevLidAngle) * p_147502_8_;

                if (f2 > f1)
                {
                    f1 = f2;
                }
            }

            if (p_147502_1_.adjacentChestXNeg != null)
            {
                f2 = p_147502_1_.adjacentChestXNeg.prevLidAngle + (p_147502_1_.adjacentChestXNeg.lidAngle - p_147502_1_.adjacentChestXNeg.prevLidAngle) * p_147502_8_;

                if (f2 > f1)
                {
                    f1 = f2;
                }
            }

            f1 = 1.0F - f1;
            f1 = 1.0F - f1 * f1 * f1;
            modelchest.chestLid.rotateAngleX = -(f1 * (float)Math.PI / 2.0F);
            modelchest.renderAll();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
	
	public boolean isDropOff(TileEntity tile, double x, double y, double z)
	{
		if (SkipsignCore.ModSetting.DropOffChest.Int() == 1)
		{
			return DrawbleApi.isDraw((TileEntityChest)tile, x, y,  z);
		}
		
		return true;
	}

	public boolean CheckVisibleState(TileEntity tileEntitySign)
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

		World w = mc.theWorld;
		EntityPlayer p = mc.thePlayer;

		int range = SkipsignCore.ModSetting.ChestRange.Int();

		int x = tileEntitySign.xCoord, y = tileEntitySign.yCoord, z = tileEntitySign.zCoord;

		switch (SkipsignCore.ModSetting.CheckDist.Int())
		{
		case 0:

			int rad = SkipsignCore.ModSetting.ChestRange.Int();

			for (Iterator iterator = w.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(x - rad, y - rad, z - rad, x + (rad + 1), y + (rad + 1), z + (rad + 1))).iterator(); iterator.hasNext();)
			{
				Entity entity = (Entity)iterator.next();
				EntityPlayer player = (EntityPlayer)entity;

				if (player.getDisplayName().equals(p.getDisplayName()))
				{
					return true;
				}
			}

		case 1:

			double dist = getDistance((double)x, (double)y, (double)z, p.posX, p.posY, p.posZ);

			if (dist < SkipsignCore.ModSetting.ChestRange.Int() + 1 && dist > (-SkipsignCore.ModSetting.ChestRange.Int()))
			{
				return true;
			}
		}

		return false;
	}

	public double getDistance(double par1, double par3, double par5, double par7, double par9, double par11)
	{
		double d3 = par7 - par1;
		double d4 = par9 - par3;
		double d5 = par11 - par5;
		return (double)MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
	}

	@Override
	public void renderTileEntityAt(TileEntity p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_)
	{
		if (Minecraft.getMinecraft().thePlayer == null)
		{
			this.renderTileEntityAt((TileEntityChest)p_147500_1_, p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_);
			return;
		}
		
		if ((CheckVisibleState(p_147500_1_) && isDropOff(p_147500_1_, p_147500_2_, p_147500_4_,  p_147500_6_)) || ((TileEntityChest)p_147500_1_).getWorldObj() == null)
		{
			this.renderTileEntityAt((TileEntityChest)p_147500_1_, p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_);
		}
		else if (!(CheckVisibleState(p_147500_1_)) && ((TileEntityChest)p_147500_1_).getWorldObj() != null)
		{

		}
	}
}
