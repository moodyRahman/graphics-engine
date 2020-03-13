# Okuyasu Engine v 0.38

#### Introduction
This is a graphics engine written entirely in Java, for the class MKS66C, Computer Graphics. 

#### To run
- Clone into this repo
- run make
- edit the script.txt to change the output file

#### MoodScript
The script.txt interprets commands in MoodScript  
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
| command      | parameters                         | purpose                                                                   |
|--------------|------------------------------------|---------------------------------------------------------------------------|
| pen-color    | red green blue                     | sets the color of the edges of the image                                  |
| bg-color     | red green blue                     | sets the color of the background of the image                             |
| line         | x0 y0 z0 x1 y1 z1                  | draws a line                                                              |
| ident        |                 x                  | set the internal transformation matrix to an identity matrix              |
| scale        | sx sy sz                           | appends a scaling operation to the current image                          |
| move         | dx dy dz                           | appends a transformation operation to the current image                   |
| rotate       | (x, y, z) theta                    | appends a rotation operation to the current image                         |
| apply        |                 x                  | apply all of the pending operations to the current image                  |
| display      |                 x                  | display the current image in an JFrame                                    |
| save         | filename.ppm                       | write the current image to a filename.ppm                                 |
| save-convert | filename.ppm width height          | writes the current image to filename.ppm and converts it to fileimage.png |
| circle       | cx cy radius                       | draws a circle                                                            |
| hermite      | x0, y0, x1, y1, rx0, ry0, rx1, ry1 | draws an hermite curve                                                    |
| bezier       | x0, y0, x1, y1, x2, y2, x3, y3     | draws a bezier curve                                                      |

#### Exanple script
```
#face
circle
250 250 0 200
circle
175 325 0 50
circle
325 325 0 50
circle
175 325 0 10
circle
325 325 0 10
hermite
150 150 350 150 -100 -100 100 150
bezier
200 250 150 50 300 250 300 250
#stuycs
bezier
46 494 7 488 47 455 10 450
hermite
77 492 73 455 3 32 -6 25
bezier
82 479 80 490 69 469 68 481
bezier
91 486 91 444 112 448 111 487
bezier
161 489 114 455 132 469 139 490
bezier
111 451 114 455 132 469 139 490
hermite
185 496 179 453 -105 -23 100 23
hermite
220 493 204 453 -112 -20 -125 -29
display
save-convert
x.ppm 500 500
```
