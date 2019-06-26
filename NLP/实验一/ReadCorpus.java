import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ReadCorpus {
    String path = "D:\\PCM\\Desktop\\课程\\自然语言处理\\实验一\\train2.txt";
    HashMap<String, HashMap<String, Integer>> Corpus = new HashMap<>();

    HashMap<String, Integer> firstInterest = new HashMap<>(); //记录句子first单词性质
    HashMap<String, Integer> lastInterest = new HashMap<>(); //记录句子last单词性质
    HashMap<String,HashMap<String, Integer>> trans = new HashMap<>(); //句子转移概率表
    HashMap<String, HashMap<String, Integer>> hashMap = new HashMap<>();
    HashSet<String> proSet = new HashSet<>();
    int lineNum = 0;
    ReadCorpus(String path){
        this.path = path;
    }
    ReadCorpus(){
    }
    void readFileByChars() throws IOException {
        FileInputStream fis = new FileInputStream(path);
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        String line = "";
        while((line = br.readLine()) != null){
            //读取一行句子，分割
            String[] temp = line.split("\\s+"); //它的空格可能不都是两个，也有可能是1个，蛋疼
            //忽略掉[ 、]
            for(int i = 0; i < temp.length; i++){
                if(temp[i].contains("[")){
                    temp[i] = temp[i].replace("[","");
                }
                if(temp[i].contains("]")){
                    int index = temp[i].lastIndexOf("]");
                    temp[i] = temp[i].substring(0, index);
                }
            }
            if(temp.length>=2 && temp[1].split("/").length == 2) {
                //默认每行第二个为一个句子的开头
                //记录句子初始性质 统计, 不考虑。 /w 开头的情况
                String firstinterest = (temp[1].split("/"))[1];//句子第一个词的词性
                Integer count = firstInterest.get(firstinterest);
                this.lineNum++;
                if (count == null) {
                    firstInterest.put(firstinterest, 1);
                } else {
                    firstInterest.put(firstinterest, count + 1);
                }

                String lastPro = firstinterest;
                for(int i = 2; i < temp.length; i++){
                    //记录数据到hashMap中，便于计算发射概率,记录可能的词性
                    String forfinal[] = temp[i].split("/");
                    if(forfinal.length == 2){
                        HashMap<String, Integer> hh = hashMap.get(forfinal[0]);
                        proSet.add(forfinal[1]);
                        if(hh == null){
                            hh = new HashMap<String, Integer>();
                            hh.put(forfinal[1],1);
                            hashMap.put(forfinal[0], hh);
                        }else{
                            Integer count3 = hh.get(forfinal[1]);
                            if(count3 == null){
                                hh.put(forfinal[1], 1);
                            }else{
                                hh.put(forfinal[1], count3+1);
                            }
                        }
                    }
                    //记录最后的词性
                    if(forfinal.length == 2 &&
                            (forfinal[0].equals("。")||forfinal[0].equals("!")||forfinal[0].equals("?"))){
                        Integer count1 = lastInterest.get(lastPro);
                        if(count1 == null){
                            lastInterest.put(lastPro, 1);
                        }else{
                            lastInterest.put(lastPro, count1+1);
                        }
                    }

                    //记录转移
                    else if(!temp[i].equals("/w") && !temp[i].equals("。") && !lastPro.equals("。")){
                        String ttemp[] = temp[i].split("/");
                        HashMap<String,Integer> hs = trans.get(lastPro);
                        if(hs == null){
                            hs = new HashMap<>();
                            hs.put(ttemp[1],1);
                            trans.put(lastPro, hs);
                        }else{
                            Integer count2 = hs.get(ttemp[1]);
                            if(count2 == null){
                                hs.put(ttemp[1],1);
                            }else{
                                hs.put(ttemp[1], count2+1);
                            }
                        }

                    }
                    //更新lastPro
                    if(temp[i].split("/").length == 2){
                        lastPro = temp[i].split("/")[1];
                    }else if(temp[i].equals("。")){
                        lastPro = "。";
                    }

                }
            }

        }
        System.out.println("finished");
    }

}
