package mods.SkipsignGUI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.Level;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = "SkipSign", version = "1.8-SNAPSHOT")
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

            ModSetting.VisibleKey.Value(cfg.get("Setting", ModSetting.VisibleKey.CfgName, 66).getInt());

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
            FMLLog.log(Level.FATAL, "preInit failed: %s", e.toString());
        }
        finally
        {
            cfg.save();
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        RenderManager renderManager = RenderManager.instance;
        
        RenderItemFrameEx renderItemFrame = new RenderItemFrameEx();
        renderItemFrame.setRenderManager(RenderManager.instance);
        renderManager.entityRenderMap.remove(EntityItemFrame.class);
        renderManager.entityRenderMap.put(EntityItemFrame.class, renderItemFrame);

        TileEntitySignRendererEx signRenderer = new TileEntitySignRendererEx();
        signRenderer.func_147497_a(TileEntityRendererDispatcher.instance);
        TileEntityRendererDispatcher.instance.mapSpecialRenderers.remove(TileEntitySign.class);
        TileEntityRendererDispatcher.instance.mapSpecialRenderers.put(TileEntitySign.class, signRenderer);

        TileEntityChestRendererEx chestRenderer = new TileEntityChestRendererEx();
        chestRenderer.func_147497_a(TileEntityRendererDispatcher.instance);
        TileEntityRendererDispatcher.instance.mapSpecialRenderers.remove(TileEntityChest.class);
        TileEntityRendererDispatcher.instance.mapSpecialRenderers.put(TileEntityChest.class, chestRenderer);

        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
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

    @SubscribeEvent
    public void RenderTickEvent(TickEvent.RenderTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            renderPartialTicks = event.renderTickTime;
    
            if (Minecraft.getMinecraft().thePlayer != null) DrawableApi.beginFrustum();
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
