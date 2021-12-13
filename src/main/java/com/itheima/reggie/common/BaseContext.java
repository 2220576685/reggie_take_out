package com.itheima.reggie.common;

/**
 * @Description:test
 * @Creatr by : jinyu
 * @Date: 2021/12/3 0003 10:53
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    /**
     * 设置值
     * @param id
     */
//    public static void setCurrentId(Long id){
//        threadLocal.set(id);
//    }
//    /**
//     * 获取值
//     * @return
//     */
//    public static Long getCurrentId(){
//        return threadLocal.get();
//    }
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
