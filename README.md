Simple Snake
============

**Work still in pogress!**  
Simple Snake game, where your goal is to move around your snake (blue square) to collect as much cherries (red squares) as you can. Nearly all the game logic has been coded on the `PlayboardView` **`extends`** `View` class. You can (manullay) adjust some game parameters by modifying the constants of that `PlayboardView` class. Some of those adjustments are:  

* Debug mode: `boolean __DEBUG`.
* Board size: `int BOARD_SIZE`.
* Number of fruits on screen: `int NUM_FRUITS`.
* Initial level: `int START_LEVEL`.
* Number of nutrients to raise level: `int NUTRIENTS_LEVEL`.
* Collision with bondaries do kill: `boolean SOLID_BOUNDARIES`.
* Sanke start position: `int SNAKE_START_X` and `int SNAKE_START_Y`.
* and more...

Controls
--------

* To start the game, simple launch the application. After two seconds, the snake will begin moving towards the left.
* If you collide with any board border or with your own tail, the game will stop. To relaunch, you must exit the application and launch it again.
* To pause the game, press HOME. If you launch the game again, you should find yourself at the point where you left before.
* To change snake direction, fling with your finger to that direction. (*Keep in mind that the snake won't move until the next game tick triggers*)

Next steps...
-------------

While the logic is pretty solid and mostly done, it still need some adjustments and big fixing. My intention is to add some kind of menu at some point, to give it the look more like the feel of a refined game. An internet statistics and game score system would be also great. You know... there's still A LOT OF work to do! :)

Developed By
------------

Santiago Gonzàlez - [santiago.gon.ber@gmail.com][1]

The MIT Licence
---------------

    Copyrigth 2013 Santiago Gonzàlez

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.

[1]: santiago.gon.ber@gmail.com