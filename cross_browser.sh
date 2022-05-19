#!/bin/bash
mvn verify -Dbrowser=chrome
mvn verify -Dbrowser=edge
mvn verify -Dbrowser=firefox