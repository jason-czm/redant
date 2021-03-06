package com.redant.common.html;

import com.xiaoleilu.hutool.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gris.wang
 * @since 2017/12/1
 **/
public class HtmlMakerFactory {

    private Map<HtmlMakerEnum,HtmlMaker> htmlMakerMap;

    private static HtmlMakerFactory factory;

    private HtmlMakerFactory(){
        htmlMakerMap = new ConcurrentHashMap<>();
    }

    /**
     * 获取工厂实例
     * @return
     */
    public static HtmlMakerFactory instance(){
        if(factory==null){
            synchronized (HtmlMakerFactory.class) {
                if (factory==null) {
                    factory = new HtmlMakerFactory();
                }
            }
        }
        return factory;
    }

    /**
     * 创建HtmlMaker实例
     * @param type
     * @param clazz
     * @return
     */
    public HtmlMaker build(HtmlMakerEnum type,Class<? extends HtmlMaker> clazz){
        if(type==null){
            return null;
        }else{
            HtmlMaker htmlMaker = htmlMakerMap.get(type);
            if(htmlMaker==null){
                htmlMaker = ClassUtil.newInstance(clazz);
                htmlMakerMap.put(type,htmlMaker);
            }
            return htmlMaker;
        }
    }

    public static void main(String[] args) {
        int loopTimes = 200;

        class Runner implements Runnable{

            private Logger logger = LoggerFactory.getLogger(Runner.class);

            @Override
            public void run() {
                HtmlMakerFactory factory = HtmlMakerFactory.instance();
                logger.info("factory={},currentThread={}",(factory!=null?factory.getClass().getName():"null"),Thread.currentThread().getName());
            }
        }

        for(int i=0;i<loopTimes;i++){
            new Thread(new Runner()).start();
        }
    }

}
