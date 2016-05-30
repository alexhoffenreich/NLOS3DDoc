package com.example.NLOS3DDoc.Documentation.Procedure;

import com.example.NLOS3DDoc.DVL.DVL;

/**
 * Created by ADSL on 26/05/2016.
 */
public abstract class DVLCommand {
    public DVL dvl = DVL.getInstance();
    abstract public void fullyExecute();
    abstract public boolean partiallyExecute();
}
