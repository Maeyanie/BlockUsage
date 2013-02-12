package maeyanie;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import net.minecraft.world.*;
import net.minecraft.block.*;
import net.minecraft.item.*;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.*;
import cpw.mods.fml.relauncher.*;

@Mod(name="BlockUsage", version="1.4.7.1", modid="BlockUsage")
public class BlockUsage {
	@Mod.PostInit
	public void postInit(FMLPostInitializationEvent event) {
		dumpBlocks();
	}

	private void dumpBlocks() {
		try {
			Multimap<ModContainer, BlockProxy> blockRegistry = 
				ReflectionHelper.getPrivateValue(GameRegistry.class, null, "blockRegistry");
				
			HashMap<BlockProxy, ModContainer> blockToMod = new HashMap<BlockProxy, ModContainer>();
			
			for (Map.Entry<ModContainer, BlockProxy> entry : blockRegistry.entries()) {
				blockToMod.put(entry.getValue(), entry.getKey());
			}
			
		
			FileWriter file = new FileWriter("blockusage.txt");

			for (int x = 0; x < 4096; x++) {
				if (Block.blocksList[x] == null) {
					file.write("Block ID "+x+": Unused\n");
				} else {
					ModContainer mod = blockToMod.get(Block.blocksList[x]);
					if (mod != null) {
						file.write("Block ID "+x+": "+Block.blocksList[x].toString()+" ("+mod.getName()+")\n");
					} else {
						file.write("Block ID "+x+": "+Block.blocksList[x].toString()+"\n");
					}
				}
			}

			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Map<Integer, ItemData> idMap = ReflectionHelper.getPrivateValue(GameData.class, null, "idMap");
		
			FileWriter file = new FileWriter("itemusage.txt");

			for (int x = 0; x < 32000; x++) {
				if (Item.itemsList[x] == null) {
					file.write("Item ID "+x+" ("+(x-256)+"): Unused\n");
				} else {
					ItemData data = idMap.get(x);
					if (data != null) {
						file.write("Item ID "+x+" ("+(x-256)+"): "+Item.itemsList[x].toString()+" ("+data.getModId()+")\n");
					} else {
						file.write("Item ID "+x+" ("+(x-256)+"): "+Item.itemsList[x].toString()+"\n");
					}
				}
			}

			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


