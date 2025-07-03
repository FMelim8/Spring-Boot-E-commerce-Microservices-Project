#!/bin/bash

echo "[entrypoint] Looking for .gz logs in /logs..."

find /logs -type f -name "*.gz" -exec sh -c '
  for file do
    echo "Unzipping $file"
    gunzip -f "$file"
  done
' sh {} +

echo "[entrypoint] Starting Filebeat..."
exec filebeat -e
