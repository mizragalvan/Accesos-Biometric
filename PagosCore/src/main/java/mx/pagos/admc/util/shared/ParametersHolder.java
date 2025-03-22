package mx.pagos.admc.util.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParametersHolder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2491915940754480345L;
	private Map<String, Object> parametersMap = new HashMap<>();
	private Map<String, String> stringsMap = new HashMap<>();
	private List<ParametersMap> parametersMapList = new ArrayList<>();
	private List<StringsMap> stringsMapList = new ArrayList<>();

	public final void setParametersMap(final Map<String, Object> parametersMapParameter) {
		this.parametersMap = parametersMapParameter;
	}

	public final Map<String, String> getStringsMap() {
		return this.stringsMap;
	}

	public final void setStringsMap(final Map<String, String> stringsMapParameter) {
		this.stringsMap = stringsMapParameter;
	}

	public final Map<String, Object> getParametersMap() {
		return this.parametersMap;
	}

	public final void addParameter(final String parameterName, final Object parameterValue) {
		this.parametersMap.put(parameterName, parameterValue);
	}

	public final Object getParameterValue(final String parameterName) {
		return this.parametersMap.get(parameterName);
	}

	/**
	 * @return the parametersMapList
	 */
	public List<ParametersMap> getParametersMapList() {
		return parametersMapList;
	}

	/**
	 * @param parametersMapList the parametersMapList to set
	 */
	public void setParametersMapList(List<ParametersMap> parametersMapList) {
		this.parametersMapList = parametersMapList;
		if (null == this.parametersMap) {
			this.parametersMap = mapeaLista(parametersMapList);
		}
	}

	/**
	 * Metodo parsea lista a mapa.
	 * 
	 * @param parametersMapList2 lista
	 * @return map
	 */
	private static Map<String, Object> mapeaLista(List<ParametersMap> parametersMapList2) {
		Map<String, Object> lista = new HashMap<>();
		if (null != parametersMapList2 && !parametersMapList2.isEmpty()) {
			for (ParametersMap map : parametersMapList2) {
				lista.put(map.getKey(), map.getValue());
			}
		}
		return lista;
	}

	/**
	 * @return the stringsMapList
	 */
	public List<StringsMap> getStringsMapList() {
		return stringsMapList;
	}

	/**
	 * @param stringsMapList the stringsMapList to set
	 */
	public void setStringsMapList(List<StringsMap> stringsMapList) {
		this.stringsMapList = stringsMapList;
		if (null == this.stringsMap) {
			this.stringsMap = mapeaListaString(stringsMapList);
		}
	}

	/**
	 * Metodo parsea lista.
	 * 
	 * @param stringsMapList2 lista
	 * @return mapa
	 */
	private Map<String, String> mapeaListaString(List<StringsMap> stringsMapList2) {
		Map<String, String> lista = new HashMap<>();
		if (null != stringsMapList2 && !stringsMapList2.isEmpty()) {
			for (StringsMap map : stringsMapList2) {
				lista.put(map.getKey(), map.getValue());
			}
		}
		return lista;
	}
}
