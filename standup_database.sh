#!/usr/bin/env bash

echo "Standing up mysql"
docker run -d \
    --name lcag-mysql \
    --network lcag-automation-network \
    -p 4306:3306 \
    -e MYSQL_ROOT_PASSWORD=password \
    -e MYSQL_DATABASE=db \
    -e MYSQL_USER=user \
    -e MYSQL_PASSWORD=password \
    mysql:5.6
