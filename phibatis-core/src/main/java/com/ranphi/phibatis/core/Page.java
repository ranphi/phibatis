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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Ranphi
 */
public class Page extends JpaCriteria implements Serializable{

	private static final long serialVersionUID = 1L;
	public static final String PAGE_SIZE = "pageSize";
	public static final String PAGE_NUM = "pageNum";
	public static final String LIST = "list";
	public static final String TOTAL = "total";

	protected int pageSize;
	protected int pageNum;
	protected long total;
	protected boolean isCount = true;
	protected int totalPage = -1;
	protected List<?> list;
	
	
	public Page() {
		pageSize = 20;
		pageNum = 1;
		this.put(PAGE_SIZE, pageSize);
		this.put(PAGE_NUM, pageNum);
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public long getTotal() {
		return total;
	}

	public List<?> getList() {
		if (this.list == null) {
			this.list = new LinkedList<Object>();
		}
		return this.list;
	}

	public int getPageNum() {
		if (0 == pageNum) {
			return 1;
		} else {
			return this.pageNum;
		}
	}

	public int getStartRow() {
		return (this.getPageNum() - 1) * this.getPageSize();
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public boolean isCount() {
		return isCount;
	}

	public void setCount(boolean isCount) {
		this.isCount = isCount;
	}
	
	
}
