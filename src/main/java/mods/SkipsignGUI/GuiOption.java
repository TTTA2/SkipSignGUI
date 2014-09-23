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

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.java.games.input.Keyboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.settings.GameSettings.Options;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.client.settings.KeyBinding;

@SideOnly(Side.CLIENT)
public class GuiOption extends GuiScreen
{
	public void drawScreen(int par1, int par2, float par3)
	{
		FontRenderer fontRenderer = this.fontRendererObj;    	
		int x = this.width / 2;
		int y = this.height / 2;
		
		this.drawDefaultBackground();

		this.drawString(fontRenderer, "環境設定", x - 172, y - 90, 16777215);
		
		this.drawString(fontRenderer, "看板", x - 172, y - 75 	  + 5, 16777215);
		this.drawString(fontRenderer, "フレーム", x - 172, y - 50 + 5, 16777215);
		this.drawString(fontRenderer, "チェスト", x - 172, y - 25 + 5, 16777215);
		
		super.drawScreen(par1, par2, par3);
	}

	private GuiButton drawMode, ShowBoard, ChangeKey, ChangeMode, ZoomKey, ScrGui;
	private GuiButton ApplySign, ApplyItemFrame, ApplyChest;
	private GuiButton SignDO, ChestDO;
	private boolean KeyChange_OpenSetting = false;
	private boolean KeyChange_ZoomKey = false;

	public void initGui()
	{
		FontRenderer fontRenderer = this.fontRendererObj;    
		
		this.buttonList.clear();

		int x = this.width / 2;
		int y = this.height / 2;
		
		SignDO = new GuiButton(0, x + 65, y - 75, 120, 20, "範囲外を描画しない");
		ChestDO = new GuiButton(0, x + 65, y - 25, 120, 20, "範囲外を描画しない");
		
		ScrGui = new GuiButton(0, x - 172, y - 5, 195, 20, "画面外のチェスト・看板を非表示"); 

		drawMode = new GuiButton(0, x - 172, y - 75, 60, 20, "範囲描画");
		
		ApplySign = new GuiButton(0, x - 120, y - 75, 75, 20, "範囲描画");
		ApplyItemFrame = new GuiButton(0, x - 120, y - 50, 75, 20, "範囲描画");
		ApplyChest = new GuiButton(0, x - 120, y - 25, 75, 20, "範囲描画");
		
		GuiOptionSliderEx Srange = new GuiOptionSliderEx(5, x - 40, y - 75, "描画範囲", SkipsignCore.ModSetting.SignRange, (float)SkipsignCore.ModSetting.Hurihaba);
		GuiOptionSliderEx Frange = new GuiOptionSliderEx(5, x - 40, y - 50, "描画範囲", SkipsignCore.ModSetting.FrameRange, (float)SkipsignCore.ModSetting.Hurihaba);
		GuiOptionSliderEx Crange = new GuiOptionSliderEx(5, x - 40, y - 25, "描画範囲", SkipsignCore.ModSetting.ChestRange, (float)SkipsignCore.ModSetting.Hurihaba);
		
		//AllDraw = new GuiButton(1, x - 107, y - 75, 60, 20, "すべて描画");
		//SkipDraw = new GuiButton(2, x - 42, y - 75, 60, 20, "描画しない");

		ChangeKey = new GuiButton(3, x - 77, y + 20, 100, 20, String.format("設定画面:%s", org.lwjgl.input.Keyboard.getKeyName((SkipsignCore.ModSetting.VisibleKey.Int()))));
		ShowBoard = new GuiButton(4, x - 172, y + 20, 90, 20, "本体を表示");

		ChangeMode = new GuiButton(6, x - 172, y + 45, 90, 20, "距離算出式:0");

		ZoomKey = new GuiButton(7, x - 77, y + 45, 100, 20, String.format("一時解除:%s", org.lwjgl.input.Keyboard.getKeyName(SkipsignCore.ModSetting.VisibleKey.Int())));


		Invalidate();

		this.buttonList.add(ApplySign);
		this.buttonList.add(ApplyItemFrame);
		this.buttonList.add(ApplyChest);
		
		this.buttonList.add(Srange);
		this.buttonList.add(Frange);
		this.buttonList.add(Crange);
		
		this.buttonList.add(SignDO);
		this.buttonList.add(ChestDO);
		
//		this.field_146292_n.add(drawMode);
//		this.field_146292_n.add(ScrGui);
		this.buttonList.add(ChangeKey);
		this.buttonList.add(ShowBoard);
		this.buttonList.add(ChangeMode);
		this.buttonList.add(ZoomKey);
//		this.field_146292_n.add(range);
	}
	
	

