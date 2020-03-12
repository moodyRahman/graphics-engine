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
| save-convert | filename.xyz width height          | writes the current image to filename.ppm and converts it to fileimage.xyz |
| circle       | cx cy radius                       | draws a circle                                                            |
| hermite      | x0, y0, x1, y1, rx0, ry0, rx1, ry1 | draws an hermite curve                                                    |
| bezier       | x0, y0, x1, y1, x2, y2, x3, y3     | draws a bezier curve                                                      |
