
all:	
	-mkdir bin
	-mkdir tmp
	-mkdir pics
	javac src/*.java -d bin
	java -cp bin src.Parser


clean:
	-rm *.png
	-rm *.ppm
	-rm -rf ./bin
	-rm ./src/*.class
	-rm *.gif


run:	
	javac src/*.java -d bin
	java -cp bin src.Parser


animate-clean:
	rm *.png
	rm *.ppm
	javac *.java
	java Parser
