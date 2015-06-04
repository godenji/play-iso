package playiso

import play.api.mvc.{PathBindable, QueryStringBindable}

object RouteBindable {
  import macros._
  /**
   * materialize a value class path bindable
   */
  implicit final def pathBindValueClass[T <: MappedToBase]
    (implicit pathBind: PathBindable[T#Type], iso: Isomorphism[T]): 
    PathBindable[T] = new PathBindable[T]{
      override def bind(key: String, value: String): 
        String Either T = 
          pathBind.bind(key,value).fold(
            Left(_), i=> Right( iso.comap(i) )
          )
        
      override def unbind(key: String, id: T): String = {
        pathBind.unbind(key, iso.map(id) )
      }
    }
  
  /**
   * materialize a value class querystring bindable
   */
  implicit final def qsBindValueClass[T <: MappedToBase]
    (implicit qsBind: QueryStringBindable[T#Type], iso: Isomorphism[T]): 
    QueryStringBindable[T] = new QueryStringBindable[T]{
      override def bind(key: String, params: Map[String, Seq[String]]): 
        Option[String Either T] = 
          qsBind.bind(key, params).map(_.fold(
            Left(_), i=> Right( iso.comap(i) )
          ))
        
      override def unbind(key: String, id: T): String = {
        qsBind.unbind(key, iso.map(id) )
      }
    }
}