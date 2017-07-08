#!/usr/bin/env bash

cd $(dirname $0)
cd ../..

if [ ! -e TXRTMPSDK.framework ]; then
    unzip TXRTMPSDK.framework.zip
fi

rm TXRTMPSDK.framework.zip
