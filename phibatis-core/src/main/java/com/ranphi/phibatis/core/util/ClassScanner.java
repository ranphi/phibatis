/**
 *    Copyright 2019 ranphi@foxmail.com
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.ranphi.phibatis.core.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Ranphi
 */
public class ClassScanner {

	private static final Logger logger = LoggerFactory.getLogger(ClassScanner.class);
	
	private static final String FILE_PROTOCOL = "file";
	private static final String JAR_PROTOCOL = "jar";
	private static Set<String> classSet = new HashSet<String>();
	 
	private FileFilter fileFilter;
	private ClassHandler fileHandler;
	
	public ClassScanner(ClassHandler fileHandler,FileFilter fileFilter) {
		this.fileFilter = fileFilter;
		this.fileHandler = fileHandler;
	}

	public void scanClass(boolean isSubpackage, List<String> packageList) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		for (String pkg : packageList) {
			String dir = PathClassResolver.convertClassToPath(pkg);
			this.scanPackage(loader, isSubpackage, dir);
		}
	}

	private void scanPackage(ClassLoader loader, boolean isRecursive, String dir) {
		try {
			Enumeration<URL> urls = loader.getResources(dir);
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				String protocol = url.getProtocol();
				if (FILE_PROTOCOL.equals(protocol)) {
					this.scanFilePackage(loader, isRecursive, dir, url);
				} else if (JAR_PROTOCOL.equals(protocol)) {
					this.scanJarPackage(loader, dir, url);
				}
			}
		} catch (IOException e) {
			logger.error("Scan package error：" + dir, e);
		}
	}

	private void scanFilePackage(ClassLoader loader, boolean isRecursive, String dir, URL url) {
		String path = url.getPath();
		if (path == null) {
			return;
		}
		try {
			path = URLDecoder.decode(path,"utf-8");
			File dirFiles = new File(path);
			File[] files = dirFiles.listFiles(fileFilter);
			if (files == null) {
				return;
			}
			for (File file : files) {
				String fileName = file.getName();
				String d = PathClassResolver.convertClassToPath(dir);
				if (file.isDirectory() && isRecursive) {
					scanPackage(loader, isRecursive, d + "/" + fileName);
					continue;
				}
				String clazz = PathClassResolver.convertPathToClass(d + "/" + fileName);
				if(!classSet.contains(clazz)){
					logger.debug("Scan persistence class：{}", clazz);
					classSet.add(clazz);
					fileHandler.handle(loader.loadClass(clazz));
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	private void scanJarPackage(ClassLoader loader, String dir, URL url) throws IOException {
		String jarFile = url.getPath();
		if (jarFile == null) {
			return;
		}
		try {
			jarFile = URLDecoder.decode(jarFile,"utf-8");
			int pos = jarFile.indexOf('!');
			if (pos == -1){
				jarFile = jarFile.substring(5);
			}else{
				jarFile = jarFile.substring(5, pos);
			}
			JarInputStream jis = new JarInputStream(new FileInputStream(jarFile));
			JarEntry entry = null;
			while ((entry = jis.getNextJarEntry()) != null) {
				String fileName = entry.getName();
				if (fileName.startsWith(dir) && fileName.endsWith(".class")) {
					String clazz = PathClassResolver.convertPathToClass(fileName);
					if(!classSet.contains(clazz)){
						logger.debug("Scan persistence class：{}", clazz);
						classSet.add(clazz);
						fileHandler.handle(loader.loadClass(clazz));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