	protected void actionPerformed(GuiButton p_146284_1_)
	{
		KeyChange_OpenSetting = false;
		KeyChange_ZoomKey = false;
		
		if (ApplySign == p_146284_1_)
		{
			int vis = SkipsignCore.ModSetting.SignVisible.Int();
			vis++; if (vis > 2) vis = 0;
			SkipsignCore.ModSetting.SignVisible.Value = vis;
		}
		
		if (ApplyItemFrame == p_146284_1_)
		{
			int vis = SkipsignCore.ModSetting.FrameVisible.Int();
			vis++; if (vis > 2) vis = 0;
			SkipsignCore.ModSetting.FrameVisible.Value = vis;
		}
		
		if (ApplyChest == p_146284_1_)
		{
			int vis = SkipsignCore.ModSetting.ChestVisible.Int();
			vis++; if (vis > 2) vis = 0;
			SkipsignCore.ModSetting.ChestVisible.Value = vis;
		}
		
		if (SignDO == p_146284_1_)
		{
			int vis = SkipsignCore.ModSetting.DropOffSign.Int();
			vis++; if (vis > 1) vis = 0;
			SkipsignCore.ModSetting.DropOffSign.Value = vis;
		}
		
		if (ChestDO == p_146284_1_)
		{
			int vis = SkipsignCore.ModSetting.DropOffChest.Int();
			vis++; if (vis > 1) vis = 0;
			SkipsignCore.ModSetting.DropOffChest.Value = vis;
		}
		
		if (p_146284_1_.id == 4)
		{
			SkipsignCore.ModSetting.HideBoard.Value = !SkipsignCore.ModSetting.HideBoard.Bool();
		}

		if (p_146284_1_.id == 3)
		{
			KeyChange_OpenSetting = !KeyChange_OpenSetting;
		}
		
		if (p_146284_1_.id == 7)
		{
			KeyChange_ZoomKey = !KeyChange_ZoomKey;
		}

		if (p_146284_1_.id == 6)
		{
			SkipsignCore.ModSetting.CheckDist.Value = ((SkipsignCore.ModSetting.CheckDist.Int() == 0)) ?  1 : 0;
		}

		Invalidate();
	}

