package com.atguigu.myspark.day03

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * @author Zhang Chao
 * @version java_day
 * @date 2020/7/8 4:51 下午
 */
object S12_Transformation_foldByKey {
  def main(args: Array[String]): Unit = {
    // create SparkConf and set App's name
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")

    // create SparkContext which is the submission entrance of Spark App
    val sc: SparkContext = new SparkContext(conf)

    val RDD: RDD[(String, Int)] = sc.makeRDD(List(("a", 5), ("a",2), ("b", 10), ("c", 9), ("b", 3), ("c", 2), ("a", 1), ("b", 2)), 2)

    RDD.foldByKey(0)(_+_)
        .collect()
        .foreach(println)
    
    // 关闭连接
    sc.stop()
  }
}
