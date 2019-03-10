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
package com.ranphi.phibatis.core;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import com.ranphi.phibatis.core.annotation.Table;
import com.ranphi.phibatis.core.sql.SqlProcessor;
import com.ranphi.phibatis.core.sql.MySQLDialect;
import com.ranphi.phibatis.core.sql.SqlDialect;
import com.ranphi.phibatis.core.sql.SqlParser;
import com.ranphi.phibatis.core.util.ClassHandler;
import com.ranphi.phibatis.core.util.ClassScanner;

/**
 * 
 * @author Ranphi
 */
public class PhibatisDriver implements InitializingBean {

	private List<String> scanPackages;
	private SqlDialect dialect;

	public PhibatisDriver() {
	}

	public void afterPropertiesSet() throws Exception {
		if (scanPackages != null) {
			new ClassScanner(new ClassHandler() {
				public void handle(Class<?> clazz) {
					if (clazz.isAnnotationPresent(Table.class)) {
						SqlParser sqlParser = SqlParser.getInstance();
						sqlParser.setSqlProcessor(new SqlProcessor(getDialect()));
						sqlParser.parse(clazz);
					}
				}
			}, new FileFilter() {
				public boolean accept(File path) {
					if (path.isDirectory())
						return true;
					return path.isFile() && path.getName().endsWith(".class");
				}
			}).scanClass(true, scanPackages);
		}
	}

	public void setScanPackages(List<String> scanPackages) {
		this.scanPackages = scanPackages;
	}

	public SqlDialect getDialect() {
		if(dialect == null){
			dialect = MySQLDialect.of();
		}
		return dialect;
	}

	public void setDialect(SqlDialect dialect) {
		this.dialect = dialect;
	}

}
