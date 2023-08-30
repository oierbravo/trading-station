<!-- modrinth_exclude.start -->
Trading Station
=============
<!-- modrinth_exclude.end -->
Item trading machine.
Made for modpacks. It doesn't add any recipe.

Features
--------

- Basic station with no power requirements.
- Powered station with RF power requirements.
- Custom *Trading recipe*
- Indestructible variant for each station.
- Configurable consumption & progress.
- Mechanical (Create Addon) station available with companion mod.
- 
Trading recipe
---------------

- `ingredients` (required). An array/list of 1 or 2 ingredients.
- `result` (required). Single output item/block
- `processingTime` (optional). Ticks required to process. Default to 1. Powered machine has a 5x speed.
- `biome` (optional). Biome requirement for the recipe. Default any.
- `exclusiveTo` (optional). Required station. Defaults any. Possible values: `basic`, `powered`, `mechanical`

### Example (basic input & output)
```
{
  "type": "trading_station:trading",
  "ingredients": [
    {"item": "minecraft:emerald", "count": 5}
  ],
  "result": {
    "item": "minecraft:diamond",
    "count": 5
  },
  "processingTime": 500
}
```
### Example (output item with NBT)
```
{
  "type": "trading_station:trading",
  "ingredients": [
    {"item": "minecraft:diamond", "count": 5}
  ],
  "result": {
    "item": "minecraft:enchanted_book",
    "nbt": "{StoredEnchantments: [{id:\"looting\",lvl:3s}]}"
  },
  "processingTime": 100
}

```
### Example (biome requirement)
```
{
	"type": "trading_station:trading",
	"result": {
		"item": "minecraft:diamond_sword",
		"count": 1,
		"nbt": "{Damage:0,Enchantments:[{id:\"mending\",lvl:1s}]}"
	},
	"ingredients": [
		{
			"item": "minecraft:diamond",
			"count": 5
		}
	],
	"processingTime": 100,
	"biome": {
		"name": "minecraft:plains"
	}
}
```
### Example (exclusiveTo)
```
{
	"type": "trading_station:trading",
	"result": {
		"item": "minecraft:diamond_sword",
		"count": 1,
		"nbt": "{Damage:0,Enchantments:[{id:\"sharpness\",lvl:1s}]}"
	},
	"ingredients": [
		{
			"item": "minecraft:diamond",
			"count": 5
		}
	],
	"processingTime": 100,
	"exclusiveTo": [
		"powered"
	]
}
```

KubeJS 6.1 Integration
----------------------
```
ServerEvents.recipes(event => {
  /**
  *  event.recipes.tradingStationTrading(Result Item, Input Ingredients[])
  *  .processingTime(Int) [optional]
  *  .biome(Biome|BiomeTag) [optional]
  *  .exclusiveTo(String) [optional]
  *  .exclusiveTo(String[]) [optional]
  **/

    // Basic example
    event.recipes.tradingStationTrading(Item.of('minecraft:emerald', 5),[Item.of("5x minecraft:diamond")]);
    event.recipes.tradingStationTrading(Item.of('minecraft:emerald', 5),[Item.of("5x minecraft:oak_log"),Item.of("10x minecraft:birch_log")]).processingTime(100);
    event.recipes.tradingStationTrading(Item.of('minecraft:emerald', 5),[Item.of("5x minecraft:diamond")]).processingTime(100);
    event.recipes.tradingStationTrading(Item.of('minecraft:andesite',2),[Item.of("2x minecraft:cobblestone")]).processingTime(100);

    //Enchanted book result
    event.recipes.tradingStationTrading(Item.of('minecraft:enchanted_book', '{StoredEnchantments:[{id:"power",lvl:5s}]}').strongNBT(),[Item.of("minecraft:stone")]).processingTime(100)

    // With biome requirement
    event.recipes.tradingStationTrading(Item.of('minecraft:diamond_sword', '{Enchantments:[{id:"power",lvl:5s}]}').strongNBT(),[Item.of("5x minecraft:diamond")]).processingTime(100).biome('#minecraft:is_beach');
    event.recipes.tradingStationTrading(Item.of('minecraft:diamond_sword', '{Enchantments:[{id:"mending",lvl:1s}]}').strongNBT(),[Item.of("5x minecraft:diamond")]).processingTime(100).biome('minecraft:plains');

    //With exclusive to requirement
    event.recipes.tradingStationTrading(Item.of('minecraft:diamond_sword', '{Enchantments:[{id:"looting",lvl:1s}]}').strongNBT(),[Item.of("5x minecraft:diamond")]).processingTime(100).exclusiveTo('powered');
    event.recipes.tradingStationTrading(Item.of('minecraft:diamond_sword', '{Enchantments:[{id:"sharpness",lvl:1s}]}').strongNBT(),[Item.of("5x minecraft:diamond")]).processingTime(100).exclusiveTo(['powered','mechanical']);

})
```