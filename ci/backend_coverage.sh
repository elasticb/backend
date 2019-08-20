#!/bin/sh
cd $TRAVIS_BUILD_DIR/backend/comunications
sbt coverageReport coveralls
