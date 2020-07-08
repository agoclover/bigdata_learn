package com.atguigu.spark.day03

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Author: Felix
  * Date: 2020/7/7
  * Desc: 转换算子-flatMap  对RDD中的元素进行扁平化处理，要求RDD中的元素是可迭代
  */
object Spark01_Transformation_flatMap {
  def main(args: Array[String]): Unit = {
    //创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")

    //创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)

    //通过集合创建RDD
    val rdd: RDD[List[Int]] = sc.makeRDD(List(List(1,2,3),List(4,5),List(6,7),List(8)))
    val newRDD: RDD[Int] = rdd.flatMap(list=>list)
    newRDD.collect().foreach(println)

    // 关闭连接
    sc.stop()
  }
}
