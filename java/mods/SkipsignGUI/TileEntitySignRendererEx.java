package mods.SkipsignGUI;

import java.util.Iterator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntitySignRendererEx extends TileEntitySpecialRenderer
{
	private static final ResourceLocation field_147513_b = new ResourceLocation("textures/entity/sign.png");
	private final ModelSign field_147514_c = new ModelSign();
	private static final String __OBFID = "CL_00000970";

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

		World w = mc.theWorld;
		EntityPlayer p = mc.thePlayer;

		int range = SkipsignCore.ModSetting.SignRange.Int();

		int x = tileEntitySign.xCoord, y = tileEntitySign.yCoord, z = tileEntitySign.zCoord;

		switch (SkipsignCore.ModSetting.CheckDist.Int())
		{
		case 0:

			int rad = SkipsignCore.ModSetting.SignRange.Int();

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

			if (dist < SkipsignCore.ModSetting.SignRange.Int() + 1 && dist > (-SkipsignCore.ModSetting.SignRange.Int()))
			{
				return true;
			}
		}

		return false;
	}

	public void func_147500_a(TileEntitySign p_147512_1_, double p_147512_2_, double p_147512_4_, double p_147512_6_, float p_147512_8_)
	{
		Block block = p_147512_1_.getBlockType();
		GL11.glPushMatrix();
		float f1 = 0.6666667F;
		float f3;

		if (block == Blocks.standing_sign)
		{
			GL11.glTranslatef((float)p_147512_2_ + 0.5F, (float)p_147512_4_ + 0.75F * f1, (float)p_147512_6_ + 0.5F);
			float f2 = (float)(p_147512_1_.getBlockMetadata() * 360) / 16.0F;
			GL11.glRotatef(-f2, 0.0F, 1.0F, 0.0F);
			this.field_147514_c.signStick.showModel = true;
		}
		else
		{
			int j = p_147512_1_.getBlockMetadata();
			f3 = 0.0F;

			if (j == 2)
			{
				f3 = 180.0F;
			}

			if (j == 4)
			{
				f3 = 90.0F;
			}

			if (j == 5)
			{
				f3 = -90.0F;
			}

			GL11.glTranslatef((float)p_147512_2_ + 0.5F, (float)p_147512_4_ + 0.75F * f1, (float)p_147512_6_ + 0.5F);
			GL11.glRotatef(-f3, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, -0.3125F, -0.4375F);
			this.field_147514_c.signStick.showModel = false;
		}

		this.bindTexture(field_147513_b);
		GL11.glPushMatrix();
		GL11.glScalef(f1, -f1, -f1);

		if ((!SkipsignCore.ModSetting.HideBoard.Bool()) || (SkipsignCore.ModSetting.HideBoard.Bool() && CheckVisibleState(p_147512_1_)))
		{
			this.field_147514_c.renderSign();
		}

		GL11.glPopMatrix();
		FontRenderer fontrenderer = this.func_147498_b();
		f3 = 0.016666668F * f1;
		GL11.glTranslatef(0.0F, 0.5F * f1, 0.07F * f1);
		GL11.glScalef(f3, -f3, f3);
		GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
		GL11.glDepthMask(false);
		byte b0 = 0;

		for (int i = 0; i < p_147512_1_.signText.length; ++i)
		{
			String s = p_147512_1_.signText[i];

			if (i == p_147512_1_.lineBeingEdited)
			{
				s = "> " + s + " <";
				fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, i * 10 - p_147512_1_.signText.length * 5, b0);
			}
			else
			{
				if (CheckVisibleState(p_147512_1_))
				{
					fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, i * 10 - p_147512_1_.signText.length * 5, b0);
				}
			}
		}

		GL11.glDepthMask(true);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

	public void renderTileEntityAt(TileEntity p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_)
	{
		if (isDropOff(p_147500_1_, p_147500_2_, p_147500_4_, p_147500_6_))
		{
			this.func_147500_a((TileEntitySign)p_147500_1_, p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_);
		}
	}
	
	public boolean isDropOff(TileEntity tile, double x, double y, double z)
	{
		if (SkipsignCore.ModSetting.DropOffSign.Int() == 1)
		{
			return DrawbleApi.isDraw((TileEntitySign)tile, x, y,  z);
		}
		
		return true;
	}

	public double getDistance(double par1, double par3, double par5, double par7, double par9, double par11)
	{
		double d3 = par7 - par1;
		double d4 = par9 - par3;
		double d5 = par11 - par5;
		return (double)MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
	}
}