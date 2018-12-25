package sc.ustc.utils;

import sc.ustc.items.Action;
import sc.ustc.myInterface.ExecuteHelper;

public class ExecutHelperImp implements ExecuteHelper {
    @Override
    public String doNothing(Action action) {
        System.out.println("I did nothing!");
        return "I DO NOTHING";
    }
}
