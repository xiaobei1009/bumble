package org.bumble.test;

import org.bumble.base.test.BumbleTest;

import java.util.concurrent.*;

/**
 * @author shenxiangyu
 * @date 2019-03-06
 */
public class FutureTaskTest extends BumbleTest {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        FutureTask<String> future = new FutureTask<String>(new Callable<String> () {
            //使用Callable接口作为构造参数   
            public String call() {
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {

                }
                //真正的任务在这里执行，这里的返回值类型为String，可以为任意类型
                return "hello";
            }
        });

        executor.execute(future);
        //在这里可以做别的任何事情   
        try {
            String result = future.get(5000, TimeUnit.MILLISECONDS); //取得结果，同时设置超时执行时间为5秒。同样可以用future.get()，不设置执行超时时间取得结果   
            logger.info(result);
        } catch (Exception e) {
            future.cancel(true);
        } finally {
            executor.shutdown();
        }
    }

    @Override
    public void template(String[] args) throws Exception {

    }
}
