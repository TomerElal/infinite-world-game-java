PEPSE Game

Project Description:
The game that is displayed when you run the main function in the gameManager file is actually a game in which the player walks 
in an infinite world in which there are various objects such as a sun that rises and sets, a halo of the sun, trees with leaves 
on them that fall and grow, earth, sky and the player himself. It should be noted that when the player advances a number of frames
and then retraces his path, he will find the same frame in the game that he saw before and this is in order to preserve the memory 
of the game. Also the player can jump and hover for a limited time that depends on his energy level.
Features and extensions can be added to this game such as competing players, enemies, missions, stages, etc.


UML:

![uml_after](https://github.com/TomerElal/infinite-world-game-java/assets/126855038/1da2d314-645b-4f6c-be05-c6b2cb496eac)


Infinite World:
We designed an infinite world concept by creating three frames of the world and maintaining them continuously.
In each frame, we organize all objects into different layers, resulting in five layers for each frame. 
This approach allows us to easily delete a frame by removing all objects from the five layers associated with that frame,
as needed. We trigger this process when the avatar moves beyond the middle frame.

Tree:
We structured the tree component into four classes-
Tree -> This class takes a range and generates trees within that range.
Trunk -> The trunk class requires only one x-coordinate and creates a trunk at that location.
Leaves -> Similar to the trunk, the leaves class also takes an x-coordinate and generates leaves on top of the trunk.
Leaf Object -> This class extends the Block object and adds additional functionality to it.
We emphasized clear responsibilities for each class, with the exception of the Leaf Object,
which serves as a gameObject with enhanced functionality, necessitating its separate creation.

Design
In our design process, we explored the possibility of using strategies for the leaves and factories for their creation.
However, we ultimately chose to use callbacks and a single leaf class for simplicity and cleanliness.
We realized that many elements in the game could be created using the Block class, with changes such as altering colors or,
in the case of leaves, extending the class.

Acknowledgments
Thank you for taking the time to explore our project.

Authors: Yoel & Tomer

