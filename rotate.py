import os


script = """move
{x} {y} 0
apply
ident
save
{fname}.ppm"""


rotate = """rotate
x 5
rotate
y 5
rotate
z 5
apply
ident
save
{fname}.ppm"""


trans = """move
5 -2 0
apply
ident
save
{fname}.ppm"""


scale = """scale
1.1 1.1 1.1
apply
ident
save
{fname}.ppm"""


# for z in range(12):
#         z += 1
#         toin = "{:02d}".format(z)
#         command = script.format(fname=toin, x=4, y=4)
#         print(command)

# for x in range(13, 25):
#         command = rotate.format(fname = x)
#         print(command)

# for d in range(23, 30):
#         command = trans.format(fname = d)
#         print(command)

for g in range(30, 36):
        command = scale.format(fname=g)
        print(command)