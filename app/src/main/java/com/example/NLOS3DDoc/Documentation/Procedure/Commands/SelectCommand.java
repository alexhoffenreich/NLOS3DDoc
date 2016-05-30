package com.example.NLOS3DDoc.Documentation.Procedure.Commands;

import com.example.NLOS3DDoc.Documentation.Procedure.DVLCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex-lenovi on 5/26/2016.
 */
public class SelectCommand extends DVLCommand {
    List<String> names;


    public SelectCommand(String name) {
        names = new ArrayList<>();
        names.add(name);
    }

    public SelectCommand(List<String> names) {
        names = new ArrayList<>();
        names.addAll(names);
    }



    public void fullyExecute() {
        for (String name : names) {
            dvl.selectNodes(name);
        }
    }

    @Override
    public boolean partiallyExecute() {
        fullyExecute();
        return true;
    }
}
