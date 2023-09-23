# PEPSE Game

## Project Description:
PEPSE Game is an infinite world adventure game where players explore a dynamic environment with various objects,
including a rising and setting sun, trees with falling leaves, earth, sky, and more.
Players can jump and hover using their energy level.


![inifinite-world](https://github.com/TomerElal/infinite-world-game-java/assets/126855038/5aa94d6f-6895-4996-a5b3-76cb89860f9b)


## Features

- Infinite world concept
- Dynamic sun and tree systems
- Player movement, jumping, and energy management
- Potential for extensions and additional features
  
## Installation

First, a third-party library must be installed from here: https://danthe1st.itch.io/danogamelab
It is also desirable to work with the IntelliJ code editor, but not mandatory.
Now after creating the project, the third-party library must be added to the dependencies,
we will do this by following the sequence of actions - File --> Project structure --> Modules --> Dependencies --> Add a new dependency --> Select the JAR folder.
Now in the opened panel we access to the path where the third party directory is located and select there both the jar file and the *entire* src folder.
Now click OK and the third party directory has been successfully added and you can use it.

  
## UML:

![uml_after](https://github.com/TomerElal/infinite-world-game-java/assets/126855038/1da2d314-645b-4f6c-be05-c6b2cb496eac)

## Extensions
Features and extensions can be added to this game such as competing players, enemies, missions, stages, etc.

## Infinite World:
I designed an infinite world concept by creating three frames of the world and maintaining them continuously.
In each frame, I organized all objects into different layers, resulting in five layers for each frame. 
This approach allows to easily delete a frame by removing all objects from the five layers associated with that frame,
as needed. This process begin when the avatar moves beyond the middle frame.

## Acknowledgments
Acknowledgment to the distributor of danoGameLab with whose help the project could be carried out in the best possible way.


