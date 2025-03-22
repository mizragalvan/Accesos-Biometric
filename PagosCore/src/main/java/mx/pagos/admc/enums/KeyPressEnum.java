package mx.pagos.admc.enums;

public enum KeyPressEnum {
	F1 (0,"f1"),
	F2 (1,"f2"),
	F3 (2,"f3"),
	F4 (3,"f4"),
	F5 (4,"f5"),
	F6 (5,"f6"),
	F7 (6,"f7"),
	F8 (7,"f8"),
	F9 (8,"f9"),
	F10(9,"f10"),
	F11(10,"f11"),
	I1(11,"1"),
	I2(12,"2"),
	I3(13,"3"),
	I4(14,"4"),
	I5(15,"5"),
	I6(16,"6"),
	I7(17,"7"),
	I8(18,"8"),
	I9(19,"9"),
	I0(20,"0"),
	Q(21,"Q"),
	W(22,"W"),
	E(23,"E"),
	R(24,"R"),
	Y(25,"Y"),
	U(26,"U"),
	I(27,"I"),
	O(28,"O"),
	P(29,"P"),
	A(30,"A"),
	S(31,"S"),
	D(32,"D"),
	F(33,"F"),
	G(34,"G"),
	H(35,"H"),
	J(36,"J"),
	K(37,"K"),
	L(38,"L"),
	Z(39,"Z"),
	X(40,"X"),
	C(41,"C"),
	V(42,"V"),
	B(43,"B"),
	N(44,"N"),
	M(45,"M"),
	SPACE		(46,"Space"),
	TAB			(47,"Tab"),
	ESCAPE		(48,"Escape"),
	BACKSPACE	(49,"Backspace"),
	INSERT		(50,"Insert"),
	DELETE		(51,"Delete"),
	ARROW_UP	(52,"Arrow_Up"),
	ARROW_DOWN	(53,"Arrow_Down"),
    ARROW_LEFT	(54,"Arrow_Left"),
	ARROW_RIGHT	(55,"Arrow_Right"),
	HOME		(56,"Home"),
	END			(57,"End"),
	PAGE_UP		(58,"Page_Up"),
	PAGE_DOWN	(59,"Page_Down"),
	SHIFT		(60,"Shift"),
	CTRL		(61,"Ctrl"),
	ALT			(62,"Alt"),
	F12(63,"f12"),
	ENTER		(64,"Enter");

	private String desc;
	private int id;

	KeyPressEnum (int id,String desc){
		this.desc=desc;
		this.id=id;
	}	
	
	public String toString() {
		return desc;
	}
	
	public int getId(){
		return this.id;
	}
}
