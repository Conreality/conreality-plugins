VERSION  := $(shell cat VERSION)

CURL     ?= curl

all:

android/libs/polar-ble-sdk.aar:
	$(CURL) -Lo $@ https://github.com/polarofficial/polar-ble-sdk/raw/master/polar-sdk-android/libs/polar-ble-sdk.aar

clean:
	rm -f *~

distclean: clean

mostlyclean: clean

.PHONY: clean distclean mostlyclean
.SECONDARY:
.SUFFIXES:
