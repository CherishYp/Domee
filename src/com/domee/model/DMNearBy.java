package com.domee.model;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by duyuan on 13-5-22.
 */
public class DMNearBy implements Serializable {

    private static final long serialVersionUID = 1L;

    private LinkedList<DMPois> pois;
    private int total_number;

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }

    public LinkedList<DMPois> getPois() {
        return pois;
    }

    public void setPois(LinkedList<DMPois> pois) {
        this.pois = pois;
    }
}
