import java.util.HashMap;

public class Viterbi {
    public static int[] compute(
            String[] obs, String[] states, double[] start_p,
            double[][] trans_p,
            double[][] emit_p){

        double[][] V = new double[obs.length][states.length];
        int[][] path = new int[states.length][obs.length];
        int[] ans = new int[obs.length];
        for (String s :states)
        {
            int y =findLocation(s, states);

            V[0][y] = start_p[y] * emit_p[0][y];
            path[y][0] = y;
        }

        for (int t = 1; t < obs.length; ++t)
        {
            int[][] newpath = new int[states.length][obs.length];
            int chose = -1;
            for (String cur : states)
            {
                double prob = -1;
                int state;
                for (String cur0 : states)
                {
                    int y = findLocation(cur, states);
                    int y0 = findLocation(cur0, states);
                    double nprob = V[t - 1][y0] * trans_p[y0][y] * emit_p[t][y];

                    if (nprob > prob)
                    {
                        prob = nprob;
                        state = y0;

                        // 记录最大概率
                        V[t][y] = prob;
                        // 记录路径
                        System.arraycopy(path[state], 0, newpath[y], 0, t);
                        newpath[y][t] = y;
                    }
                }
            }


            path = newpath;
        }

        double prob = -1;
        int state = 0;
        for (String s : states)
        {
            int y = findLocation(s,states);
            if (V[obs.length - 1][y] > prob)
            {
                prob = V[obs.length - 1][y];
                state = y;
            }
        }

        return path[state];
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
