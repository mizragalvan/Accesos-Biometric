package mx.pagos.security.structures;

import java.util.List;

import mx.pagos.admc.core.enums.ApplicationEnum;

public class Menu {
    private String factoryName;
    private String menuLevel;
    private String factoryNameParent;
    private ApplicationEnum application;
    private String factory;
    private String imagen;   
    private String type; 
    private String nombre;
    private String collapse;
    private List<Menu> subMenu;
    
    public List<Menu> getSubMenu() {
		return subMenu;
	}
	
	public void setSubMenu(List<Menu> subMenu) {
		this.subMenu = subMenu;
	}
    
    /**
	 * @return the collapse
	 */
	public String getCollapse() {
		return collapse;
	}

	/**
	 * @param collapse the collapse to set
	 */
	public void setCollapse(String collapse) {
		this.collapse = collapse;
	}

	public Menu() { }
    
    public Menu(final String factoryNameParameter) {
        this.factoryName = factoryNameParameter;
    }
    
    public Menu(final String factoryNameParameter, final String menuLevelParameter,
            final String factoryNameParentParameter) {
        this.factoryName = factoryNameParameter;
        this.menuLevel = menuLevelParameter;
        this.factoryNameParent = factoryNameParentParameter;
    }
    
    public final String getFactoryName() {
        return this.factoryName;
    }
    
    public final void setFactoryName(final String factoryNameParameter) {
        this.factoryName = factoryNameParameter;
    }
    
    public final String getMenuLevel() {
        return this.menuLevel;
    }
    
    public final void setMenuLevel(final String menuLevelParameter) {
        this.menuLevel = menuLevelParameter;
    }
    
    public final String getFactoryNameParent() {
        return this.factoryNameParent;
    }
    
    public final void setFactoryNameParent(final String factoryNameParentParameter) {
        this.factoryNameParent = factoryNameParentParameter;
    }

	/**
	 * @return the application
	 */
	public ApplicationEnum getApplication1() {
		return application;
	}
	
	/**
	 * @return the application
	 */
	public String getApplication() {		 
		 if (application == null) {
				return null;
			} else {
				return application.toString();
			}		 
	}

	/**
	 * @param application the application to set
	 */
	public void setApplication(ApplicationEnum application) {
		this.application = application;
	}
	
	/**
	 * @param application the application to set
	 */
	public void setApplication(String application) {
		if (null == application) {
			this.application = null;
		} else {
			this.application = ApplicationEnum.valueOf(application);
		}
	}

	/**
	 * @return the factory
	 */
	public String getFactory() {
		return factory;
	}

	/**
	 * @param factory the factory to set
	 */
	public void setFactory(String factory) {
		this.factory = factory;
	}

	/**
	 * @return the image
	 */
	public String getImagen() {
		return imagen;
	}

	/**
	 * @param imagen the image to set
	 */
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}
