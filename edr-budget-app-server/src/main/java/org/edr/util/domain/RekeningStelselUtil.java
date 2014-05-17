package org.edr.util.domain;

public class RekeningStelselUtil {

	public static boolean isChild(String childRekeningnr, String parentRekeningnr) {
		String patternOfChild = null;
		if (parentRekeningnr.substring(0, 1).equals("0")) {
			patternOfChild = ".00000";
		} else if (parentRekeningnr.substring(1, 2).equals("0")) {
			patternOfChild = parentRekeningnr.substring(0, 1) + ".0000";
		} else if (parentRekeningnr.substring(2, 4).equals("00")) {
			patternOfChild = parentRekeningnr.substring(0, 2) + "..00";
		} else if (parentRekeningnr.substring(4, 6).equals("00")) {
			patternOfChild = parentRekeningnr.substring(0, 4) + "..";
		} else {
			return false;
		}

		return childRekeningnr.matches(patternOfChild);
	}

}
