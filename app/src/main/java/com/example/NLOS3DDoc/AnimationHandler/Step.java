package com.example.NLOS3DDoc.AnimationHandler;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex-lenovi on 5/24/2016.
 */
public class Step {
    private String id;
    private String title;
    private String description;
    private List<Operation> operations;
    private Operation cur_operation;
    private Element step_element;
    private Procedure procedure;

    public Step(Element step_element, Procedure procedure) {
        this.step_element = step_element;
        this.procedure = procedure;
        operations = new ArrayList<>();
        NodeList op_nodes = step_element.getChildNodes();
        for (int i=0;i<op_nodes.getLength(); i++){
            operations.add(new Operation(this,(Element) op_nodes.item(i)));
        }
    }


    public void start() {
        cur_operation = operations.get(0);
        cur_operation.execute();
        procedure.handleEndOfOperation();
        if (cur_operation.equals(operations.get(operations.size() - 1)))
            procedure.handleEndOfStep();
    }


    public Procedure getProcedure() {
        return procedure;
    }

    public Operation getCurrentOperation() {
        return cur_operation;
    }
}
