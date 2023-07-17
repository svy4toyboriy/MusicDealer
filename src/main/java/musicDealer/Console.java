package musicDealer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Console {
    public static void call(String fileName, String songUrl) throws IOException {
        Process process = Runtime.getRuntime().exec("cmd /c  yt-dlp -x --audio-format mp3" +
                " --audio-quality 0 -P D:\\Audio\\downloads -o " + fileName + ".mp3 " + songUrl,
                null, new File("D:\\Audio\\"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
}
