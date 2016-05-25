package com.example.NLOS3DDoc.AnimationHandler;

import org.w3c.dom.Element;

/**
 * Created by alex-lenovi on 5/24/2016.
 */
public class Operation {
    private Step step;
    private Element operation_element;

    public Operation(Step step, Element operation_element) {
        this.operation_element = operation_element;
        this.step = step;
    }

    public void execute() {
        switch (operation_element.getTagName().toLowerCase()){
            case "remove":
                break;
            case "zoom":

                break;
            case "hide":
                break;
            case "show":
                break;
            case "opacity":
                break;
        }
    }
}
