package com.domee.model;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by duyuan on 13-6-16.
 */
public class DMListsResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private LinkedList<DMLists> lists;
    private String total_number;

    public LinkedList<DMLists> getLists() {
        return lists;
    }

    public void setLists(LinkedList<DMLists> lists) {
        this.lists = lists;
    }

    public String getTotal_number() {
        return total_number;
    }

    public void setTotal_number(String total_number) {
        this.total_number = total_number;
    }
}
