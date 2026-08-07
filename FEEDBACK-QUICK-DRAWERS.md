
Congratulations, you have created a working MVP!!
This is no small feat! Your `Player` class shows real command of the SimpleGfx keyboard API by handling both press and release events for smooth movement. The main thing to tighten up now is finishing what you started: a couple of fields and methods were clearly meant to do something but were left half-wired, so a pass to either complete or delete them. Implement the high score feature and pay attention to details regarding code conventions, namelly organizing your code base in packages. It organizes the code, enforces encapsulation and avoids name pollution.
## What Works Well

**Grid abstraction decouples game logic from pixels**
You decided to apply the same design choice as the car crash assignment, which is totally OK. Our implementations are a source of knowledge. It happens to make sense because the entities in your game move in discrete steps that map naturally to rows and columns. So, a grid layer pays off when your game logic is naturally cell-based, because it simplifies bounds-checking and collision logic. But it is not necessary when using Simple Gfx and it can be an unnecessary overhead if the movement of the entities is different. Also, make sure that this grid abstraction is not getting in the way and creating that funny movement.

**Shared interfaces used for real polymorphism, not just structure** 
`Movable` and `Shootable` are small, single-method interfaces that both `Player` and `Enemy` implement, letting `Game.update()` call `player.move()` / `enemy.move()` and treat both classes uniformly without an `instanceof` in sight. This is a clean, minimal application of interface segregation, each interface asks for exactly one capability, so a class only needs to implement the behaviors it actually has (`Bullet` implements `Movable` but not `Shootable`, since bullets don't shoot).

**Correct, more advanced use of the SimpleGraphics keyboard API**
`Player` implements `KeyboardHandler` directly and registers listeners for both `KEY_PRESSED` and `KEY_RELEASED`, then uses `keyReleased()` to reset `direction` to `null`. That's a meaningfully more correct way to handle continuous movement than reacting to a single key press, the player keeps moving smoothly for as long as the key is held and stops the instant it's released, instead of moving one grid cell per keystroke. 

## Areas for Improvement

### 1. `Player.isAlive()` always returns `false`, and `hit()` doesn't update any state
In `Player.java`, `alive` is initialized to `false` and never set to `true` anywhere, so `isAlive()` is permanently `false` from the moment the game starts. Worse, `hit()` doesn't mutate `alive` at all — it just returns `!alive`.
Compare this with `Enemy.hit()`. Right now your win/lose logic in `Game.resolveCollisions()` happens to work because it tracks `playerWasHit`/`enemyWasHit` as local booleans instead of calling `isAlive()`, but that means `Player.alive`, `isAlive()`, and `hit()` are dead code that looks like it should matter but doesn't, and the two `hit()` methods now mean different things depending on which class you're reading. Fix `Player.hit()` so the state of the object actually reflects what happened, the same way `Enemy` does, and so both classes honor the same contract for what implementing "being hit" means.

### 2. Dead fields and a no-op method left over from earlier iterations
A few pieces of the codebase look like they should do something but don't, which makes the classes harder to trust at a glance.

### 3. Leftover commented-out code and mixed-language comments
Don't underestimate the importance of Code Conventions they help keeping code clean and consistent as something that pays off during maintenance. Imagine, six months from now, a reader (including a teammate on the team who doesn't read Portuguese) won't know if the commented block is a TODO, an alternative implementation to consider, or safe to delete, and personal work-log comments like `Lorenzo:` don't belong in the shared codebase once the change is committed (that's what `git blame` and commit messages are for). 

### 4. A natural fit for this week's Strategy pattern: your own README already names it
Your `Readme` file lists a "Future improvement: make the enemy follow the player" for `Enemy.move()`, which currently always picks a random direction (`Math.random() < 0.5`). Rather than adding an `if` to branch between "random" and "follow player" behavior inside `Enemy.move()`, which is exactly the kind of branching this week's Strategy design pattern material warns grows unmanageable as more behaviors are added, you could extract a `MovementStrategy` interface to implement the various directions and actions regarding movements. This would let you add the follow-player behavior your README already envisions as a new class instead of restructuring `Enemy.move()`. 

### 5. Implemente the high score feature
Do this using your recently acquired knowledge about Java I/O and Streams!