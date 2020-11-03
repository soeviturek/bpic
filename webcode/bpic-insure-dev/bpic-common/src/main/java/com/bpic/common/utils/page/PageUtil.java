package com.bpic.common.utils.page;

public class PageUtil {

	
	public static int getPages(int size,int total){
		 int pages = 0;
			if (size == 0) {
	            return 0;
	        }
	        pages =total / size;
	        if (total % size != 0) {
	            pages++;
	        }
	        return pages;
	}
}
