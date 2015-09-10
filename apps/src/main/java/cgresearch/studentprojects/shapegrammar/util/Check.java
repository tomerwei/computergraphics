package cgresearch.studentprojects.shapegrammar.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Check {
	private static final List<String> functionNameList = 
		    Collections.unmodifiableList(Arrays.asList("front", "left","top","right","back","bot"));
	
	public static boolean checkIsFunctionName(String name) {
		for (String listElement : functionNameList) {
			if(listElement == name.trim()){
				return true;
			}
		}
		return false;
	}
}
