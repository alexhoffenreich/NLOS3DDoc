package com.sap.ve;

import java.util.ArrayList;
import com.sap.ve.DVLTypes.DVLNODEFLAG;

public class SDVLNodeInfo extends ListSelectionItem
{
	public long nodeID;
	public String nodeName;
	public String assetID;
	public String uniqueID;
	public ArrayList<Long> parentNodes = new ArrayList<>();
	public ArrayList<Long> childNodes = new ArrayList<>();
	public int flags;
	public float opacity;
	public int highlightColor;
	public ArrayList<SDVLURI> uriList = new ArrayList<>();

	public boolean isVisible()
	{
		return (flags & DVLNODEFLAG.VISIBLE) != 0;
	}

	public boolean isSelected()
	{
		return (flags & DVLNODEFLAG.SELECTED) != 0;
	}


	@Override
	public String toString()
	{
	    return nodeName;
	}

	@Override
	public boolean equals(Object o)
	{
		return (o instanceof SDVLNodeInfo) && (this.nodeID == ((SDVLNodeInfo)o).nodeID);
	}
}
