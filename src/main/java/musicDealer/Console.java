package musicDealer;

import java.io.*;


public class Console {
    public static void call(String fileName, String songUrl) throws IOException {
        Process process = Runtime.getRuntime().exec("cmd /c  yt-dlp" +
                        " -f \"bestaudio[filesize<49M][ext=m4a]\" -P D:\\Audio\\downloads -o " + fileName + ".m4a " + songUrl,
                null, new File("D:\\Audio\\"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
}
