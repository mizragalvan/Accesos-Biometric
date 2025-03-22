package mx.engineer.utils.general;

public class ValidatePathSistem {
	
	public static String getUrlSistem(String url) {
		if(!System.getProperty("os.name").toLowerCase().contains("windows")) {
			url = url.replace("C:\\", "\\");
			url = url.replace("R:\\", "\\");
			url = url.replace("\\", "/");
			return url;
		}
		return url;			
	}
	
	public static String getSeparatorSistem() {
		if(System.getProperty("os.name").toLowerCase().contains("windows")) {
			return "\\";
		}
		return "/";			
	}

}
