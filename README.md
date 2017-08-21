# Parallax-City
Procedural city generation with parallax scrolling 

![](http://i.imgur.com/kUiQMis.png)

A procedural java program tied to libgdx for rendering.

https://www.youtube.com/watch?v=kyph8hab9I8

Runnable binaries included and should run on most devices.

# Controls

WASD Controls the camera.

R and F increases and decreases the amout of generated layers.

UP and DOWN arrows to increase and decrease the buffer size

LEFT and RIGHT arrows to increase and decrease scroll speed.

= and - to control zoom.

G to generate new city.

H to close help.

# To set up source code 

Create a default libgdx project. 

Add a shape renderer and sprite batch as well as an orthographic camera.

Tie the previously created camera's projection matrix to the shape renderer and batch.

Create a new TheCity class and replace the game class renderer calls in TheCity to match your created renderers.

Either remove the call to drawText which draws help or create a method in your game class that matches it.

If all goes correctly you should see a city with a blue background.

