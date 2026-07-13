# Spawn Rules

A server-side Fabric mod that lets you block specific mobs and animals from spawning on your server. It cancels spawning (natural, mob spawners, trial spawners, world gen, and reinforcements) early in the tick loop to save server performance.

Spawning from spawn eggs, summon commands, and portal travel is always allowed so you can still test things in creative mode.

You can manage all settings either visually through the in-game GUI or by editing the configuration file directly.

## Configuration

### In-Game GUI
Run the command `/spawnrules config` to open the configuration menu. 
* This opens a chest menu listing all survival-spawnable mobs.
* Click any mob to open its dimension settings and toggle whether it can spawn in the Overworld, Nether, or End.
* All changes made in the GUI are instantly applied and saved.

### Configuration File
The config file is generated at `config/spawnrules.json` after running the server once.

Here is an example config that blocks zombies in the overworld:

```json
{
  "dimensions": {
    "minecraft:overworld": {
      "blocked_types": [
        "minecraft:zombie"
      ],
      "blocked_categories": []
    }
  }
}
```

#### Options
* `blocked_types`: A list of entity IDs (like `minecraft:zombie` or `minecraft:creeper`) to block in that dimension.
* `blocked_categories`: A list of spawn categories to block. The categories are:
  * `monster` (monsters like skeletons, creepers, etc.)
  * `creature` (passive mobs like cows, pigs, sheep)
  * `ambient` (bats)
  * `water_creature` (squids, dolphins)
  * `water_ambient` (tropical fish, cod)
  * `axolotls`
  * `underground_water_creature` (glow squids)

## Commands

* `/spawnrules config` - Opens the visual settings GUI in-game. Requires operator permission level 2.
* `/spawnrules reload` - Reloads the configuration file from disk. Requires operator permission level 2.

## License

This project is available under the CC0 license. Feel free to use it however you want.
