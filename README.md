# Parallax-City
Procedural city generation with parallax scrolling 

A procedural java program tied to libgdx for rendering.

https://www.youtube.com/watch?v=kyph8hab9I8

Runnable binaries included and should run on most devices.

To set up source code create a default libgdx project. 
Add a shape renderer and sprite batch as well as an orthographic camera.
Tie the previously created camera to the shape renderer and batch.
Create a new TheCity class and replace the game class renderer calls in TheCity to match your created renderers.
Either remove the call to drawText which draws help or create a method in your game class that matches it.
If all goes correctly you should see a city with a blue background.
