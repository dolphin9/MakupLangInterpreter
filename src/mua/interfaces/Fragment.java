package mua.interfaces;

import mua.exceptions.MuaExceptions;
import mua.values.Value;

public interface Fragment {
    boolean hasNextInstruction();

    Value nextRawInstruction() throws MuaExceptions;
}
