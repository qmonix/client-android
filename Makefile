#
# Builds android SDK JAR.
#
# Note: ant might fail to locate ivy, if this is the case you should add
# ivy path to CLASSPATH environment variable.
# E.g. export CLASSPATH=/usr/share/java/ivy.jar
#


ANDROID_SDK_VERSION ?= unknown

SDK_JAR_NAME = qmonix-android-client.jar
SDK_JAR_PATH = build/$(SDK_JAR_NAME)


all: build
.PHONY: all


build:
	ant -Dproject.version=$(ANDROID_SDK_VERSION)
.PHONY: build


clean:
	ant clean
.PHONY: clean
