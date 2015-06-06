package object playiso {
  /**
   * trait that your value classes must extend
   * (provides concrete primitive type for macro materializer)
   * 
   * {{{case class FooId(id: Long) extends AnyVal with MappedTo[Long]}}}
   */
  trait MappedTo[T] extends Any with MappedToBase {
    type Underlying = T
    def value: T
    override def toString() = s"$value"
  }
  
  /**
   * base type used to materialize value class isomorphism
   */
  trait MappedToBase extends Any {
    type Underlying
    def value: Underlying
  }
}