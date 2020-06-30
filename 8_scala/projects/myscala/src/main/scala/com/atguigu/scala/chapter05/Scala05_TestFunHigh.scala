package com.atguigu.scala.chapter05

/**
  * Author: Felix
  * Date: 2020/6/30
  * Desc:
  *   -高阶函数：函数的高级用法
  *     *在Scala语言中，函数作为一等公民
  *       >函数可以作为值进行传递
  *         是作为参数和作为返回值的基础
  *
  *       >函数可以作为参数进行传递
  *         *一般通过匿名函数实现(lambda)
  *         *好处
  *           >解耦  降低函数之间的耦合度
  *           >灵活扩展函数的功能
  *
  *       >函数可以作为返回值进行返回
  *         *应用在函数嵌套的场景，如果存在函数嵌套，对于函数式编程语言，提供闭包，避免函数大量堆积在栈内存
  *         *同时有了闭包，会延长外层函数变量的生命周期，让内层函数进行访问。
  *
  *       >函数柯里化
  *
  */
object Scala05_TestFunHigh {
  def main(args: Array[String]): Unit = {

    /*//函数作为值进行传递
    //定义一个函数
    def foo():Int = {
      println("foo...")
      1
    }
    //（1）调用foo函数，把返回值给变量f
    //var f = foo
    //var f = foo()
    //println(f)

    //（2）在被调用函数foo后面加上 _，相当于把函数foo当成一个整体，传递给变量f1
    var f1 = foo _
    //println(f1())

    //（3）如果明确变量类型，那么不使用下划线也可以将函数作为整体传递给变量
    //var f2: ()=>Int = foo _
    var f3: ()=>Int = foo
    //println(f3())
*/

    //函数作为参数传递
    def f2(): Unit ={
      println("aaaa")
    }
    def f1(f:()=>Unit): Unit ={
      f()
    }
    f1(f2 _)
    f1(f2)


    /*//函数可以作为返回值进行返回
    //函数嵌套
    def f8()={
      def f9(): Unit ={
        println("XXXXXX")
      }
      //将f9作为整体返回给f8
      f9 _
    }

    //var ff = f8()
    //ff()
    f8()()*/
  }
}
