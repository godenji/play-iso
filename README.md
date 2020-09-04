play-iso
===================

Deprecated
-----------
For Scala 2.12/2.13 supported version please use [Isomorphic](https://github.com/godenji/isomorphic) instead.


Description
-----------

Eliminates binding boilerplate for your custom types in routes and forms 
(i.e. no need to define `QueryStringBindable`, `PathBindable`, and `Formatter` bindings). 
If your custom type is a value class (or has a single value constructor), 
play-iso will automatically convert primitive type for a given route or form field param 
to the target custom type (e.g. convert a `Long` to a `UserId`).
