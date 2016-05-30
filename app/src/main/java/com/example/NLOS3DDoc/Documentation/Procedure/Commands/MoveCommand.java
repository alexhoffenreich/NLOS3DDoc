package com.example.NLOS3DDoc.Documentation.Procedure.Commands;

import com.example.NLOS3DDoc.DVL.DVL;
import com.example.NLOS3DDoc.Documentation.Procedure.DVLCommand;
import com.sap.ve.SDVLNodeInfo;

import java.util.List;

/**
 * Created by alex-lenovi on 5/26/2016.
 */
public class MoveCommand extends DVLCommand {
    List<Long> nodes;
    float delta_x, delta_y, delta_z;

    public MoveCommand(List<Long> nodes, float delta_x, float delta_y, float delta_z) {

    }


    @Override
    public void fullyExecute() {

    }

    @Override
    public boolean partiallyExecute() {
        return false;
    }
}
