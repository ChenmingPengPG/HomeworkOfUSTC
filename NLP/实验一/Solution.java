import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Solution {
    static int window_size = 8;
    static List<String> fmm(String resource, List<String> dict, int window_size){
        int length = dict.size();
        int index = 0;
        List<String> words = new ArrayList<>();
        window_size = resource.length() < window_size ? resource.length() : window_size;
        while(index < resource.length()){
            boolean match = false;
            for(int i = window_size; i > 0; i--){
                if(index + i <= resource.length()){
                    String subStr = resource.substring(index, index+i);
                    if(dict.contains(subStr)){
                        match = true;
                        words.add(subStr);
                        index += i;
                        break;
                    }
                }
            }
            if(!match){
                words.add(String.valueOf(resource.charAt(index)));
                index += 1;
            }
        }
        return words;
    }
    static List<String> rmm(String resource, List<String> dict, int window_size){
        window_size = resource.length() < window_size ? resource.length() : window_size;
        int index = resource.length();
        List<String> words = new ArrayList<>();
        while(index > 0){
            boolean match = false;
            for(int i = window_size; i > 0; i--){
                if(index - i > 0){
                    String subStr = resource.substring(index - i, index);
                    if(dict.contains(subStr)){
                        match = true;
                        words.add(subStr);
                        index -= i;
                        break;
                    }
                }
            }
            if(!match){
                words.add(String.valueOf(resource.charAt(index-1)));
                index -= 1;
            }
        }
        Collections.reverse(words);
        return words;
    }

    static List<String> fr_mm(String resource, List<String> dict, int window_size){
        List<String> words_f = fmm(resource, dict, window_size);
        List<String> words_r = rmm(resource, dict, window_size);
        /*System.out.println(words_f);
        System.out.println(words_r);*/

        //单字词个数
        int f_single_num = 0;
        int r_single_num = 0;
        //总词数
        int total_f = words_f.size();
        int total_r = words_r.size();
        //非字典词数
        int oov_fmm = 0;
        int oov_rmm = 0;
        //罚分，罚分值越低越好
        int score_fmm = 0;
        int score_rmm = 0;

        //两个结果一样返回任意一个
        if(same(words_f, words_r)){
            return words_f;
        }else{
            for(String l : words_f){
                if(l.length() == 1){
                    f_single_num += 1;
                }
            }
            for(String l : words_r){
                if(l.length() == 1){
                    r_single_num += 1;
                }
            }
            for(String l : words_f){
                if(!dict.contains(l)){
                    oov_fmm += 1;
                }
            }
            for(String l : words_r){
                if(!dict.contains(l)){
                    oov_rmm += 1;
                }
            }
            //这里都罚分值都为1分
            //非字典词越少越好
            if(oov_fmm > oov_rmm){
                score_fmm += 1;
            }else if(oov_fmm < oov_rmm){
                score_rmm += 1;
            }
            //总词数越少越好
            if(total_f > total_r){
                score_fmm += 1;
            }else if(total_f < total_r){
                score_rmm += 1;
            }
            //单字词越少越好
            if(f_single_num > r_single_num){
                score_fmm += 1;
            }else if(f_single_num < r_single_num){
                score_rmm += 1;
            }
            return score_fmm < score_rmm ? words_f : words_r;
        }

    }
    static boolean same(List<String> list1, List<String> list2){
        if(list1.size() != list2.size()){
            return false;
        }else{
            for(int i = 0; i < list1.size(); i++){
                if(!list1.get(i).equals(list2.get(i))){
                    return false;
                }
            }
            return true;
        }

    }
}
