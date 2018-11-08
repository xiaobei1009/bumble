package org.bumble.test.jmx;

/**
 * @author : shenxiangyu
 * @date :
 */
public class Hello implements HelloMBean {
    @Override
    public void say() {
        System.out.println("Hello World Jmx say: " + val);
    }

    private String val = "abc";

    @Override
    public void setVal(String val) {
        this.val = val;
    }

    @Override
    public String getVal() {
        return val;
    }
}
