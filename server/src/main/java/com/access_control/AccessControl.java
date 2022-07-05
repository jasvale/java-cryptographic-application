package com.access_control;

import com.commons.datastructures.ClientData;

public class AccessControl {
	public static boolean isUserValid(ClientData clientData) {
		if(clientData.getIdentifier().equals("john")) {
			return true;
		}
		else if(clientData.getIdentifier().equals("pete")) {
			return true;
		}
		return false;
	}
	
}
