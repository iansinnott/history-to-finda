all: add_to_scripts

build:
	lein uberjar
	
add_to_scripts: build
	cp ./target/uberjar/history-to-finda-0.1.0-SNAPSHOT-standalone.jar $(HOME)/bin/scripts/
	