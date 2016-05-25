package com.example.NLOS3DDoc.AnimationHandler;

/**
 * Created by alex-lenovi on 5/25/2016.
 */
public interface ProcedureEventHandler {
    public void onEndOfStep(Step step, Procedure procedure);
    public void onEndOfProcedure(Procedure procedure);
    public void onEndOfOperation (Operation operation, Step step, Procedure procedure);
}
