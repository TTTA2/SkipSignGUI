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

public class Setting 
{
	public static int Hurihaba = 128;

	public static Key VisibleKey;
	public static Key SignVisible;
	public static Key FrameVisible;
	public static Key ChestVisible;
	public static Key SignRange;
	public static Key FrameRange;
	public static Key ChestRange;
	public static Key Zoom_Key;
	public static Key CheckDist;
	public static Key HideBoard;
	
	public static Key DropOffSign;
	public static Key DropOffChest;
	
	public Setting()
	{
		VisibleKey = new Key(64, "ChangeModeKey", "I");
		SignVisible = new Key(0, "SignVisble", "I");
		FrameVisible = new Key(0, "FrameVisble", "I");
		ChestVisible = new Key(0, "ChestVisble", "I");
		SignRange = new Key(5, "SignDrawRange", "I");
		FrameRange = new Key(5, "FrameDrawRange", "I");
		ChestRange = new Key(5, "ChestDrawRange", "I");
		Zoom_Key = new Key(29, "ZoomKey", "I");
		CheckDist = new Key(0, "CheckDist", "I");
		HideBoard = new Key(false, "HideBackground", "B");
		
		DropOffSign = new Key(1, "DropOffSign", "I");
		DropOffChest = new Key(1, "DropOffChest", "I");
	}
	
	public static String CfgPath = "";
}
