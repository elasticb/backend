#!/bin/sh
cd $TRAVIS_BUILD_DIR/backend/comunications
sbt clean coverage test
