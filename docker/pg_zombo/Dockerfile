FROM postgres:10-alpine as intermediate
ENV ZOMBODB_VERSION 10-1.0.0

RUN apk --no-cache add curl git build-base curl-dev wget

RUN \
  cd /usr/src && \
  wget --quiet https://github.com/zombodb/zombodb/archive/v$ZOMBODB_VERSION.tar.gz && \
  tar zxf v$ZOMBODB_VERSION.tar.gz && \
  cd zombodb-$ZOMBODB_VERSION && \
  make clean install && \
  cd .. && \
  rm -rf zombodb-$ZOMBODB_VERSION
