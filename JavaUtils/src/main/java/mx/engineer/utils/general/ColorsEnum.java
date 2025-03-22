/**
 * 
 */
package mx.engineer.utils.general;

/**
 * @author Mizraim
 */
public enum ColorsEnum {

	BLUE(0xFF40BAD0), RED(0xFFE91C43), PURPLE(0xFF8A4F9E), ORANGE(0xFFF4B13D), WHITE(0xFFFFFFFF), BLACK(0xFF000000);

	private final int argb;

	ColorsEnum(final int argb) {
		this.argb = argb;
	}

	public int getArgb() {
		return argb;
	}
}
