/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cd4017be.circuits.gui;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cd4017be.circuits.tileEntity.Circuit;
import cd4017be.lib.BlockGuiHandler;
import cd4017be.lib.TileContainer;
import cd4017be.lib.templates.AutomatedTile;
import cd4017be.lib.templates.GuiMachine;

/**
 *
 * @author CD4017BE
 */
public class GuiCircuit extends GuiMachine
{
    
    private final Circuit tileEntity;
    private int sel = -1;
    private TextField text = new TextField("", 2);
    
    public GuiCircuit(Circuit tileEntity, EntityPlayer player)
    {
        super(new TileContainer(tileEntity, player));
        this.tileEntity = tileEntity;
    }
    
    @Override
    public void initGui() 
    {
        this.xSize = 86;
        this.ySize = 96;
        super.initGui();
    }
    
    @Override
	protected void drawGuiContainerForegroundLayer(int mx, int my) 
    {
		super.drawGuiContainerForegroundLayer(mx, my);
		this.drawInfo(44, 20, 34, 8, "\\i", "circuit.timer");
		this.drawInfo(57, 35, 14, 54, "\\i", "rs.filter");
		this.drawInfo(47, 35, 9, 54, "\\i", "circuit.io");
		if (this.isPointInRegion(48, 36, 7, 52, mx, my)) {
			int s = (my - guiTop - 36) / 9;
			this.drawSideCube(-64, 32, s, (byte)(tileEntity.getConfig(s + 6) / 2 + 2));
		}
	}
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) 
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(new ResourceLocation("circuits", "textures/gui/circuit.png"));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        if (tileEntity.getConfig(12) != 0) this.drawTexturedModalRect(this.guiLeft + 7, this.guiTop + 15, 86, 36, 17, 9);
        int x, y;
        for (int i = 0; i < 6; i++)
            this.drawTexturedModalRect(this.guiLeft + 47, this.guiTop + 35 + i * 9, 86, tileEntity.getConfig(i + 6) * 9, 9, 9);
        int n = tileEntity.var >> 8 & 0xff;
        x = guiLeft + 8; y = guiTop + 25;
        for (int i = 0; i < n; i++) {
        	int j = i % 8, k = i / 8;
        	this.drawTexturedModalRect(x + j * 4, y + k * 4, 86 + (tileEntity.ram[k] >> j & 1) * 4, 45, 4, 4);
        }
        for (int k = 8; k < 16; k++) {
        	if ((tileEntity.var >> (k + 16) & 1) != 0) 
        		for (int j = 0; j < 8; j++)
        			this.drawTexturedModalRect(x + j * 4, y + k * 4, 86 + (tileEntity.ram[k] >> j & 1) * 4, 49, 4, 4);
        }
        String s;
        x = guiLeft + 58;
        for (int i = 0; i < 6; i++) {
        	y = guiTop + 36 + i * 9;
        	if (i == sel) text.draw(x, y, 0x80ffff, 0xffff8080);
        	else {
        		s = String.format("%02X", tileEntity.getConfig(i));
        		this.fontRendererObj.drawString(s, x, y, 0x80ffff);
        	}
        }
        this.drawStringCentered(tileEntity.getName(), this.guiLeft + this.xSize / 2, this.guiTop + 4, 0x404040);
        this.drawStringCentered(String.format("%.2f", (float)tileEntity.netData.ints[0] / 20F).concat("s"), this.guiLeft + 61, this.guiTop + 20, 0x404040);
        
    }
    
    @Override
    protected void keyTyped(char c, int k) throws IOException {
    	if (sel >= 0) {
    		byte r = text.keyTyped(c, k);
    		if (r == 1) this.setTextField(-1);
    		else if (r >= 0) this.setTextField((sel + r + 5) % 6);
    	} else super.keyTyped(c, k);
    }
    
    private void setTextField(int k) throws IOException {
		if (k == sel) return;
		if (sel >= 0) try {
			tileEntity.setConfig(sel, Integer.parseInt(text.text, 16));
				this.sendCurrentChange((byte)0);
			} catch(NumberFormatException e) {}
		if (k >= 0) {
			text.text = String.format("%02X", tileEntity.getConfig(k));
			if (sel < 0) text.cur = text.text.length();
		}
		sel = k;
	}
    
    private void sendCurrentChange(byte a) throws IOException {
		PacketBuffer dos = tileEntity.getPacketTargetData();
		dos.writeByte(AutomatedTile.CmdOffset + a);
		if (a == 0) dos.writeLong(tileEntity.netData.longs[0]);
		else if (a == 1) dos.writeInt(tileEntity.netData.ints[0]);
		BlockGuiHandler.sendPacketToServer(dos);
	}
    
    @Override
    protected void mouseClicked(int x, int y, int b) throws IOException
    {
    	byte a = -1;
        int k = (y - this.guiTop - 35) / 9;
        if (k < 0 || k >= 6) k = -1;
        if (this.isPointInRegion(25, 15, 18, 9, x, y)) {
            a = 2;
        } else if (this.isPointInRegion(43, 28, 18, 5, x, y)) {
            tileEntity.netData.ints[0] -= 20;
            if (tileEntity.netData.ints[0] < 1) tileEntity.netData.ints[0] = 1;
            a = 1;
        } else if (this.isPointInRegion(61, 28, 18, 5, x, y)) {
            tileEntity.netData.ints[0] -= 1;
            if (tileEntity.netData.ints[0] < 1) tileEntity.netData.ints[0] = 1;
            a = 1;
        } else if (this.isPointInRegion(61, 15, 18, 5, x, y)) {
            tileEntity.netData.ints[0] += 1;
            if (tileEntity.netData.ints[0] > 1200) tileEntity.netData.ints[0] = 1200;
            a = 1;
        } else if (this.isPointInRegion(43, 15, 18, 5, x, y)) {
            tileEntity.netData.ints[0] += 20;
            if (tileEntity.netData.ints[0] > 1200) tileEntity.netData.ints[0] = 1200;
            a = 1;
        } else if (this.isPointInRegion(47, 35, 9, 54, x, y)) {
        	tileEntity.setConfig(k + 6, (tileEntity.getConfig(k + 6) + 1) % 4);
        	a = 0;
        } else if (this.isPointInRegion(7, 15, 18, 9, x, y)) {
            tileEntity.netData.longs[0] ^= 1L << 60;
            a = 0;
        }
        if (!this.isPointInRegion(57, 35, 14, 54, x, y)) k = -1;
        if (k != sel) this.setTextField(k);
        else if (a >= 0) this.sendCurrentChange(a);
        else super.mouseClicked(x, y, b);
    }
    
}
