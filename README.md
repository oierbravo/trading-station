# Trading Station
- Item trading machine. 
- Made for modpacks. __It doesn't add any recipe.__


## 1.21.1-1.x Version Requires Mechanicals Lib
- [Curseforge](https://www.curseforge.com/minecraft/mc-mods/mechanicals-lib "Curseforge")
- [Modrinth](https://modrinth.com/mod/mechanicals-lib "Modrinth")

## [Dedicated wiki](https://wiki.mechanicalmods.net/mods/trading-station/)

## Features
- Basic station with no power requirements.
- Powered station with RF power requirements.
- Custom *Trading recipe*
- Custom recipe requirements per recipe.
- Indestructible variant for each station.
- Configurable consumption & progress.
- Mechanical (Create Addon) station available with companion mod.[Link](https://modrinth.com/mod/mechanical-trading-station)

## Trading recipe
- `"type": "trading_station:trading"`
- `result`:Output item. Item `components` allowed.
- `ingredients`: Required items.
- `processingTime`: Required time in ticks.
- `recipeRequirements`: Custom recipe requirements. [WIKI](https://wiki.mechanicalmods.net/mods/mechanicals-lib/recipe-requirements/)
### Example (One ingredient)
```json
{
	"type": "trading_station:trading",
	"result": {
		"id": "minecraft:diamond",
		"count": 5
	},
	"ingredients": [
		{
			"ingredient": {
				"item": "minecraft:emerald"
			},
			"count": 5
		}
	]
}
```
### Example (Two ingredients)
```json
{
  "type": "trading_station:trading",
  "result": {
    "id": "minecraft:gold_block",
    "count": 5
  },
  "ingredients": [
    {
      "ingredient": {
        "item": "minecraft:oak_log"
      },
      "count": 5
    },
    {
      "ingredient": {
        "item": "minecraft:birch_log"
      },
      "count": 10
    }
  ],
  "processingTime": 250
}
```
### Example (Enchanted book)
```json
{
	"type": "trading_station:trading",
	"result": {
		"id": "minecraft:enchanted_book",
		"count": 1,
		"components": {
			"minecraft:stored_enchantments": {
				"levels": {
					"minecraft:density": 3
				}
			}
		}
	},
	"ingredients": [
		{
			"ingredient": {
				"item": "minecraft:diamond"
			},
			"count": 5
		}
	],
	"processingTime": 100
}
```
## Example (Machine Requirement)
```json
{
	"type": "trading_station:trading",
	"result": {
		"id": "minecraft:emerald_block",
		"count": 1
	},
	"ingredients": [
		{
			"ingredient": {
				"item": "minecraft:diamond"
			},
			"count": 5
		}
	],
	"processingTime": 100,
	"requirements": [
		{
			"value": [
				"powered",
				"mechanical"
			],
			"type": "trading_station:machine_id"
		}
	]
}
```
## KubeJS
### Some examples
```js
// Processing time
event.recipes.trading_station.trading(Item.of('minecraft:gold_block', 5),[Item.of("5x minecraft:oak_log"),Item.of("10x minecraft:birch_log")]).processingTime(250);

//Enchanted book result
event.recipes.trading_station.trading(Item.of('minecraft:enchanted_book[stored_enchantments={levels:{"minecraft:unbreaking":3}}]', 1),[Item.of("minecraft:diamond", 5)]).processingTime(100);

//With machine requirement
event.recipes.trading_station.trading(Item.of('minecraft:emerald_block'),[Item.of("5x minecraft:diamond")]).processingTime(100).requirements(MachineId.of(["powered","mechanical"]));
```
### Machine ID (binding)
```js
MachineId.of(["powered","mechanical"])
```