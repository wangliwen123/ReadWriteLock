package com.example.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class MyCache{
  private volatile Map<String,Object> map = new HashMap<>();
  private ReadWriteLock rwLock = new ReentrantReadWriteLock();

  public void put(String key,Object value){
      rwLock.writeLock().lock();
      try {
          System.out.println(Thread.currentThread().getName()+"\t 正在写入数据");
          map.put(key, value);
          System.out.println(Thread.currentThread().getName()+"\t 写完成");
          System.out.println();
      } catch (Exception e) {
          e.printStackTrace();
      } finally {
          rwLock.writeLock().unlock();
      }
  }

  public Object get(String key){
      rwLock.readLock().lock();
      Object result = null;
      try {
          System.out.println(Thread.currentThread().getName()+"\t 正在读取数据");
         result =  map.get(key);
          System.out.println(Thread.currentThread().getName()+"\t 读取完成"+result);

      } catch (Exception e) {
          e.printStackTrace();
      } finally {
          rwLock.readLock().unlock();
      }
      return result;
  }

}

public class ReadWriteLockDemo {
    public static void main(String[] args) {
        MyCache myCache = new MyCache();

        for (int i = 1; i <=10; i++) {
            final int temInt = i;
            new Thread(()->{
                myCache.put(Thread.currentThread().getName()+"",temInt+"");
            },String.valueOf(i)).start();
        }

        for (int i = 1; i <=10; i++) {
            new Thread(()->{
                myCache.get(Thread.currentThread().getName()+"");
            },String.valueOf(i)).start();
        }
    }
}
