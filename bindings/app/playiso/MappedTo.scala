package playiso
/**
 * trait that value classes must extend in order to
 * provide concrete primitive type to macro materializer
 */
trait MappedTo[T] extends Any with macros.MappedToBase {
  type Type = T
  def value: T
  override def toString() = s"$value"
}