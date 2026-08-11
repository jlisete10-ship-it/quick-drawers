Game Loop:
- Shoot the enemy AI, survive, defeat the enemy, and repeat.

Game Mechanics:

- Player class:
    - Moves up and down.
    - Shoots when the player presses a key.
    - If a bullet hits the enemy, the enemy dies.
    - Bullets travel to the right.

- Enemy class:
    - Moves randomly up and down.
      (Future improvement: make the enemy follow the player.)
    - Shoots automatically at regular intervals.
    - If a bullet hits the player, the player dies (health = 0).
    - Bullets travel to the left.

- Movable interface:
    - move();
    - The player moves according to keyboard input.
    - The enemy moves randomly along the Y-axis.

- Shootable interface:
    - shoot();
    - The player shoots to the right.
    - The enemy shoots to the left.

- Bullet class:
    - Once the player shoots, the bullet becomes an independent object.
    - Player -> shoot() -> creates a Bullet -> Bullet.move() -> collision -> enemy dies.

- Game class:

    - init():
        - Create the game arena.
        - Create the player.
        - Register the keyboard controls.
        - Create the enemy.

    - start():
        - Start the game loop.
        - Update player and enemy movement.
        - Update bullets.
        - Check collisions.

Improvements implemented:
        Added a game menu with Start and Restart buttons.
        Added multiple enemies and enemy spawning.
        Added Normal and Easy difficulty modes.
        Added Easy Mode activation using the `X` key.
        Added player lives and life icons.
        Added score and persistent high score using file I/O.
        Added game over and victory screens.
        Improved game restart by reusing the Player and keyboard listener.
        Added new game resources and background.





