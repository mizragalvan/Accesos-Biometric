package mx.pagos.admc.util.shared;

import java.io.Serializable;
import java.util.List;

public class ConsultaList<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<T> list;
	private String param1;
	private String param2;
	private String param3;
	private Integer param4;
	private Integer param5;
	private Integer param6;
	
	public final List<T> getList() {
		return this.list;
	}

	public final void setList(final List<T> listP) {
		this.list = listP;
	}

	public final void setParam1(final String param1P) {
		this.param1 = param1P;
	}
	
	public final String getParam1() {
		return this.param1;
	}

	public final String getParam2() {
		return this.param2;
	}

	public final void setParam2(final String param2P) {
		this.param2 = param2P;
	}

	public final String getParam3() {
		return this.param3;
	}

	public final void setParam3(final String param3P) {
		this.param3 = param3P;
	}

	public final Integer getParam4() {
		return this.param4;
	}

	public final void setParam4(final Integer param4P) {
		this.param4 = param4P;
	}

	public final Integer getParam5() {
		return this.param5;
	}

	public final void setParam5(final Integer param5Parameter) {
		this.param5 = param5Parameter;
	}

	public final Integer getParam6() {
		return this.param6;
	}

	public final void setParam6(final Integer param6Parameter) {
		this.param6 = param6Parameter;
	}
}

