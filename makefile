
all:
	javac *.java
	java Parser
	display -filter point -resize 300% animatespheres.gif


clean:
	rm *.class
	rm *.ppm


run:
	javac *.java
	java Parser
