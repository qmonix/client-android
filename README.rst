============
Introduction
============

This is Qmonix client implementation for Android Java based applications.
Qmonix is highly customizable, easy to use multi-platform analytics system.

See: http://docs.qmonix.com/latest/.


Build prerequisites
===================

* Google Android SDK [#f1]_.
* JDK 6 (Java Developement Kit) [#f2]_.
* Ant [#f3]_.
* Ivy [#f4]_.


Getting started
===============

Simply invoke **make** which builds Qmonix android client .jar archive. Put
this archive to your project and checkout the user guide [#f5]_ to see how to
integrate it to you android project.


Issues
======

For some reasons android sdk did not have debug certificate file
~/.android/debug.keystore.


Solution
--------

Manualy create debug certificate::

        $ cd ~/.android
        $ /usr/lib/jvm/java-6-openjdk/bin/keytool -genkey -v -keystore \
                debug.keystore -alias androiddebugkey -storepass android \
                -keypass android -keyalg RSA -validity 14000

This wil create a certificate valid for 14000 days.


.. rubric:: References

.. [#f1] http://developer.android.com/sdk/index.html
.. [#f2] http://openjdk.java.net/install/index.html
.. [#f3] http://ant.apache.org/
.. [#f4] https://ant.apache.org/ivy/
.. [#f5] http://docs.qmonix.com/latest
