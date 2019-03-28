package org.bumble.test.model;

import java.util.List;

/**
 * @author shenxiangyu
 * @date 2019-03-13
 */
public class Zoo <T extends Animal> {
    public List<T> getAnimalList() {
        return animalList;
    }

    public void setAnimalList(List<T> animalList) {
        this.animalList = animalList;
    }

    private List<T> animalList;
}
