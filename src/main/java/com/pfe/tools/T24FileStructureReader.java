package com.pfe.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class T24FileStructureReader {
	
	public Properties getFileProperties(String propertiesFile) {
		Properties props = new Properties();

		// try retrieve data from file
		try {
			InputStream input = getClass().getClassLoader()
					.getResourceAsStream(propertiesFile);
			props.load(input);
			//logger.info("Fichier " + propertiesFile + " Charge");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}

}
