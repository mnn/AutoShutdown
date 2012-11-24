#!/bin/bash

version=0.1.0

output=jar_output
outtmp=$output/tmp
dist=$output/dist

echo -n Preparing...

mkdir $output &>/dev/null
mkdir $outtmp &>/dev/null
mkdir $dist &>/dev/null

od=`pwd`

rm -fr "./$outtmp/*"
rm -fr "./$dist/*"
touch "$outtmp/.placeholder"

echo Done

echo -n Copying mod files...

#cp eclipse/Minecraft/bin/jaffas_01.png eclipse/Minecraft/bin/jaffas_02.png eclipse/Minecraft/bin/jaffas_logo.png eclipse/Minecraft/bin/guifridge.png eclipse/Minecraft/bin/jaffabrn1.png eclipse/Minecraft/bin/mcmod.info eclipse/Minecraft/bin/guicollector.png eclipse/Minecraft/bin/sharpener.wav eclipse/Minecraft/bin/suck.wav "$outtmp"
cp -r reobf/minecraft/monnef "$outtmp"

outName="mod_autoshutdown_$version"

cd "$outtmp"
zip -q -9r "../$outName.jar" ./*

cd "$od"

echo Done
#jar packing done

#prepare libs - jsoup
echo -n Copying libraries...
mkdir $dist/mods &>/dev/null
cp "$output/$outName.jar" "$dist/mods"
echo Done

echo -n Packing...
cd $dist
zip -q -9r "../${outName}_packed.zip" ./*

echo Done
