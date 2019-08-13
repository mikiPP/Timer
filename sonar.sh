#!/bin/bash

echo Sonar execution script

# Set the workspace to the current directory
export WORKSPACE=$(pwd)

# Execute sonar
sonar-scanner -DWORKSPACE=$WORKSPACE
