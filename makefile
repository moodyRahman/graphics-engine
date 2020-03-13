
all:	
	mkdir bin
	javac src/*.java -d bin
	java -cp bin src.Parser
	convert -delay 30 -loop 0 *.ppm  sksk.gif
	animate -resize 300% -filter point sksk.gif


clean:
	-rm *.png
	-rm *.ppm
	-rm -rf ./bin
	-rm ./src/*.class


run:	
	javac src/*.java -d bin
	java -cp bin src.Parser


animate-clean:
	rm *.png
	rm *.ppm
	javac *.java
	java Parser
