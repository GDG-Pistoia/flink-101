#!/bin/bash

kcat -b localhost:9092 -t subscriptions -P
