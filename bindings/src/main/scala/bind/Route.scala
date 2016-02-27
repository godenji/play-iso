package playiso
package bind

import play.api.mvc.{PathBindable, QueryStringBindable}

object Route {
  import macros._

  implicit final def pathBindValueClass[T <: MappedToBase]
      (implicit pathBind: PathBindable[T#Underlying], iso: Isomorphism[T]): PathBindable[T] = new PathBindable[T] {

    override def bind(key: String, value: String): Either[String, T] = {
      pathBind.bind(key,value).fold(Left(_), i=> Right(iso.comap(i)))
    }

    override def unbind(key: String, id: T): String = {
      pathBind.unbind(key, iso.map(id))
    }
  }

  implicit final def queryStringBindValueClass[T <: MappedToBase]
      (implicit qsBind: QueryStringBindable[T#Underlying], iso: Isomorphism[T]): QueryStringBindable[T] = new QueryStringBindable[T] {

    override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, T]] = {
      qsBind.bind(key, params).map(_.fold(Left(_), i=> Right(iso.comap(i))))
    }
    override def unbind(key: String, id: T): String = {
      qsBind.unbind(key, iso.map(id))
    }
  }
}
