package musicDealer;

import java.io.*;


public class Console {
    public static void call(String fileName, String songUrl) throws IOException {
        String command = "yt-dlp" +
                " -f \"bestaudio[filesize<49M][ext=m4a]\" -P /root/home/resources/" +
                "Audio/downloads/ -o " + fileName + ".m4a " + songUrl; // Example command to list files

        Process process = Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c", command});
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
}
