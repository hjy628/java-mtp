/**
 * Created by hjy on 18-1-4.
 */
package com.hjy.mtpattern.chap14;

/**
 * 使用AsyncTask类来实现Half-sync/Half-async模式，应用代码只需要创建一个AsyncTask类的子类(或者匿名子类),并在子类中补充以下几个任务
 * 1[必需] 定义一个含义具体的服务方法名，该方法可直接调用父类的dispatch方法
 * 2[必需] 实现父类的抽象方法doInBackground方法
 * 3[可选] 根据应用的实际需要覆盖父类的onPreExecute方法、onPostExecute方法和onExecutionError方法
 *
 *
 */