BUILDER = ant
BUILD_TARGET = debug

# ANDROID_HOME is an enviroment variable pointing to directory where android
# sdk is located.
ANDROID_TOOLS_DIR = $(ANDROID_HOME)/tools
# Android virtual device name, these devices are located in ~/.android/avd
AVD_NAME ?= test_avd


all: build
.PHONY: all


build:
	$(BUILDER) $(BUILD_TARGET)
.PHONY: build


install: uninstall
	$(BUILDER) installd
.PHONY: install


uninstall:
	$(BUILDER) uninstall
.PHONY: uninstall


clean:
	$(BUILDER) clean
.PHONY: clean


run-emulator:
	$(ANDROID_TOOLS_DIR)/emulator -avd $(AVD_NAME) -gpu on -wipe-data
.PHONY: run-emulator
