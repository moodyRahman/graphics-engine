# OkuyasuEngine v 0.53

## Introduction
This is a graphics engine written entirely in Java, for the class MKS66C, Computer Graphics.  

All images will be saved inside ```./pics```


#### To run
- ```make``` 
  - compile the source code and execute with ```./scripts/stuyface.mscript```
- ```make compile```
  - compile the source code
- ```make run script=./your_script.mscript```
  - execute with the specified script
- ```make info```
  - generate documentation
- ```make clean```
  - delete root directory images and and compiled code
- ```make deepclean```
  - delete all makefile generated code
- ```make jar```
  - generate OkuyasuEngine.jar
    - execute via ```java -jar OkuyasuEngine.jar ./your_script.mscript```

## MoodScript
The OkuyasuEngine interprets commands in MoodScript  
```
command1  
command2  
parameter1 parameter2  
command3  
paramater1 paramater2
``` 
Some commands are followed by parameters while some are not.  
Here is the commandset (parameters enclosed in parentheses must be one of the enclosed options)
Anything that is not in the command set is a comment  
"-" denotes a command with no parameters
| command             | parameters                              | purpose                                                                   |
|---------------------|-----------------------------------------|---------------------------------------------------------------------------|
| ```pen-color```     | red green blue                          | sets the color of the edges of the image                                  |
| ```bg-color```      | red green blue                          | sets the color of the background of the image                             |
| ```move```          | dx dy dz                                | appends a transformation operation to the coordinate stack                |
| ```scale```         | sx sy sz                                | appends a scaling operation to the coordinate stack                       |
| ```rotate```        | (x, y, z) theta                         | appends a rotation operation to the coordinate stack                      |
| ```push```          |                 -                       | push the current coordinate system to the stack                           |
| ```pop```           |                 -                       | pop the current coordinate system to the stack                            |
| ```line```          | x0 y0 z0 x1 y1 z1                       | draws a line                                                              |
| ```circle```        | cx cy radius                            | draws a circle                                                            |
| ```hermite```       | x0 y0 x1 y1 rx0 ry0 rx1 ry1             | draws an hermite curve                                                    |
| ```bezier```        | x0 y0 x1 y1 x2 y2 x3 y3                 | draws a bezier curve                                                      |
| ```sphere```        | x y z radius                            | draws a sphere                                                            |
| ```torus```         | x y z inner_radius outer_radius         | draws a torus                                                             |
| ```box```           | x y z width height depth                | draws a box                                                               |
| ```clear```         |                 -                       | removes all shapes from the image                                         |
| ```pause```         |                 -                       | pauses the script execution                                               |
| ```save```          | filename.ppm                            | write the current image to a filename.ppm                                 |
| ```save-convert```  | filename.ppm width height               | writes the current image to filename.ppm and converts it to fileimage.png |
| ```display```       |                 -                       | display the current image in an JFrame                                    |
| ```display-custom```|xresolution yresolution                  | display the current image in an JFrame of the given resolution            |
#### Example script
```
bg-color
0 0 0
pen-color
32 194 14
push
scale 2 2 2
push
move
200 50 0
rotate
y 30
rotate
x 10
box
-50 0 0 20 20 100
box
50 0 0 20 20 100
push
move
0 70 0
box
-50 0 -80 20 70 20
box
50 0 -80 20 70 20
push
move
0 100 -80
sphere
0 0 0 110
push
move
-110 0 0
box
-20 0 20 -10 150 -20
pop
push
move
110 0 0
rotate
x -120
box
0 0 20 10 150 20
box
0 -150 20 10 -10 -80
push
rotate
x 90
torus
0 120 100 10 20
pop
pop
push
move
0 160 0
sphere
0 0 0 50
sphere
30 30 30 5
sphere
-30 30 30 5
circle
0 0 30 10
display-custom
1000 1000
# credits to George Zhou for my favorite script of all time
```

#### Future Plans
- pass this class