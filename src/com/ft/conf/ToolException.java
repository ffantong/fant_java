package com.ft.conf;

public class ToolException extends Exception{
	private static final long serialVersionUID = 1L;

	public ToolException(Exception e,ExpTyp typ){
		super(e);
		this.e_typ = typ;
	}

	@Override
	public void printStackTrace() {
		System.err.println(e_typ);
		super.printStackTrace();
	}
	private ExpTyp e_typ;
}
