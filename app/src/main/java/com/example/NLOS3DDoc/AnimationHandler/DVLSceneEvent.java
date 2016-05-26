package com.example.NLOS3DDoc.AnimationHandler;

import com.sap.ve.DVLCore;
import com.sap.ve.DVLRenderer;
import com.sap.ve.DVLScene;
import com.sap.ve.SDVLNodeInfo;

import java.util.List;

/**
 * Created by alex-lenovi on 5/26/2016.
 */
public interface DVLSceneEvent {
    void onSelectionChanged(DVLCore core, DVLRenderer renderer, DVLScene scene, List<SDVLNodeInfo> selected_nodes);
}
