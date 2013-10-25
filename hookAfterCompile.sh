#!/bin/bash

ed="eclipse/Minecraft/bin"
bdf="bin_data_forge"
bd="bin_data"
args="-d --preserve=all -fr"
cargs=""$args" "$bd/*" "$bdf/*" "$ed/""

cp $cargs
