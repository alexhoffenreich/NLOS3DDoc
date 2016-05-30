package com.example.NLOS3DDoc.Documentation.Procedure.Commands;

import com.example.NLOS3DDoc.Documentation.Procedure.DVLCommand;

/**
 * Created by alex-lenovi on 5/26/2016.
 */
public class ClearSelectionCommand extends DVLCommand{
    public ClearSelectionCommand() {
    }

    @Override
    public void fullyExecute() {
        dvl.clearSelection();
    }

    @Override
    public boolean partiallyExecute() {
        dvl.clearSelection();
        return true;
    }
}
