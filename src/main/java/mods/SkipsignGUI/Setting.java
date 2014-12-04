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
        VisibleKey = new Key(66, "ChangeModeKey", "I");
        SignVisible = new Key(0, "SignVisble", "I");
        FrameVisible = new Key(0, "FrameVisble", "I");
        ChestVisible = new Key(0, "ChestVisble", "I");
        SignRange = new Key(20, "SignDrawRange", "I");
        FrameRange = new Key(20, "FrameDrawRange", "I");
        ChestRange = new Key(128, "ChestDrawRange", "I");

        DropOffSign = new Key(1, "DropOffSign", "I");
        DropOffChest = new Key(1, "DropOffChest", "I");

        Zoom_Key = new Key(29, "ZoomKey", "I");
        CheckDist = new Key(1, "CheckDist", "I");
        HideBoard = new Key(false, "HideBackground", "B");
    }

    public static String CfgPath = "";
}
