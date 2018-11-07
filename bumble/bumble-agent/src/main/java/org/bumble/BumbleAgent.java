package org.bumble;

import java.lang.instrument.Instrumentation;

/**
 * BumbleAgent
 */
public class BumbleAgent
{
    public static void main( String[] args )
    {
        System.out.println( "Hello Bumble Agent!" );
    }

    public static void premain(String agentOps, Instrumentation inst) {
        System.out.println("=========premain方法执行========");
        System.out.println(agentOps);

        BumbleTransformer bt = new BumbleTransformer();

        // TODO scan class path to find all available methods
        bt.add("org.bumble.test.model.SimpleCat.say");

        // 添加Transformer
        inst.addTransformer(bt);
    }
}
