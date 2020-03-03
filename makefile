
all:
	javac *.java
	java Parser
	convert -delay 30 -loop 0 *.ppm  sksk.gif
	animate sksk.gif 


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
