import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ReadDict {
    List<String> words_dict = new ArrayList<>();
    String path;
    int max = -1;
    ReadDict(String path){
        this.path = path;
    }
    List<String> readFileByChars() throws IOException {
        //File file = new File(path);
        FileInputStream fis = new FileInputStream(path);
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        String line ="";

        while((line = br.readLine()) != null){
            String temp = line.split(" ")[0];
            words_dict.add(temp);
            if(temp.length() > max){
                max = temp.length();
            }
            //System.out.println(line);
        }
        return words_dict;
    }

}
