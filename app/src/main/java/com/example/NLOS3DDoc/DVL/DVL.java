package com.example.NLOS3DDoc.DVL;

import android.app.Activity;
import android.opengl.Matrix;
import android.util.Log;

import com.example.NLOS3DDoc.Documentation.Procedure.DVLCommand;
import com.sap.ve.DVLCore;
import com.sap.ve.DVLRenderer;
import com.sap.ve.DVLScene;
import com.sap.ve.DVLTypes;
import com.sap.ve.SDVLMatrix;
import com.sap.ve.SDVLNodeIDsArrayInfo;
import com.sap.ve.SDVLNodeInfo;
import com.sap.ve.SDVLPartsListInfo;
import com.sap.ve.SDVLProcedure;
import com.sap.ve.SDVLProceduresInfo;
import com.sap.ve.SDVLStep;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by ADSL on 25/05/2016.
 */
public class DVL {
    private static DVL ourInstance = new DVL();
    private Activity activity;
    private DVLScene scene;
    private DVLCore core;
    private DVLRenderer renderer;
    private SDVLNodeIDsArrayInfo selected_nodes;
    private float fade_time;
    private boolean init_ok = false;
    private SDVLPartsListInfo m_partsListInfo;
    private ArrayList<SDVLProcedure> portfolios;
    private ArrayList<SDVLProcedure> procedures;
    private DVLSceneEventHandler dvl_scene_event_handler;
    private Stack<DVLCommand> dvlCommands;

    private DVL() {

    }

    public static DVL getInstance() {
        return ourInstance;
    }

    public static void triggerSceneStepEvent(int type, long stepId) {

    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<SDVLProcedure> getPortfolios() {
        return portfolios;
    }

    public ArrayList<SDVLProcedure> getProcedures() {
        return procedures;
    }



    public void init(DVLCore core) {
        selected_nodes = new SDVLNodeIDsArrayInfo();
        if (core != null) {
            this.core = core;

            if (core.GetRenderer() != null) {
                this.renderer = core.GetRenderer();
                if (renderer.GetAttachedScene() != null) {
                    this.scene = renderer.GetAttachedScene();
                    init_ok = true;
                    SDVLProceduresInfo m_proceduresInfo = new SDVLProceduresInfo();
                    scene.RetrieveProcedures(m_proceduresInfo);
                    portfolios = m_proceduresInfo.portfolios;
                    procedures = m_proceduresInfo.procedures;
                    m_partsListInfo = new SDVLPartsListInfo();
                    scene.BuildPartsList(DVLTypes.DVLPARTSLIST.RECOMMENDED_uMaxParts,
                            DVLTypes.DVLPARTSLIST.RECOMMENDED_uMaxNodesInSinglePart,
                            DVLTypes.DVLPARTSLIST.RECOMMENDED_uMaxPartNameLength,
                            DVLTypes.DVLPARTSLISTTYPE.ALL, DVLTypes.DVLPARTSLISTSORT.NAME_ASCENDING,
                            DVLTypes.DVLID_INVALID, "", m_partsListInfo);

                } else {
                    Log.e("DVL.init", "AttachedScene = null !!");
                }
            } else {
                Log.e("DVL.init", "Renderer = null !!");
            }

        } else {
            Log.e("DVL.init", "Core = null !!");
        }

        fade_time = 2f;
    }

    public void selectNodes(String node_name) {
        if (init_ok) {
            scene.FindNodes(DVLTypes.DVLFINDNODETYPE.NODE_NAME, DVLTypes.DVLFINDNODEMODE.EQUAL_CASE_INSENSITIVE, node_name, selected_nodes);
        }
    }

    public void move(float delta_x, float delta_y, float delta_z) {
        if (init_ok) {
            SDVLMatrix mat = new SDVLMatrix();
            scene.GetNodeWorldMatrix(selected_nodes.nodes.get(0), mat);
            Matrix.translateM(mat.m, 0, delta_x, delta_y, delta_z);
            scene.SetNodeWorldMatrix(selected_nodes.nodes.get(0), mat);
        }
    }

    public void rotate(float angle, float delta_x, float delta_y, float delta_z) {
        if (init_ok) {
            SDVLMatrix mat = new SDVLMatrix();
            scene.GetNodeWorldMatrix(selected_nodes.nodes.get(0), mat);

            Matrix.rotateM(mat.m, 0, angle, delta_x, delta_y, delta_z);
            scene.SetNodeWorldMatrix(selected_nodes.nodes.get(0), mat);
        }

    }

    public void zoom(boolean isolate) {
        if (init_ok) {
            if (isolate) {
                renderer.ZoomTo(DVLTypes.DVLZOOMTO.NODE_SETISOLATION, selected_nodes.nodes.get(0), fade_time);
            } else {
                renderer.ZoomTo(DVLTypes.DVLZOOMTO.NODE, selected_nodes.nodes.get(0), fade_time);
            }
        }
    }

    public void clearSelection() {

        selected_nodes.nodes.clear();
    }

    public void setView(String view_name) {
        if (init_ok) {
            SDVLProceduresInfo m_proceduresInfo = new SDVLProceduresInfo();
            scene.RetrieveProcedures(m_proceduresInfo);
            List<SDVLStep> standard_views = m_proceduresInfo.portfolios.get(0).steps;

            for (SDVLStep st : standard_views) {
                if (st.name.equals(view_name)) {
                    scene.ActivateStep(st.id, true, false);
                }
            }
        }
    }

    public void resetView() {
        if (init_ok) {
            clearSelection();
            renderer.SetIsolatedNode(-1);
            renderer.ResetView();
        }
    }

    public void triggerSelectionChanged(long hScene, int numberOfSelectedNodes, final long idFirstSelectedNode) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final List<SDVLNodeInfo> nodes = new ArrayList<>();
                //todo: change implementation to all selected nodes, not only first
                nodes.add(getNodeInfo(idFirstSelectedNode));
                if (dvl_scene_event_handler != null) {

                    try {
                        dvl_scene_event_handler.onSelectionChanged(core, renderer, scene, nodes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    public SDVLNodeInfo getNodeInfo(long node_id) {

        return getNodeInfo(node_id, DVLTypes.DVLNODEINFO.NAME | DVLTypes.DVLNODEINFO.CHILDREN |
                DVLTypes.DVLNODEINFO.UNIQUEID | DVLTypes.DVLNODEINFO.OPACITY |
                DVLTypes.DVLNODEINFO.ASSETID | DVLTypes.DVLNODEINFO.HIGHLIGHT_COLOR | DVLTypes.DVLNODEINFO.URI |
                DVLTypes.DVLNODEINFO.FLAGS | DVLTypes.DVLNODEINFO.PARENTS);
    }

    public SDVLNodeInfo getNodeInfo(long node_id, int DVLNODEINFOFlag) {
        SDVLNodeInfo cur_node = new SDVLNodeInfo();
        scene.RetrieveNodeInfo(node_id, DVLNODEINFOFlag, cur_node);
        return cur_node;
    }

    public List<String> getNodeNames(ArrayList<Long> node_ids) {
        List<String> node_names = new ArrayList<>();
        for (long i : node_ids) {
            node_names.add(getNodeInfo(i, DVLTypes.DVLNODEINFO.NAME).nodeName);
        }
        return node_names;
    }

    public void setOnSelectEvent(DVLSceneEventHandler handler) {
        dvl_scene_event_handler = handler;
        //todo: change implementation to support multiple handlers. Including remove handler.
    }

    public void addCommand(DVLCommand cmd) {
        dvlCommands.push(cmd);
    }

}
