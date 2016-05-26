package com.sap.ve;

import java.util.ArrayList;

public class SDVLPartsListItem extends ListSelectionItem
{
	public String partName;
	public ArrayList<Long> nodesList = new ArrayList<>();

	@Override
	public String toString()
	{
		return partName;
	}
}
