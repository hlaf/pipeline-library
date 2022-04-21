package com.emt.util;

import java.util.Map;

public class TestUtils {
	
	public static String getFileContent(Map state) {
		String version_info = "";
		if ((boolean)state.get("file_contains_version_info")) {
    	    version_info = String.format("%s = '%s'%n",
    	                    		     state.get("file_version_key"),
    	        		                 state.get("file_version_value"));
		}
		String file_content = String.format(
    			"%nsome random text%n" +
    	        version_info +
    	        "%nmore random text%n"
    	);
		return file_content;
	}
	
	// Logic
    private static boolean implies(boolean a, boolean b) {
        return (!a) || b;
    }

}
