package playiso
package bind

import play.api.data.mapping._
import play.api.libs.json.JsValue

object Validation {
  import macros._

  implicit final def rulesValueClass[T <: MappedToBase]
      (implicit rulesBind: Rule[JsValue, T#Underlying], iso: Isomorphism[T]): Rule[JsValue, T] = {
    rulesBind.fmap(iso.comap)
  }

  implicit final def writesValueClass[T <: MappedToBase]
      (implicit writesBind: Write[T#Underlying, JsValue], iso: Isomorphism[T]): Write[T, JsValue] = {
    writesBind.contramap(iso.map)
  }
}