	private void Invalidate()
	{
		switch (SkipsignCore.ModSetting.SignVisible.Int())
		{
		case 0:
			ApplySign.displayString = "範囲描画";
			break;
		case 1:
			ApplySign.displayString = "すべて描画";
			break;
		case 2:
			ApplySign.displayString = "描画しない";
			break;
		}
		
		switch (SkipsignCore.ModSetting.FrameVisible.Int())
		{
		case 0:
			ApplyItemFrame.displayString = "範囲描画";
			break;
		case 1:
			ApplyItemFrame.displayString = "すべて描画";
			break;
		case 2:
			ApplyItemFrame.displayString = "描画しない";
			break;
		}
		
		switch (SkipsignCore.ModSetting.ChestVisible.Int())
		{
		case 0:
			ApplyChest.displayString = "範囲描画";
			break;
		case 1:
			ApplyChest.displayString = "すべて描画";
			break;
		case 2:
			ApplyChest.displayString = "描画しない";
			break;
		}
		
		switch (SkipsignCore.ModSetting.DropOffSign.Int())
		{
		case 0:
			SignDO.displayString = "範囲外を描画する";
			break;
		case 1:
			SignDO.displayString = "範囲外を描画しない";
			break;
		}
		
		switch (SkipsignCore.ModSetting.DropOffChest.Int())
		{
		case 0:
			ChestDO.displayString = "範囲外を描画する";
			break;
		case 1:
			ChestDO.displayString = "範囲外を描画しない";
			break;
		}

		if (SkipsignCore.ModSetting.HideBoard.Bool())
		{
			ShowBoard.displayString = "背景を非表示";
		}
		else
		{
			ShowBoard.displayString = "背景を表示";
		}

		if (KeyChange_OpenSetting)
		{
			ChangeKey.displayString = "キーを入力してください";
		}
		else
		{
			ChangeKey.displayString = String.format("設定画面:%s", org.lwjgl.input.Keyboard.getKeyName(SkipsignCore.ModSetting.VisibleKey.Int()));
		}
		
		if (KeyChange_ZoomKey)
		{
			ZoomKey.displayString = "キーを入力してください";
		}
		else
		{
			ZoomKey.displayString = String.format("一時解除:%s", org.lwjgl.input.Keyboard.getKeyName(SkipsignCore.ModSetting.Zoom_Key.Int()));
		}

		ChangeMode.displayString = String.format("距離算出式:%d", SkipsignCore.ModSetting.CheckDist.Int());
	}

	public void func_146281_b()
	{
		SkipsignCore.SaveConfig();
	}

	protected void keyTyped(char par1, int par2)
	{
		super.keyTyped(par1, par2);
		
		if (KeyChange_ZoomKey && par2 != 1)
		{
			SkipsignCore.ModSetting.Zoom_Key.Value = par2;

			KeyChange_ZoomKey = false;
		}

		if (KeyChange_OpenSetting && par2 != 1)
		{
			SkipsignCore.ModSetting.VisibleKey.Value = par2;

			KeyChange_OpenSetting = false;
		}

		Invalidate();
	}

	//	public GuiOption()
	//	{
		//		super(new ContainerOption());		
	//	}
	//
	//	public GuiOption(EntityPlayer player, World world, int x, int y, int z)
	//	{
	//		super(new ContainerOption(player, world, x, y, z));		
	//	}
	//
	//	@Override
	//	protected void func_146979_b(int p_146979_1_, int p_146979_2_)
	//	{
	//		drawGuiContainerForegroundLayer(p_146979_1_, p_146979_2_);
	//	}
	//
	//	@Override
	//	protected void func_146976_a(float var1, int var2, int var3) 
	//	{
	//		drawGuiContainerBackgroundLayer(var1, var2, var3);
	//	}
	//
	//	private int xStart, yStart;
	//	
	//	private void drawGuiContainerForegroundLayer(int var1, int va2)
	//	{
	//		FontRenderer fontRenderer = this.field_146289_q;
	//		//fontRenderer.drawString("Gui Sample", 58, 6, 0x404040);
	//		fontRenderer.drawString("�`��ݒ�", -18, 50, 0x404040);
	////		fontRenderer.drawString("�`��ݒ�", 8, this.field_147000_g - 96 + 2, 0x404040);
	//	}
	//
	//	private void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	//	{
	//		Minecraft mc = this.field_146297_k;
	//		int width = this.field_146294_l;
	//		int height = field_146295_m;
	//		int xSize = 229;
	//		int ySize = 78;
	//
	//		/*int xSize = field_146999_f;*/
	//		/*int ySize = field_147000_g;*/
	//
	//		//GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	//
	//		mc.renderEngine.bindTexture((new ResourceLocation("skipsignf", "textures/gui/background.png")));
	//		xStart = width - xSize >> 1;
	//		yStart = height - ySize >> 1;
	//		//drawTexturedModalRect(xStart, yStart, 0, 0, xSize, ySize);
	//	}
	//
	//    public void drawScreen(int par1, int par2, float par3)
	//    {
	//    	
	//    }
}
