package com.example.NLOS3DDoc.AnimationHandler;

import android.opengl.Matrix;
import android.util.Log;

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

/**
 * Created by ADSL on 25/05/2016.
 */
public class DVL {
    private static DVL ourInstance = new DVL();
    private DVLScene scene;
    private DVLCore core;
    private DVLRenderer renderer;
    private SDVLNodeIDsArrayInfo selected_nodes;
    private float fade_time;
    private boolean init_ok = false;
    private SDVLPartsListInfo m_partsListInfo;
    private ArrayList<SDVLProcedure> portfolios;
    private ArrayList<SDVLProcedure> procedures;
    private DVLSceneEvent dvl_scene_events;
    public ArrayList<SDVLProcedure> getPortfolios() {
        return portfolios;
    }

    public ArrayList<SDVLProcedure> getProcedures() {
        return procedures;
    }

    private DVL() {
    }

    public static DVL getInstance() {
        return ourInstance;
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


    public void triggerSelectionChanged(long hScene, int numberOfSelectedNodes, long idFirstSelectedNode) {
        List<SDVLNodeInfo> nodes = new ArrayList<>();

    }

    public static void triggerSceneStepEvent (int type, long stepId) {

    }


}
