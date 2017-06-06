#!/usr/bin/env bash
echo "update source code"
git pull
echo "start building docker image"
docker build -t tengpu-web-api .
echo "finish build"