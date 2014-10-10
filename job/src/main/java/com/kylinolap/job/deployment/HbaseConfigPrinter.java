package com.kylinolap.job.deployment;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by honma on 9/30/14.
 * <p/>
 * This class is assumed to be run by
 * "hbase org.apache.hadoop.util.RunJar kylin-job-0.5.7-SNAPSHOT-job.jar com.kylinolap.job.deployment.HadoopConfigPrinter "
 * in the shell, so that hbase and hadoop related environment variables will be
 * visible to this class.
 */
public class HbaseConfigPrinter {
	public static void main(String[] args) {
		printConfigs();
	}

	private static void printConfigs() {
		System.out.println("export KYLIN_LD_LIBRARY_PATH="
				+ ConfigLoader.LD_LIBRARY_PATH_LOADER.loadValue());
		System.out.println("export KYLIN_HBASE_CLASSPATH="
				+ ConfigLoader.HBASE_CLASSPATH_LOADER.loadValue());
		System.out.println("export KYLIN_HBASE_CONF_PATH="
				+ ConfigLoader.HBASE_CONF_FOLDER_LOADER.loadValue());
	}

	@SuppressWarnings("unused")
	private static void printAllEnv() {
		for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
			System.out.println("Key: " + entry.getKey());
			System.out.println("Value: " + entry.getValue());
			System.out.println();
		}
	}

	enum ConfigLoader {

		LD_LIBRARY_PATH_LOADER {
			@Override
			public String loadValue() {
				return System.getenv("LD_LIBRARY_PATH");
			}
		},

		HBASE_CLASSPATH_LOADER {
			@Override
			public String loadValue() {
				return System.getenv("CLASSPATH");
			}
		},

		HBASE_CONF_FOLDER_LOADER {
			@Override
			public String loadValue() {
				String output = HBASE_CLASSPATH_LOADER.loadValue();
				String[] paths = output.split(":");
				StringBuilder sb = new StringBuilder();

				for (String path : paths) {
					path = path.trim();
					File f = new File(path);
					if (StringUtils.containsIgnoreCase(path, "conf")
							&& f.exists() && f.isDirectory()
							&& f.getName().equalsIgnoreCase("conf")) {
						sb.append(":" + path);
					}
				}
				return sb.toString();
			}
		};

		public abstract String loadValue();
	}

}