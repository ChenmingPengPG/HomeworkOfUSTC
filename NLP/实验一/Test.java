import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws IOException {
        //字典路径
        String dictPath = "D:\\PCM\\Desktop\\课程\\自然语言处理\\实验一\\dict_jiaba.txt";
        String corpusPath = "D:\\PCM\\Desktop\\课程\\自然语言处理\\实验一\\train2.txt";
        List<String> result = null;
        //第一题，分词法
        try {
            ReadDict rd =  new ReadDict(dictPath);
            List<String> words_dict = rd.readFileByChars();
            //String resource = "他说的确实在理";
            //String resource = "我对他有意见";
            //String resource = "今年是中国发展历史上非常重要的很不平凡的一年";
            String resource = "安徽省合肥市长江路悬挂起3３００盏大红灯笼为节日营造出千盏灯笼凌空舞十里长街别样红的欢乐祥和气氛";
            int window_size = rd.max;
            System.out.println(Solution.fmm(resource, words_dict, window_size));
            System.out.println(Solution.rmm(resource, words_dict, window_size));

            result = Solution.fr_mm(resource, words_dict, window_size);
            System.out.println(result);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //第二题，viterbi算法标注词性
        ReadCorpus rc = new ReadCorpus(corpusPath);
        //读取语料库，统计词频，词性
        rc.readFileByChars();
        //得到result即obs 和 states
        String re[] = null, states[] = null;
        if(result != null && rc.proSet!= null) {
            re =  (String[])result.toArray(new String[0]);
            states = (String[])rc.proSet.toArray(new String[0]);
        }
        assert re != null;
        //计算start_p[]
        double start_p[] = new double[states.length];
        HashMap<String,Integer> fs = rc.firstInterest;
        int num = rc.lineNum;
        for(Map.Entry<String,Integer> kv : fs.entrySet()){
            String ll = kv.getKey();
            int n = kv.getValue();
            int i = findLocation(ll,states);
            start_p[i] = (double) n /(double)num;
        }
        //计算状态转移概率trans_pp
        double trans_pp[][] = new double[states.length][states.length];
        int trans_p[][] = new int[states.length][states.length];
        HashMap<String,HashMap<String, Integer>> trans = rc.trans;
        for(Map.Entry<String, HashMap<String,Integer>> kv : trans.entrySet()){
            for(Map.Entry<String,Integer> kvv : kv.getValue().entrySet()){
                String first = kv.getKey();
                String second = kvv.getKey();
                int i = findLocation(first,states);
                int j = findLocation(second,states);
                trans_p[i][j] = kvv.getValue();
            }
        }
        for(int i = 0; i < trans_p.length; i++){
            int total_cur = trans_p.length;
            for(int j = 0; j < trans_p.length; j++){
                total_cur += trans_p[i][j];
            }
            for(int j = 0; j < trans_p.length; j++){
                trans_pp[i][j] = (double)(trans_p[i][j]+1)/(double)total_cur;
            }
        }


        //计算发射概率emit_pp
        double emit_pp[][] = new double[re.length][states.length];
        int emit_p[][] = new int[re.length][states.length];
        HashMap<String,HashMap<String, Integer>> emit = rc.hashMap;
        for(Map.Entry<String, HashMap<String,Integer>> kv : emit.entrySet()){
            for(Map.Entry<String,Integer> kvv : kv.getValue().entrySet()){
               String ss = kv.getKey();
               String pro = kvv.getKey();
               int i = findLocation(ss, re);
               int j = findLocation(pro, states);
               if((i!=-1) && (j != -1)) {
                   emit_p[i][j] = kvv.getValue();
               }
            }
        }
        for(int i = 0; i < re.length; i++){
            int total_cur = states.length;
            for(int j = 0; j < states.length; j++){
                total_cur += emit_p[i][j];
            }
            for(int j = 0; j < states.length; j++){
                emit_pp[i][j] = (double)(emit_p[i][j]+1)/(double)total_cur;
            }
        }
        int[] path = Viterbi.compute(re,states,start_p,trans_pp,emit_pp);
        for(int i = 0; i < path.length; i++){
            System.out.print(path[i]+" ");
            System.out.print(states[path[i]]+" ");
        }

    }

    public static int findLocation(String pro, String states[]){
        for(int i = 0; i < states.length; i++){
            if(pro.equals(states[i])){
                return i;
            }
        }
        return -1;
    }
}
