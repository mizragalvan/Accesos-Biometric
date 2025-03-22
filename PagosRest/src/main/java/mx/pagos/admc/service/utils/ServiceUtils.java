package mx.pagos.admc.service.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;



public final class ServiceUtils {
    private static final String SCAPED_QUOTES = "\"";
    
    private ServiceUtils() { }
    

	private static final Logger Log = Logger.getLogger(ServiceUtils.class);
    
    public static void sendFileToDownload(final HttpServletResponse response, final File file)
            throws FileNotFoundException, IOException {
    	
    	Log.info("sendFileToDownload :: file ("+file.getAbsolutePath()+")");
    	
        setReponseData(response, file.getName(), file);
        final FileInputStream fileInputStream = new FileInputStream(file);
        final ServletOutputStream servletOutputStream = response.getOutputStream();
        IOUtils.copy(fileInputStream, servletOutputStream);
        fileInputStream.close();
        servletOutputStream.flush();
        servletOutputStream.close();
    }
    
    private static void setReponseData(final HttpServletResponse response, final String name, final File file) {
        final int buffer = 1024 * 100;
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=" + SCAPED_QUOTES + name + SCAPED_QUOTES);
        response.setContentLength(Long.valueOf(file.length()).intValue());
        response.setBufferSize(buffer);
    }
    
    public static void sendFileToDownloadAndDeleteFile(final HttpServletResponse response, final File file)
            throws FileNotFoundException, IOException {
        sendFileToDownload(response, file);
        if (file.exists())
            file.delete();
    }
}
