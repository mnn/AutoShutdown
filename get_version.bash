#!/bin/bash
v=`grep -E "VERSION ?=" src/main/java/tk/monnef/autoshutdown/AutoShutdown.java | sed -r 's@^.*"(.*)".*;$@\1@'`
echo -n $v
