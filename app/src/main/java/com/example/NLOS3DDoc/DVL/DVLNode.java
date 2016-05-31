package com.example.NLOS3DDoc.DVL;

import android.opengl.Matrix;

import com.sap.ve.DVLCore;
import com.sap.ve.DVLRenderer;
import com.sap.ve.DVLScene;
import com.sap.ve.DVLTypes;
import com.sap.ve.SDVLMatrix;
import com.sap.ve.SDVLMetadataNameValuePair;
import com.sap.ve.SDVLNodeInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alex-lenovi on 5/30/2016.
 */
public class DVLNode {
    private long id;
    private DVL dvl;
    private SDVLNodeInfo node_info;
    private Map<String, String> meta_data;
    private DVLCore core;
    private DVLRenderer renderer;
    private DVLScene scene;

    public DVLNode(long id) {
        this.id = id;
        dvl = DVL.getInstance();
        node_info = dvl.getNodeInfo(id);
        core = dvl.getCore();
        renderer = dvl.getRenderer();
        scene = dvl.getScene();

        meta_data = new HashMap<>();
        ArrayList<SDVLMetadataNameValuePair> meta = new ArrayList<>();
        dvl.getScene().RetrieveMetadata(id, meta);
        for (SDVLMetadataNameValuePair m : meta) {
            meta_data.put(m.name, m.value);
        }

    }

    public String getMetadata(String name) {
        if (meta_data.containsKey(name)) return meta_data.get(name);
        else return "[NOT FOUND]";
    }

    public String getName() {
        return node_info.nodeName;
    }

    public List<DVLNode> getChildNodes() {
        List<DVLNode> child_nodes = new ArrayList<>();
        for (Long i : node_info.childNodes) {
            child_nodes.add(new DVLNode(i));
        }
        return child_nodes;
    }

    public void move(float delta_x, float delta_y, float delta_z) {
        SDVLMatrix mat = getMatrix();
        Matrix.translateM(mat.m, 0, delta_x, delta_y, delta_z);
        setMatrix(mat);
    }

    public void rotate(float angle, float delta_x, float delta_y, float delta_z) {
        SDVLMatrix mat = getMatrix();
        Matrix.rotateM(mat.m, 0, angle, delta_x, delta_y, delta_z);
        setMatrix(mat);
    }

    public void scale(float angle, float x_scale, float y_scale, float z_scale) {
        SDVLMatrix mat = getMatrix();
        Matrix.scaleM(mat.m, 0, x_scale, y_scale, z_scale);
        setMatrix(mat);
    }

    public void zoomTo(boolean isolate, float fade_time) {
        if (isolate) {
            renderer.ZoomTo(DVLTypes.DVLZOOMTO.NODE_SETISOLATION, id, fade_time);
        } else {
            renderer.ZoomTo(DVLTypes.DVLZOOMTO.NODE, id, fade_time);
        }
    }



    public SDVLMatrix getMatrix() {
        SDVLMatrix mat = new SDVLMatrix();
        dvl.getScene().GetNodeWorldMatrix(id, mat);
        return mat;
    }

    public void setMatrix(SDVLMatrix matrix) {
        dvl.getScene().SetNodeWorldMatrix(id, matrix);
    }

    public long getId() {
        return id;
    }

    public void select() {
        dvl.setNodeFlags(id, DVLTypes.DVLNODEFLAG.SELECTED, true);
    }

    public void unselect() {
        dvl.setNodeFlags(id, DVLTypes.DVLNODEFLAG.SELECTED, false);
    }

    public void open() {
        dvl.setNodeFlags(id, DVLTypes.DVLNODEFLAG.CLOSED, true);
    }

    public void close() {
        dvl.setNodeFlags(id, DVLTypes.DVLNODEFLAG.CLOSED, false);
    }

    public void enableSelection() {
        dvl.setNodeFlags(id, DVLTypes.DVLNODEFLAG.UNHITABLE, false);
    }

    public void disableSelection() {
        dvl.setNodeFlags(id, DVLTypes.DVLNODEFLAG.UNHITABLE, true);
    }

    public void show() {
        dvl.setNodeFlags(id, DVLTypes.DVLNODEFLAG.VISIBLE, true);
    }

    public void hide() {
        dvl.setNodeFlags(id, DVLTypes.DVLNODEFLAG.VISIBLE, false);
    }

    public void setHighLight(int color) {
        dvl.getScene().SetNodeHighlightColor(id, color);
    }

    public void clearHighLight() {
        dvl.getScene().SetNodeHighlightColor(id, 0);
    }


}
