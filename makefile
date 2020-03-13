
all:	
	-mkdir bin
	javac src/*.java -d bin
	java -cp bin src.Parser scripts/stuyface.mscript


compile:
	javac src/*.java -d bin


clean:
	-rm *.png
	-rm *.ppm
	-rm -rf ./bin
	-rm ./src/*.class
	-rm *.gif

run:	./bin
	echo $(script)
	javac src/*.java -d bin
	java -cp bin src.Parser $(script)

animate-clean:
	rm *.png
	rm *.ppm
	javac *.java
	java Parser
