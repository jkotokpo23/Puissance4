package gui.util;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GuiConstants {

	public static final String CONNECT_4_TITLE = "Puissance 4";
	
	public static final String VERSION = "1.0.0";

	public static final long AI_MOVE_MILLISECONDS = 100L;

	public static final String CONNECT_4_BOARD_IMG_PATH = "images/Board4.png";

	public static final int DEFAULT_CONNECT_4_WIDTH = 600;
	public static final int DEFAULT_CONNECT_4_HEIGHT = 635;

	public static final int DEFAULT_CONNECT_5_WIDTH = 670;
	public static final int DEFAULT_CONNECT_5_HEIGHT = 710;

}
