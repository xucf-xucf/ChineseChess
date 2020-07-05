/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
package com;

import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class test  {
	
	public static void main(String[] args) {
		String pyPath = "F:/思特奇/新建文件夹/HelloP yWorld/src/printPhoneTesd.py";
		PythonInterpreter interpreter = new PythonInterpreter();
		interpreter.execfile(pyPath);
		PyFunction func = (PyFunction)interpreter.get("test",PyFunction.class);
		PyObject pyobj = func.__call__();
		System.out.println(pyobj);
	}
}
