package cd4017be.circuits.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import cd4017be.circuits.item.ItemWireless8bit;
import cd4017be.lib.TileBlock;

public class BlockWireless8bit extends TileBlock {
	public static final PropertyInteger prop = PropertyInteger.create("type", 0, 1);
	
	public BlockWireless8bit(String id) {
		super(id, Material.iron, ItemWireless8bit.class, 0x70);
		this.setDefaultState(this.blockState.getBaseState().withProperty(prop, 0));
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, prop);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.blockState.getBaseState().withProperty(prop, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(prop);
	}
	
}
