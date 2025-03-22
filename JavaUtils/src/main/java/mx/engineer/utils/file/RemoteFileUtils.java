package mx.engineer.utils.file;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public final class RemoteFileUtils {
    private RemoteFileUtils() { }
    
    public static File getRemoteFile(final String filePath, final String targetPath) throws IOException {
        final File targetFile = new File(targetPath + File.separator + FilenameUtils.getName(filePath));
        if (filePath.startsWith("http://") || filePath.startsWith("https://"))
            FileUtils.copyURLToFile(new URL(filePath), targetFile);
        else
            FileUtils.copyFile(new File(filePath), targetFile);
        return targetFile;
    }
}
