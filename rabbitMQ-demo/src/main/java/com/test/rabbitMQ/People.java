package com.test.rabbitMQ;

import java.io.Serializable;
import java.util.List;

/**
 * @author csf/chenshifeng@ksjgs.com
 * @date 2016/8/3.
 */
public class People implements Serializable {
    private int id;
    private String name;
    private boolean male;
    private People spouse;
    private List<People> friends;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public People getSpouse() {
        return spouse;
    }

    public void setSpouse(People spouse) {
        this.spouse = spouse;
    }

    public List<People> getFriends() {
        return friends;
    }

    public void setFriends(List<People> friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        return "People[id=" + id + ",name=" + name + ",male=" + male + "]";
    }

}