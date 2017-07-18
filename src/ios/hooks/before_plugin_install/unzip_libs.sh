#!/usr/bin/env bash

cd $(dirname $0)
cd ../..

if [ ! -e TXRTMPSDK.framework ]; then unzip TXRTMPSDK.framework.zip; fi
if [ -e TXRTMPSDK.framework.zip ]; then rm TXRTMPSDK.framework.zip; fi
