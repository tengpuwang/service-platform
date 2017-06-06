#!/usr/bin/env bash
docker run --name tengpu-web-api -d -p 8000:8000 --link mongo:mongo tengpu-web-api