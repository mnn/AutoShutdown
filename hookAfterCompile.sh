#!/bin/bash

ed="eclipse/Minecraft/bin"
bd="bin_data_forge"
args="-d --preserve=all -f"
cargs=""$args" "$bd/*" "$ed/""

cp $cargs
