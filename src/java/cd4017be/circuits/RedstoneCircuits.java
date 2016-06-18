/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cd4017be.circuits;

import cd4017be.api.computers.ComputerAPI;
import cd4017be.api.recipes.RecipeAPI;
import cd4017be.circuits.block.BlockInvConnector;
import cd4017be.circuits.block.BlockRSPipe1;
import cd4017be.circuits.block.BlockRSPipe8;
import cd4017be.circuits.block.BlockWireless8bit;
import cd4017be.circuits.item.ItemCircuit;
import cd4017be.circuits.item.ItemProgramm;
import cd4017be.lib.BlockGuiHandler;
import cd4017be.lib.BlockItemRegistry;
import cd4017be.lib.ConfigurationFile;
import cd4017be.lib.DefaultItemBlock;
import cd4017be.lib.TileBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import static cd4017be.circuits.Objects.*;

/**
 *
 * @author CD4017BE
 */
@Mod(modid="Circuits", useMetadata = true)
public class RedstoneCircuits 
{
    // The instance of your mod that Forge uses.
    @Instance("AutomatedRedstone")
    public static RedstoneCircuits instance;
    
    // Says where the client and server 'proxy' code is loaded.
    @SidedProxy(clientSide="cd4017be.circuits.ClientProxy", serverSide="cd4017be.circuits.CommonProxy")
    public static CommonProxy proxy;
    
    public static CreativeTabs tabCircuits;
    private static final String recipeFile = "automatedRedstone.rcp";
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) 
    {
        ConfigurationFile.init(event, recipeFile, "/assets/circuits/config/recipes.rcp");
    	BlockItemRegistry.setMod("circuits");
        tabCircuits = new CreativeTabCircuits("circuits");
        initBlocks();
        initItems();
        RecipeAPI.loadRecipes(recipeFile, RecipeAPI.PRE_INIT);
    }
    
    @Mod.EventHandler
    public void load(FMLInitializationEvent event) 
    {
    	BlockItemRegistry.setMod("circuits");
        BlockGuiHandler.registerMod(this);
        proxy.registerRenderers();
        RecipeAPI.loadRecipes(recipeFile, RecipeAPI.INIT);
        ComputerAPI.register();
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	RecipeAPI.loadRecipes(recipeFile, RecipeAPI.POST_INIT);
    }
    
    private void initBlocks()
    {
        (programmer = TileBlock.create("programmer", Material.WOOD, SoundType.WOOD, DefaultItemBlock.class, 0x1)).setCreativeTab(tabCircuits).setHardness(1.5F).setResistance(10F);
    	(assembler = TileBlock.create("assembler", Material.ROCK, SoundType.STONE, DefaultItemBlock.class, 0x1)).setCreativeTab(tabCircuits).setHardness(1.5F).setResistance(10F);
    	(circuit = TileBlock.create("circuit", Material.ROCK, SoundType.STONE, ItemCircuit.class, 0x50)).setCreativeTab(tabCircuits).setHardness(1.5F).setResistance(10F);
    	(rsp8bit = new BlockRSPipe8("rsp8bit", Material.IRON, SoundType.METAL)).setCreativeTab(tabCircuits).setHardness(1.0F).setResistance(10F);
    	(rsp1bit = new BlockRSPipe1("rsp1bit", Material.IRON, SoundType.METAL)).setCreativeTab(tabCircuits).setHardness(1.0F).setResistance(10F);
    	(lever8bit = TileBlock.create("lever8bit", Material.ROCK, SoundType.STONE, DefaultItemBlock.class, 0x1)).setCreativeTab(tabCircuits).setHardness(1.5F).setResistance(10F);
    	(display8bit = TileBlock.create("display8bit", Material.ROCK, SoundType.STONE, DefaultItemBlock.class, 0x1)).setCreativeTab(tabCircuits).setHardness(1.5F).setResistance(10F);
    	(logicConv = TileBlock.create("logicConv", Material.ROCK, SoundType.STONE, DefaultItemBlock.class, 0x30)).setCreativeTab(tabCircuits).setHardness(1.5F).setResistance(10F);
    	(calcConv = TileBlock.create("calcConv", Material.ROCK, SoundType.STONE, DefaultItemBlock.class, 0x30)).setCreativeTab(tabCircuits).setHardness(1.5F).setResistance(10F);
    	(wireless8bit = new BlockWireless8bit("wireless8bit")).setCreativeTab(tabCircuits).setHardness(2.0F).setResistance(20F);
    	(invReader = TileBlock.create("invReader", Material.ROCK, SoundType.STONE, DefaultItemBlock.class, 0x0)).setCreativeTab(tabCircuits).setHardness(1.5F).setResistance(10F);
    	(itemTranslocator = TileBlock.create("itemTranslocator", Material.ROCK, SoundType.STONE, DefaultItemBlock.class, 0x0)).setCreativeTab(tabCircuits).setHardness(1.5F).setResistance(10F);
    	(invConnector = new BlockInvConnector("invConnector", Material.GLASS, SoundType.GLASS)).setCreativeTab(tabCircuits).setHardness(0.5F).setResistance(10F);
    	proxy.registerBlocks();
    }
    
    private void initItems()
    {
    	circuitPlan = new ItemProgramm("circuitPlan");
    }
}
