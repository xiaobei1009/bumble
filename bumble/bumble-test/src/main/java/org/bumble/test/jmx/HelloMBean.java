package org.bumble.test.jmx;

/**
 * @author : shenxiangyu
 * @date :
 */
public interface HelloMBean {
    void say();
    void setVal(String val);
    String getVal();
}
