package com.domee.model;

import java.io.Serializable;

public class Visible implements Serializable {

	private static final long serialVersionUID = 1L;

	private int type;
	private int list_id;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getList_id() {
		return list_id;
	}

	public void setList_id(int list_id) {
		this.list_id = list_id;
	}

}
