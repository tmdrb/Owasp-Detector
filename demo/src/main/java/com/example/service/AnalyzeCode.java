package com.example.service;

import java.util.ArrayList;

public abstract class AnalyzeCode {

	public abstract ArrayList<String> Packagesfind() throws Exception;
	public abstract void Classesfind() throws Exception;
	public abstract void varsfind() throws Exception;
	
	
}
