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

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import net.minecraftforge.client.event.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = "SkipSign", version = "1.7.2-SignBoard|ItemFrame|Chest")
public class SkipsignCore
{
	private boolean key_down = false;
	private int HoldTime = 0;

	public static Setting ModSetting;

	public static float renderPartialTicks;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());

		try
		{
			cfg.load();

			ModSetting = new Setting();

			ModSetting.VisibleKey.Value(cfg.get("Setting", ModSetting.VisibleKey.CfgName, 64).getInt());

			ModSetting.SignVisible.Value(cfg.get("Setting", ModSetting.SignVisible.CfgName, 0).getInt());
			ModSetting.FrameVisible.Value(cfg.get("Setting",ModSetting.FrameVisible.CfgName, 0).getInt());
			ModSetting.ChestVisible.Value(cfg.get("Setting", ModSetting.ChestVisible.CfgName, 0).getInt());

			ModSetting.SignRange.Value(cfg.get("Setting", ModSetting.SignRange.CfgName, 20).getInt());
			ModSetting.FrameRange.Value(cfg.get("Setting",ModSetting.FrameRange.CfgName, 20).getInt());
			ModSetting.ChestRange.Value(cfg.get("Setting", ModSetting.ChestRange.CfgName, 128).getInt());

			ModSetting.DropOffSign.Value(cfg.get("Setting", ModSetting.DropOffSign.CfgName, 1).getInt());
			ModSetting.DropOffChest.Value(cfg.get("Setting",ModSetting.DropOffChest.CfgName, 1).getInt());

			ModSetting.Zoom_Key.Value(cfg.get("Setting", ModSetting.Zoom_Key.CfgName, 29).getInt());
			ModSetting.CheckDist.Value(cfg.get("Setting", ModSetting.CheckDist.CfgName, 1).getInt());
			ModSetting.HideBoard.Value(cfg.get("Setting", ModSetting.HideBoard.CfgName, false).getBoolean(false));

			ModSetting.CfgPath = event.getSuggestedConfigurationFile().getPath();
		}
		catch (Exception e)
		{
			//FMLLog.log(Level.SEVERE, "ErrorMessage:%s", (Object)e);
		}
		finally
		{
			cfg.save();
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		TileEntityRendererDispatcher.instance.mapSpecialRenderers.remove(TileEntitySign.class);
		TileEntityRendererDispatcher.instance.mapSpecialRenderers.put(TileEntitySign.class, new TileEntitySignRendererEx());

		TileEntityRendererDispatcher.instance.mapSpecialRenderers.remove(TileEntityChest.class);
		TileEntityRendererDispatcher.instance.mapSpecialRenderers.put(TileEntityChest.class, new TileEntityChestRendererEx());

		RenderManager.instance.entityRenderMap.remove(EntityItemFrame.class);
		RenderManager.instance.entityRenderMap.put(EntityItemFrame.class, new RenderItemFrameEx());

		if(FMLCommonHandler.instance().getSide() == Side.CLIENT)
		{
		}

		Iterator iterator = TileEntityRendererDispatcher.instance.mapSpecialRenderers.values().iterator();
		Iterator Eiterator = RenderManager.instance.entityRenderMap.values().iterator();

		while (iterator.hasNext())
		{
			TileEntitySpecialRenderer tileentityspecialrenderer = (TileEntitySpecialRenderer)iterator.next();
			tileentityspecialrenderer.func_147497_a(TileEntityRendererDispatcher.instance);
		}

		while (Eiterator.hasNext())
		{
			Render render = (Render)Eiterator.next();
			render.setRenderManager(RenderManager.instance);
		}

		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void T(TextureStitchEvent ev)
	{
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void tick(TickEvent.ClientTickEvent event)
	{
		if (Minecraft.getMinecraft().currentScreen == null)
		{
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;

			if (!key_down && Keyboard.isKeyDown(ModSetting.VisibleKey.Int()))
			{
				Minecraft.getMinecraft().displayGuiScreen(new GuiOption());

				key_down = true;
			}
			else if (key_down && !Keyboard.isKeyDown(ModSetting.VisibleKey.Int()))
			{
				key_down = false;
			}
		}
	}

	public boolean GetHoldKey()
	{
		return key_down;
	}

	private void addChatMessage(String str)
	{
		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(str));
	}


	@SubscribeEvent
	public void RenderTickEvent(TickEvent.RenderTickEvent event)
	{
		if (event.phase == TickEvent.Phase.START)
		{
			renderPartialTicks = event.renderTickTime;

			if (Minecraft.getMinecraft().thePlayer != null) DrawbleApi.beginFrustrum();
		}
		else if (event.phase == TickEvent.Phase.END)
		{

		}
	}

	public static void SaveConfig()
	{
		try
		{
			File file = new File(ModSetting.CfgPath);

			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(new FileReader(file));

			String str;

			while((str = br.readLine()) != null)
			{
				sb.append(str + "\n");
			}

			br.close();

			str = sb.toString();

			str = str.replaceAll(ModSetting.VisibleKey.OldCfg(), ModSetting.VisibleKey.Cfg());
			str = str.replaceAll(ModSetting.SignRange.OldCfg(), ModSetting.SignRange.Cfg());
			str = str.replaceAll(ModSetting.FrameRange.OldCfg(), ModSetting.FrameRange.Cfg());
			str = str.replaceAll(ModSetting.ChestRange.OldCfg(), ModSetting.ChestRange.Cfg());
			str = str.replaceAll(ModSetting.Zoom_Key.OldCfg(), ModSetting.Zoom_Key.Cfg());
			str = str.replaceAll(ModSetting.CheckDist.OldCfg(), ModSetting.CheckDist.Cfg());
			str = str.replaceAll(ModSetting.HideBoard.OldCfg(), ModSetting.HideBoard.Cfg());
			str = str.replaceAll(ModSetting.SignVisible.OldCfg(), ModSetting.SignVisible.Cfg());
			str = str.replaceAll(ModSetting.FrameVisible.OldCfg(), ModSetting.FrameVisible.Cfg());
			str = str.replaceAll(ModSetting.ChestVisible.OldCfg(), ModSetting.ChestVisible.Cfg());
			str = str.replaceAll(ModSetting.DropOffSign.OldCfg(), ModSetting.DropOffSign.Cfg());
			str = str.replaceAll(ModSetting.DropOffChest.OldCfg(), ModSetting.DropOffChest.Cfg());

			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(str);
			bw.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
