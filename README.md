# PiTweaks

A list of tweaks (cheats) I use on my own servers to make Minecraft less grindy.

# List of tweaks

## Fast Furnace Tweak (default: enabled)

Makes vanilla furnaces faster (default 20x faster), both in cook time and fuel usage.  Furnaces cook 2 items per second, and a piece of coal lasts 4 seconds (8 items).  The ratio of fuel to items is unchanged.

This is implemented by replacing the TileEntity when a furnace is placed with a faster version.  Existing furnaces when the mod loads will need to be broken and re-placed to substitute the new tile entity.

Beware, if you remove/disable this tweak later, all of your upgraded furnaces may disappear or explode.  I really haven't tested it.

## Anvil tweaks:

#### No Repair Cost (default: enabled)

Removes the cumulative XP cost in an anvil.  The effect is that all anvil actions cost the same as if they had never been in an anvil.
When an item is inserted into or removed from an anvil, the "RepairCost" NBT is cleared on the item.  The anvil will attempt to add a repair cost to the output, but the mod will clear that too.  If you disable this tweak, items that have been cleared will start accumulating repair cost as normal.

#### Allow overleveled enchantments (default: enabled)

Removes the cap on enchantment level when combining items.  Two enchantments of the same level will always produce an enchantment one level higher.

#### Always allow books (default: disabled)

Enchantments from enchanted books can always be applied to any item regardless of item type or existing enchantments.
eg: Infinity with Mending, Sharpness with Smite, Efficiency on boots, Blast protection on a hoe, or even looting on a dirt block.

## Fast Brewing Stand (default: enabled)

Brewing stands are faster.  Each brew operation takes a configurable number of ticks from 1 to 400 (default 20).

Must break and replace any existing brewing stands when applying this tweak.

## Boat Tweaks

#### Buoyant Boats (default: enabled)

Boats can be paddled up flowing water without sinking

#### Keep passengers underwater (default: disabled)

Boats no longer eject passengers after 3 seconds submerged.

## Mob Spawning Tweak (default: disabled)

When enabled, mobs can only spawn on a block that is a full solid cube.  This disables spawning on top-half slabs, upside-down stairs, hoppers, and other non-solid blocks even if they have a solid top.

## Recipe Tweaks (default: disabled)

Adds 2x2 versions of slab and stair recipes.  Also adds recipes to recombine slabs and stairs into their base blocks.
Optionally removes 3x3 versions of these recipes.

