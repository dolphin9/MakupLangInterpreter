package mua.values;

import mua.interfaces.Fragment;

public class CodeFragment extends ListValue implements Fragment {
    private int mInstructionPointer = 0;

    public CodeFragment(ListValue list) {
        super(list.mList);
    }

    @Override
    public boolean hasNextInstruction() {
        return mInstructionPointer < mList.size();
    }

    @Override
    public Value nextRawInstruction() {
        return mList.get(mInstructionPointer++);
    }

    public void resetPointer() {
        mInstructionPointer = 0;
    }
}
