package playiso
package macros

import scala.language.experimental.macros
import scala.reflect.macros._
import scala.util.control.NonFatal

/**
 * convert to/from a value class and its underlying primitive type
 */
class Isomorphism[T <: MappedToBase](
  val map: T => T#Underlying, val comap: T#Underlying => T
)

object MappedToBase {

  implicit def isomorphicToIsomorphism[T <: MappedToBase]: Isomorphism[T] = macro MappedToBaseMacros.isomorphicToIsomorphismImpl[T]

}

/**
  * Macro that materializes the isomorphism
  * This is a modified version of Slick's macro implementation
  * @see https://github.com/slick/slick/blob/648184c7cb710563d07b859891ed7fe46d06849d/slick/src/main/scala/slick/lifted/MappedTo.scala
  */
class MappedToBaseMacros(val c: whitebox.Context) {

  import c.universe._

  def isomorphicToIsomorphismImpl[T <: MappedToBase](implicit e: c.WeakTypeTag[T]): c.Expr[Isomorphism[T]] = {

    if (!(e.tpe <:< c.typeOf[MappedToBase])) {
      c.abort(c.enclosingPosition, "Work-around for illegal macro-invocation; see SI-8351")
    }

    implicit val eutag = c.TypeTag[T#Underlying](
      e.tpe.member(TypeName("Underlying")).typeSignatureIn(e.tpe)
    )

    val cons = c.Expr[T#Underlying => T](Function(
      List(ValDef(
        Modifiers(Flag.PARAM), TermName("v"), TypeTree(), EmptyTree)
      ),
      Apply(
        Select(New(TypeTree(e.tpe)), termNames.CONSTRUCTOR),
        List(Ident(TermName("v")))
      )
    ))

    val res = reify{
      new Isomorphism[T](_.value, cons.splice)
    }

    try c.typecheck(res.tree) catch { case NonFatal(ex) =>
      val p = c.enclosingPosition
      val msg = "Error typechecking MappedTo expansion: " + ex.getMessage
      println(p.source.path + ":" + p.line + ": " + msg)
      c.error(c.enclosingPosition, msg)
    }

    res
  }
}
