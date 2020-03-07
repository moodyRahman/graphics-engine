
all:
	javac *.java
	java Parser
	convert -delay 30 -loop 0 *.ppm  sksk.gif
	animate -resize 300% -filter point sksk.gif


clean:
	rm *.class
	rm *.ppm


run:
	javac *.java
	java Parser


animate-clean:
	rm *.ppm
	javac *.java
	java Parser
