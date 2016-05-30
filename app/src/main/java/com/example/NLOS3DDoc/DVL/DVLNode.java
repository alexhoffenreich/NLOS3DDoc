package com.example.NLOS3DDoc.DVL;

import com.sap.ve.DVLTypes;
import com.sap.ve.SDVLNodeInfo;

/**
 * Created by alex-lenovi on 5/30/2016.
 */
public class DVLNode {
    private long id;
    private DVL dvl;
    private SDVLNodeInfo node_info;

    public DVLNode(long id) {
        this.id = id;
        dvl = DVL.getInstance();
        node_info = dvl.getNodeInfo(id);

    }

    public void show(){
        dvl.setNodeFlags(id, DVLTypes.DVLNODEFLAG.VISIBLE);
    }
    public void hide(){

    }

}
