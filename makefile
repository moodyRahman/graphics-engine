
all:
	javac *.java
	java Parser
	convert -delay 30 -loop 0 *.ppm  sksk.gif
	animate -resize 300% -filter point sksk.gif


clean:
	-rm *.png
	-rm *.ppm
	-rm *.class


run:	
	javac *.java
	java Parser


animate-clean:
	rm *.png
	rm *.ppm
	javac *.java
	java Parser
