package mx.pagos.admc.contracts.structures;

import mx.pagos.admc.enums.RecordStatusEnum;

public class ScreenMenuGroup {
	
	private Integer idScreenMenuGroup;
	private Screen screen;
	private MenuGroup menuGroup;
	private RecordStatusEnum status;

	public final Integer getIdScreenMenuGroup() {
		return this.idScreenMenuGroup;
	}

	public final void setIdScreenMenuGroup(final Integer idScreenMenuGroupParameter) {
		this.idScreenMenuGroup = idScreenMenuGroupParameter;
	}

	public final Screen getScreen() {
		return this.screen;
	}

	public final void setScreen(final Screen screenParameter) {
		this.screen = screenParameter;
	}

	public final MenuGroup getMenuGroup() {
		return this.menuGroup;
	}

	public final void setMenuGroup(final MenuGroup menuGroupParameter) {
		this.menuGroup = menuGroupParameter;
	}

	public final RecordStatusEnum getStatus() {
		return this.status;
	}

	public final void setStatus(final RecordStatusEnum statusParameter) {
		this.status = statusParameter;
	}
	
	
	

}
