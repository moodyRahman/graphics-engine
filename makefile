
all:	
	-mkdir bin
	javac src/*.java -d bin
	java -cp bin src.Parser scripts/stuyface.mscript


compile:
	-mkdir bin
	javac src/*.java -d bin


clean:
	-rm *.png
	-rm *.ppm
	-rm -rf ./bin
	-rm ./src/*.class
	-rm *.gif

run: ./bin
	echo $(script)
	javac src/*.java -d bin
	java -cp bin src.Parser $(script)

jar: ./bin
	cp MANIFEST.mf ./bin
	cd bin; jar cvfe OkuyasuEngine.jar src.Parser src/*.class; mv OkuyasuEngine.jar ..;