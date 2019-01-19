package com.mssinfotech.iampro.co.data;

public class CategoryItem {
    private Integer id;
    private String name;

    public CategoryItem(Integer id, String name) {
        this.id = id;
        this.name = name;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    //to display object as a string in spinner
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CategoryItem){
            CategoryItem c = (CategoryItem )obj;
            if(c.getName().equals(name) && c.getId()==id ) return true;
        }

        return false;
    }
}
