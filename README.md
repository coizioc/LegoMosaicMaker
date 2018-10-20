# LegoMosaicMaker

This program will input an image and convert it into a Lego mosaic, or an image whose pixels represent lego bricks. Currently, the program tries to minimize the number of total bricks used, and it is mostly successful in that regard.

For example, this program converted this image:

![250x250 Github logo](https://i.imgur.com/5jXhlUm.png)

To this:

![250x250 Github logo Lego mosaic](https://i.imgur.com/vhbNLpQ.png)

Where each of the rectangles is a Lego brick.

This program will also create a txt file containing the list of bricks needed to build this mosaic:

```
To complete this, you will need:
2 Chrome Black 12x1 bricks
12 White 4x4 bricks
5 White 16x2 bricks
1 White 4x10 brick
2 Speckle Black-Copper 1x10 bricks
47 White 1x2 bricks
9 Black 3x2 bricks
3 Black 12x6 bricks
...
8 White 6x4 bricks
1 Black 10x4 brick
18 White 4x1 bricks
For a total of 2582 bricks.
```

If you are trying to run this program on your own machine, you will need the colors.csv file from [this](https://www.kaggle.com/rtatman/lego-database) link.
