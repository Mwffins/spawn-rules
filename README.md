# Spawn Rules

A server-side Fabric mod that lets you block specific mobs and animals from spawning on your server. It cancels spawning (natural, mob spawners, trial spawners, world gen, and reinforcements) early in the tick loop to save server performance.

Spawning from spawn eggs, summon commands, and portal travel is always allowed so you can still test things in creative mode.

## Configuration

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

### Options
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

* `/spawnrules reload` - Reloads the configuration file from disk. Requires operator permission level 2.

## License

This project is available under the CC0 license. Feel free to use it however you want.
