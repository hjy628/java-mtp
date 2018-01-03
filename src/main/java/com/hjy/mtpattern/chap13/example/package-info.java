/**
 * Created by hjy on 18-1-3.
 */
package com.hjy.mtpattern.chap13.example;

/**
*  数据同步定时任务DataSyncTask创建了一个Pipeline实例，该Pipeline实例包含了三个Pipe实例，
 *  它们分别对应buildPipeline方法中的3个局部变量:stageSaveFile、stageTransferFile和stageBackupFile
 *
 *
 *  stageSaveFile   一组数据库记录(类型为RecordSaveTask)
 *  将输入的一组数据库记录存入数据文件　　　
 *  若当前数据文件已写满，则输出该写满的文件(类型为File);否则，输出null
 *  单工作者线程,若使用多线程模型，必然导致多个线程征用同一个数据文件(文件未写满),而这又需要引入锁，使用单线程模型则可以避免锁的使用
 *
 *
 *  stageTransferFile 已写满的数据文件(类型File)
 *  将输入的数据文件以并行方式FTP传输至指定的多台FTP主机
 *  若文件传输成功，则输出已传输过的数据文件（类型为File）;否则输出null
 *  单工作者线程，该Pipe处理过程会用到一款非线程安全的开源FTP客户端组件，使用单线程模型可以在不引入锁的情况下确保使用该组件时的线程安全
 *
 *  stageBackupFile
 *  已传输成功的文件(类型File)
 *  将输入的文件移动到备份目录
 *  null
 *  单工作者线程
*
* */