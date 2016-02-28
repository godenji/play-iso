package playiso
package bind

import play.api.libs.json._

object Json {
  import macros._

  implicit final def readsValueClass[T <: MappedToBase]
      (implicit readsBind: Reads[T#Underlying], iso: Isomorphism[T]): Reads[T] = {
    readsBind.map(iso.comap)
  }

  implicit final def writesValueClass[T <: MappedToBase]
      (implicit writesBind: Writes[T#Underlying], iso: Isomorphism[T]): Writes[T] = Writes { valueClass =>
    writesBind.writes(iso.map(valueClass))
  }
}
