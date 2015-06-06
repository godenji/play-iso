/*
 * this is a modified version of Slick's macro implementation:
 * @see https://github.com/slick/slick/blob/648184c7cb710563d07b859891ed7fe46d06849d/slick/src/main/scala/slick/lifted/MappedTo.scala 
 */
package playiso
package macros

import scala.language.experimental.macros
import scala.reflect.macros.Context
import scala.util.control.NonFatal

/**
 * convert to/from a value class and its underlying
 * primitive type
 */
class Isomorphism[T <: MappedToBase](
  val map: T => T#Underlying, val comap: T#Underlying => T
)

/**
 * macro that materializes the isomorphism
 */
object MappedToBase {
  implicit def isomorphicToIsomorphism[T <: MappedToBase]: Isomorphism[T] =
    macro isomorphicToIsomorphismMacro[T]

  def isomorphicToIsomorphismMacro[T <: MappedToBase](c: Context)
    (implicit e: c.WeakTypeTag[T]): c.Expr[Isomorphism[T]] = {
    
    import c.universe._
    if(!(e.tpe <:< c.typeOf[MappedToBase])) c.abort(
      c.enclosingPosition, 
      "Work-around for illegal macro-invocation; see SI-8351"
    )
    implicit val eutag = c.TypeTag[T#Underlying](
      e.tpe.member( newTypeName("Underlying") ).typeSignatureIn(e.tpe)
    )
    val cons = c.Expr[T#Underlying => T](Function(
      List(ValDef(
        Modifiers(Flag.PARAM), newTermName("v"), TypeTree(), EmptyTree)
      ),
      Apply(
        Select(New(TypeTree(e.tpe)), nme.CONSTRUCTOR),
        List(Ident(newTermName("v")))
      )
    ))
    val res = reify{
      new Isomorphism[T](_.value, cons.splice)
    }
    try c.typeCheck(res.tree) catch { case NonFatal(ex) =>
      val p = c.enclosingPosition
      val msg = s"Error typechecking MappedToBase expansion: ${ex.getMessage}"
      println(s"${p.source.path} : ${p.line} $msg")
      c.error(c.enclosingPosition, msg)
    }
    res
  }
}