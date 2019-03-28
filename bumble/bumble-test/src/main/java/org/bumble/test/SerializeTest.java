package org.bumble.test;

import com.alibaba.fastjson.JSONObject;
import org.bumble.test.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shenxiangyu
 * @date 2019-03-13
 */
public class SerializeTest {
    public static void main(String[] args) {
        Woof woof = new Woof();
        woof.setName("1");
        Dog dog = new Dog();
        dog.setName("2");
        List<Object> animals = new ArrayList<Object>();
        animals.add(woof);
        animals.add(dog);

        Zoo zoo = new Zoo();
        zoo.setAnimalList(animals);

        String str = JSONObject.toJSONString(zoo);

        Zoo zoo2 = (Zoo) JSONObject.parseObject(str, Zoo.class);
        for (Object object : zoo2.getAnimalList()) {
            if (object instanceof Woof) {
                System.out.println("woof");
            }
            if (object instanceof Dog) {
                System.out.println("dog");
            }
        }
    }
}
