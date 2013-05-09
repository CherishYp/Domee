package com.domee.model;

import java.io.Serializable;
import java.util.LinkedList;

public class CommentResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private LinkedList<Comment> comments;
	private String previous_cursor;
	private String next_cursor;
	private String total_number;

	public LinkedList<Comment> getComments() {
		return comments;
	}

	public void setComments(LinkedList<Comment> comments) {
		this.comments = comments;
	}

	public String getPrevious_cursor() {
		return previous_cursor;
	}

	public void setPrevious_cursor(String previous_cursor) {
		this.previous_cursor = previous_cursor;
	}

	public String getNext_cursor() {
		return next_cursor;
	}

	public void setNext_cursor(String next_cursor) {
		this.next_cursor = next_cursor;
	}

	public String getTotal_number() {
		return total_number;
	}

	public void setTotal_number(String total_number) {
		this.total_number = total_number;
	}

}
