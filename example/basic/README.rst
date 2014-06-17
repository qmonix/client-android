=====
About
=====

This is a very simple example how to use Qmonix Android SDK.


Run the sample
==============


1. Set ANDROID_HOME environment variable::

        $ export ANDROID_HOME=~/adt-bundle-linux/sdk

2. Build Qmonix Android SDK jar::

        $ cd ../../
        $ make build

3. Build this sample application::

        $ make

4. Run Android emulator::

        $ AVD_NAME=my_avd make run-emulator

   AVD_NAME specifies Android Virtual Device name which the emulator should
   use.

5. Install sample to the emulator::

        $ make install
