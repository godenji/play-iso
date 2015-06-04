play-iso
===================

Description
-----------

Eliminates binding boilerplate for your custom types in routes and forms 
(i.e. no need to define `QueryStringBindable`, `PathBindable`, and `Formatter` bindings). 
If your custom type is a value class (or has a single value constructor), 
play-iso will automatically convert primitive type for a given route or form field param 
to the target custom type (e.g. convert a `Long` to a `UserId`).


Motivation
-----------

Given the sheer amount of boilerplate required for a  *single* 
[custom type](https://github.com/playframework/playframework/blob/master/framework/src/play/src/main/scala/play/api/mvc/Binders.scala#L41) 
in a route like `GET /user/:id	controllers.User.show(id: UserId)`, it's untenable to, say, 
model database primary/foreign key relationships as custom types in a Play application (100 database tables? good luck). 
The previous link does not even include the additional binding boilerplate required for Form fields 
(e.g. `mapping("id" -> of[UserId])`).

This is unfortunate as Play users will often avoid custom types due to the (manual) work required. With play-iso 
you get custom type bindings for free ;-)


Setup
-----------

See the [play-iso-example](https://github.com/godenji/play-iso-example) project.


License
----------

As this project is a derivative work (uses a modified version of the Slick 
[isomorphism macro](https://github.com/slick/slick/blob/648184c7cb710563d07b859891ed7fe46d06849d/slick/src/main/scala/slick/lifted/MappedTo.scala)
) it carries Slick's Typesafe license (BSD 2-clause).

