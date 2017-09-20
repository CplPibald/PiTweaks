# PiTweaks

A list of tweaks (cheats) I use on my own servers to make Minecraft less grindy.

When first loading the mod, all tweaks are disabled in the config (pitweaks.cfg).  **THIS MOD WILL HAVE NO EFFECT UNTIL YOU EDIT THE CONFIG TO ENABLE TWEAKS**

# List of tweaks

## Fast Furnace Tweak

Makes vanilla furnaces 20x faster, both in cook time and fuel usage.  Furnaces cook 2 items per second, and a piece of coal lasts 4 seconds (8 items).  The ratio of fuel to items is unchanged.

This is implemented by replacing the TileEntity when a furnace is placed with a faster version.  Existing furnaces when the mod loads will need to be broken and re-placed to substitute the new tile entity.

Beware, if you remove/disable this tweak later, all of your upgraded furnaces may disappear or explode.  I really haven't tested it.

## No Repair Cost Tweak

Removes the cumulative XP cost in an anvil.  The effect is that all anvil actions cost the same as if they had never been in an anvil.

Implementation: When an item is inserted into or removed from an anvil, the "RepairCost" NBT is cleared on the item.  The anvil will attempt to add a repair cost to the output, but the mod will clear that too.  If you disable this tweak, items that have been cleared will start accumulating repair cost as normal.

