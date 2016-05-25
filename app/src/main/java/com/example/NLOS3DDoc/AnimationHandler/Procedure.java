package com.example.NLOS3DDoc.AnimationHandler;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex-lenovi on 5/24/2016.
 */
public class Procedure {
    private String id;
    private String title;
    private List<Step> steps;
    private Step cur_step;
    private ProcedureEventHandler procedure_event;
    private Node procedure_node;


    public Procedure(Node procedure_node) {
        this.procedure_node = procedure_node;
        steps = new ArrayList<>();

        //todo: populate steps
        // TODO: 5/25/2016 set private members

    }

    public boolean moveToNextStep (){
        if (!isLastStep()){
            cur_step = steps.get(steps.lastIndexOf(cur_step)+1);
            cur_step.execute();
            return true;
        } else
        {
            return false;
        }
    }

    public boolean moveToPreviousStep (){
        if (!isFirstStep()){
            cur_step = steps.get(steps.lastIndexOf(cur_step)-1);
            cur_step.execute();
            return true;
        } else
        {
            return false;
        }

    }

    public void moveToStep(String step_id){

    }
    public void moveToFirst(){

    }
    public void moveToLast(){

    }
    public boolean isFirstStep(){
        return cur_step.equals(steps.get(steps.size()-1));
    }
    public boolean isLastStep(){
        return cur_step.equals(steps.get(0));
    }


    public void handleEndOfStep() {
        if (procedure_event!= null){
            procedure_event.onEndOfStep(cur_step, this);
            if (isLastStep()) procedure_event.onEndOfProcedure(this);
        }
    }

    public void handleEndOfOperation() {
        procedure_event.onEndOfOperation(cur_step.getCurrentOperation(), cur_step, this);
    }
}
