#!/bin/bash

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

echo -n Detecting version...
version=`grep -Ei "version = \"" src/minecraft/monnef/autoshutdown/Autoshutdown.java | sed -r 's/^.*version = "(.*?)".*$/\1/'`
verLen=${#version}
if [ $verLen -lt 1 -o $verLen -gt 5 ]; then
	echo "FAILURE"
	exit 1
fi
echo " ($version) Done"

echo -n Copying mod files...

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
