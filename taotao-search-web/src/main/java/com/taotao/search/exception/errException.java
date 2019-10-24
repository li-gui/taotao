package com.taotao.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/*
 * 全局异常处理
 */
public class errException implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,
			Exception arg3) {
		 System.out.println(arg3.getMessage());
		 arg3.printStackTrace();
		 
		 ModelAndView model=new ModelAndView();
		 model.setViewName("error/exception");
		 model.addObject("message", "你的网络有异常！");
		return model;
	}

}
