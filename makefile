
all:
	javac *.java
	display -filter point -resize 300% d.ppm


clean:
	rm *.class
	rm *.ppm


run:
	java Matrix
