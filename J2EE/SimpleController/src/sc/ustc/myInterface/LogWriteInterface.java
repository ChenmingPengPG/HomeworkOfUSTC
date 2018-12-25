package sc.ustc.myInterface;

import sc.ustc.items.Action;

public interface LogWriteInterface {
    void preAction(Action action);
    void afterAction(Action action);
}
