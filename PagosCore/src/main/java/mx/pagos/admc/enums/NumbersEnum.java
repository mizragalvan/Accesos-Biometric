package mx.pagos.admc.enums;

public enum NumbersEnum {
	ZERO(0),
	ONE(1),
	TWO(2),
	THREE(3),
	FOUR(4),
	FIVE(5),
	SIX(6),
	SEVEN(7),
	EIGTH(8),
	NINE(9),
	TEN(10),
	ELEVEN(11),
	TWELVE(12),
	THIRTEEN(13),
	FOURTEEN(14),
	FIFTEEN(15),
	SIXTEEN(16),
	SEVENTEEN(17),
	EIGHTEEN(18),
	NINETEEN(19),
	TWENTY(20),
	FIFTY(50),
	SIXTYFOUR(64),
	SEVENTYSIX(76),
	ONEHUNDREDTWENTYEIGHT(128),
	TWO_HUNDRED_AND_FIFTY(250),
	FIVE_HUNDRED(500);

	private Integer number;

	NumbersEnum(final Integer numberParameter) {
		this.number = numberParameter;
	}

	public Integer getNumber() {
		return this.number;
	}
}
