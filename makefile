
all:
	javac *.java
	java Parser
	display -filter point -resize 300% d.ppm


clean:
	rm *.class
	rm *.ppm


run:
	javac *.java
	java Parser

animate:
	javac *.java
	java Parser

animate-clean:
	rm *.ppm
	javac *.java
	java Parser
