package dev.riley0122.wutt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class FTP {
    private static final int BUFFER_SIZE = 4096;

    public static String getLocalPath(String path) {
        String[] pathParts = path.split("/");
        String fileName = pathParts[pathParts.length - 1];
        String outputPath = "cleanTheme/" + fileName;

        // Create outputPath if doesn't exist
        File outputFile = new File(outputPath);
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }
        if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return outputPath;
    }

    public static void getFile(String ip, String path) {
        String ftpUrl = "ftp://anonymous:anonymous@" + ip + "/" + path + ";type=i";
        Main.log("FTP URL: " + ftpUrl, Main.LogLevel.DEBUG);

        try {
            URL url = new URL(ftpUrl);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();            

            String outputPath = getLocalPath(path);
            FileOutputStream outputStream = new FileOutputStream(outputPath);

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            Main.log("Finished FTP transfer.", Main.LogLevel.INFO);
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
