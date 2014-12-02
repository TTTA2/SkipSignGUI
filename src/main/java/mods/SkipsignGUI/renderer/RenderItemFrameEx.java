package mods.SkipsignGUI.renderer;

import java.util.Iterator;
import java.util.List;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.RenderItemFrame;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mods.SkipsignGUI.SkipsignCore;

@SideOnly(Side.CLIENT)
public class RenderItemFrameEx extends RenderItemFrame
{
    public RenderItemFrameEx(RenderManager p_i46166_1_, RenderItem p_i46166_2_)
	{
		super(p_i46166_1_, p_i46166_2_);
	}
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
	{
		return super.getEntityTexture(entity);
	}

	@Override
    protected ResourceLocation getEntityTexture(EntityItemFrame entity)
	{
		return super.getEntityTexture(entity);
	}

	@Override
    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks)
	{
    	this.doRender((EntityItemFrame)entity, x, y, z, p_76986_8_, partialTicks);
	}

	@Override
    public void doRender(EntityItemFrame entity, double x, double y, double z, float p_76986_8_, float partialTicks)
	{
		EntityItemFrame entityItemFrame = (EntityItemFrame) entity;
		ItemStack frameItemStack = null;

		if (!CheckVisibleState(entityItemFrame))
		{
			frameItemStack = entityItemFrame.getDisplayedItem();
			entityItemFrame.setDisplayedItem(null);
		}

		if ((!SkipsignCore.ModSetting.HideBoard.Bool()) ||
			(SkipsignCore.ModSetting.HideBoard.Bool() && CheckVisibleState(entityItemFrame)))
		{
		    super.doRender(entity, x, y, z, p_76986_8_, partialTicks);
		}

	    if (frameItemStack != null)
	    {
		    entityItemFrame.setDisplayedItem(frameItemStack);
	    }
	}

	public boolean CheckVisibleState(EntityItemFrame entityItemFrame)
	{
		switch (SkipsignCore.ModSetting.FrameVisible.Int())
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
		int range = SkipsignCore.ModSetting.FrameRange.Int();

		double x = entityItemFrame.posX;
		double y = entityItemFrame.posY;
		double z = entityItemFrame.posZ;

		switch (SkipsignCore.ModSetting.CheckDist.Int())
		{
		case 0:
			int rad = SkipsignCore.ModSetting.FrameRange.Int();

			List<Entity> entities = 
				world.getEntitiesWithinAABB(EntityPlayer.class,
											AxisAlignedBB.fromBounds(x - rad,
																	 y - rad,
																	 z - rad,
																	 x + (rad + 1),
																	 y + (rad + 1),
																	 z + (rad + 1)));

			for (Iterator iterator = entities.iterator(); iterator.hasNext();)
			{
				Entity entity = (Entity)iterator.next();
				EntityPlayer entityPlayer = (EntityPlayer)entity;

				if (entityPlayer.getDisplayName().equals(player.getDisplayName()))
				{
					return true;
				}
			}

		case 1:
			double dist = player.getDistance(x, y, z);

			if (dist < SkipsignCore.ModSetting.FrameRange.Int() + 1 &&
			    dist > (-SkipsignCore.ModSetting.FrameRange.Int()))
			{
				return true;
			}
		}

		return false;
	}
}
